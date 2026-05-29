package com.vk.service;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vk.dto.AuthDTO;
import com.vk.dto.ProfileDTO;
import com.vk.entity.ProfileEntity;
import com.vk.repository.ProfileRepository;
import com.vk.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
	
	private final ProfileRepository profileRepository;
	private final EmailService emailService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	
	//@Value("${app.activation.url}")
	private String activationURL = "http://localhost:8080" ;
	
	public ProfileDTO registerProfile(ProfileDTO profileDTO) {
		
		ProfileEntity newProfile = toEntity(profileDTO);
		newProfile.setActivationToken(UUID.randomUUID().toString()); 
		//newProfile.setPassword(passwordEncoder.encode(newProfile.getPassword()));
		newProfile = profileRepository.save(newProfile);
		
		//Send Activation Link to mail 
		
		String activationLink = activationURL + "/api/v1.0/activate?token=" + newProfile.getActivationToken();

		String subject = "Activate your FinTrack Account";

		String body = "<!DOCTYPE html>"
		        + "<html>"
		        + "<head>"
		        + "  <meta charset='UTF-8'>"
		        + "  <style>"
		        + "    body { font-family: Arial, sans-serif; background-color:#f8f9fa; margin:0; padding:20px; }"
		        + "    .container { max-width:600px; margin:0 auto; background:#ffffff; border-radius:8px; padding:20px; box-shadow:0 2px 6px rgba(0,0,0,0.1); }"
		        + "    h2 { color:#2c3e50; }"
		        + "    p { font-size:14px; color:#333; line-height:1.5; }"
		        + "    a.button { display:inline-block; padding:10px 20px; margin-top:15px; background:#007bff; color:#fff; text-decoration:none; border-radius:5px; }"
		        + "    a.button:hover { background:#0056b3; }"
		        + "  </style>"
		        + "</head>"
		        + "<body>"
		        + "  <div class='container'>"
		        + "    <h2>Welcome to FinTrack!</h2>"
		        + "    <p>Thank you for signing up. To activate your account, please click the button below:</p>"
		        + "    <p><a href='" + activationLink + "' class='button'>Activate Account</a></p>"
		        + "    <p>If the button above doesn’t work, copy and paste the following link into your browser:</p>"
		        + "    <p><a href='" + activationLink + "'>" + activationLink + "</a></p>"
		        + "    <br/>"
		        + "    <p style='font-size:12px; color:#888;'>If you didn’t sign up for FinTrack, you can safely ignore this email.</p>"
		        + "  </div>"
		        + "</body>"
		        + "</html>";

		
		emailService.sendEmail(newProfile.getEmail(),subject,body);
		
		return toDTO(newProfile);
		
	}
	
	// for this method we can use model mapper 
	
	public ProfileEntity toEntity(ProfileDTO profileDTO) {
		return ProfileEntity.builder()
				            .id(profileDTO.getId())
				            .fullName(profileDTO.getFullName())
				            .email(profileDTO.getEmail())
				            .password(passwordEncoder.encode(profileDTO.getPassword()))
				            .profileImageUrl(profileDTO.getProfileImageUrl())
				            .createdAt(profileDTO.getCreatedAt())
				            .updatedAt(profileDTO.getUpdatedAt())
				            .build();
	}
	
	public ProfileDTO toDTO(ProfileEntity profileEntity) {
		return ProfileDTO.builder()
				            .id(profileEntity.getId())
				            .fullName(profileEntity.getFullName())
				            .email(profileEntity.getEmail())
				      
				            .profileImageUrl(profileEntity.getProfileImageUrl())
				            .createdAt(profileEntity.getCreatedAt())
				            .updatedAt(profileEntity.getUpdatedAt())
				            .build();
	}
	
	
	//------------------------------------------------------------------------------------
	
	
	public boolean activateProfile(String activationToken) {
		return profileRepository.findByActivationToken(activationToken)
				                .map(profile -> {
			                     profile.setIsActive(true);
			                     profileRepository.save(profile);
			             return true;
			
		              }).orElse(false);			
	}
	
	
	
	
	public boolean isAcountActive(String email) {
		return profileRepository.findByEmail(email)
				                .map(ProfileEntity::getIsActive)
				                .orElse(false);
	}
	
	
	public ProfileEntity getCurrentProfile() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		//String email = authentication.getName();
		return profileRepository.findByEmail(authentication.getName())
				                .orElseThrow(()-> new UsernameNotFoundException("Profile Not Found With Email "+authentication.getName()));		
	}
	
	
	public ProfileDTO getPublicProfile(String email) {
		
		ProfileEntity currentUser = null;
		
		if(email == null) {
			currentUser = getCurrentProfile();
		}else {
			currentUser = profileRepository.findByEmail(email)
			                 .orElseThrow(()-> new UsernameNotFoundException("Profile Not Found With Email "+email));
		}
		
		// we can use alternate for this modelmapper.
		return ProfileDTO.builder()
				         .id(currentUser.getId())
				         .fullName(currentUser.getFullName())
				         .email(currentUser.getEmail())
				         .profileImageUrl(currentUser.getProfileImageUrl())
				         .createdAt(currentUser.getCreatedAt())
				         .updatedAt(currentUser.getUpdatedAt())
				         .build();
	}

	public Map<String, Object> authenticateAndGeneratedToken(AuthDTO authDTO) {
		
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword()));
			
			//Generate JWT token.
			
			String token = jwtUtil.generateToken(authDTO.getEmail());
			
			return Map.of(
					         "token",token,
					         "user",getPublicProfile(authDTO.getEmail())
					      );
								
		} catch (Exception e) {
			
			throw new RuntimeException("Invalid Email or Password.");
		}				
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
