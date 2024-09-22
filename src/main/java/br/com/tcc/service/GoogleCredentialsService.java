package br.com.tcc.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.google.auth.oauth2.GoogleCredentials;

@Service
public class GoogleCredentialsService {

	@Autowired
    private GoogleCredentials googleCredentials;
	
	@GetMapping("/token")
    public String getToken() {
    	 try {
			googleCredentials.refreshIfExpired();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	 return googleCredentials.getAccessToken().getTokenValue();
    }
}
