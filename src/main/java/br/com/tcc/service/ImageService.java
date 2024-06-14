package br.com.tcc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import br.com.tcc.json.request.vqa.VQARequest;
import br.com.tcc.json.response.vqa.VQAResponse;
import reactor.core.publisher.Mono;

@Service
public class ImageService {
	
	@Autowired
	private GoogleCredentialsService googleCredentialsService;
	
	@Value("${project_id}")
	private String projectId;
	
	private final WebClient webClient;

    public ImageService() {
        webClient = WebClient.builder()
        					 .baseUrl("https://us-central1-aiplatform.googleapis.com")
        					 .build();
    }

	public Mono<String> ask(String question, String image) {
		return requestVQA(new VQARequest(question, image));
	}
	
	public Mono<String> generateCaptions(String image) {
		return requestVQA(new VQARequest(image));
	}
	
	private Mono<String> requestVQA(VQARequest body) {
    	return webClient.post()
                .uri(String.format("/v1/projects/%s/locations/us-central1/publishers/google/models/imagetext:predict", projectId))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", googleCredentialsService.getToken()))
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .bodyToMono(VQAResponse.class)
                .map(response -> response.predictionsAsString());
    }
}