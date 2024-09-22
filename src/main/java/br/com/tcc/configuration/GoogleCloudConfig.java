package br.com.tcc.configuration;

import java.io.IOException;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;

@Configuration
public class GoogleCloudConfig {

    @Bean
    GoogleCredentials googleCredentials() throws IOException {
        return GoogleCredentials.getApplicationDefault().createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
    }
}