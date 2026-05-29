package com.vk.controller;



import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vk.dto.AuthDTO;
import com.vk.dto.ProfileDTO;
import com.vk.service.ProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor

public class ProfileController {
	
	private final ProfileService profileService;
	
	
	@PostMapping("/register")
	public ResponseEntity<ProfileDTO> registerProfile(@RequestBody ProfileDTO profileDTO){
		
		ProfileDTO registerProfile = profileService.registerProfile(profileDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(registerProfile);	
	}
	
	
	@GetMapping("/activate")
	public ResponseEntity<String>activateProfile(@RequestParam String token){
		
		boolean isActivated = profileService.activateProfile(token);
		
	    if (isActivated) {
	        String html = "<html><body style='text-align:center; font-family:Arial;'>"
	                + "<h1 style='color:green;'>✔ Profile activated successfully!</h1>"
	                + "<p>You can now <a href='/login'>login</a> to your account.</p>"
	                + "</body></html>";
	        return ResponseEntity.ok().body(html);
	    } else {
	        String html = "<html><body style='text-align:center; font-family:Arial;'>"
	                + "<h1 style='color:red;'>✖ Activation Token Not Found or Already Used.</h1>"
	                + "<p>Please request a new activation link.</p>"
	                + "</body></html>";
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(html);
	    }
	}
	
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody AuthDTO authDTO){
			
		try {
			   if(!profileService.isAcountActive(authDTO.getEmail())) {				   
				   return ResponseEntity.status(HttpStatus.FORBIDDEN).body( Map.of("message","Acount is not active. Please activate your account first."));   
			   }
			   
			   Map<String, Object> response = profileService.authenticateAndGeneratedToken(authDTO);

			   return ResponseEntity.ok(response);
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message",e.getMessage()));
		}
	}

	@GetMapping("/profile")
	public ResponseEntity<ProfileDTO> getPublicProfile(){
		
		ProfileDTO profileDTO = profileService.getPublicProfile(null);
		
		return ResponseEntity.ok(profileDTO);
		
	}

	
	
	
	
	
	
	
	
	
	
}
