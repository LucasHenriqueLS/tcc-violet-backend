package br.com.tcc.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import br.com.tcc.json.request.llm.LLMRequest;
import br.com.tcc.json.response.llm.LLMResponse;
import reactor.core.publisher.Mono;

@Service
public class LLMService {
	
	@Value("${openai_api_key}")
	private String openaiApiKey;
	
	private final WebClient webClient;

    public LLMService() {
        webClient = WebClient.builder()
        					 .baseUrl("https://api.openai.com")
        					 .build();
    }
    
    public Mono<String> summarizeAndTranslateQuestion(String question) {
        return requestLLM(3, new LLMRequest(String.format("Resuma a seguinte pergunta e retorne somente a pergunta (e nada mais que a pergunta) resumida traduzida para inglês: %s", question)));
//        return Mono.just("Where are we?");
    }
    
    public Mono<String> contextualizeAndTranslateAnswers(String question, String answers, String captions) {
    	return requestLLM(3, new LLMRequest(String.format("Em uma imagem de um ambiente no mundo real, que possui a(s) seguinte(s) legenda(s) (em inglês): %s, foi feita a seguinte pergunta (em inglês): %s, cujo um modelo de VQA forneceu a(s) seguinte(s) resposta(s) (em inglês): %s. Forme uma frase (em português), como se fosse uma pessoa respondendo informalmente outra, de forma natural e agradável, que responda à pergunta que foi feita sobre o ambiente capturado pela imagem.", captions, question, answers)));
//    	return Mono.just("Estamos em um zoológico!");
    }
    
    public Mono<String> describeImage(String description) {
    	return requestLLM(3, new LLMRequest(String.format("Uma imagem de um ambiente no mundo real possui a(s) seguinte(s) legenda(s): %s. Forme uma frase, como se fosse uma pessoa respondendo informalmente outra, de forma natural e agradável, descrevendo o ambiente capturado pela imagem com base na descrição que lhe foi fornecida.", description)));
    }
    
    private Mono<String> requestLLM(Integer attempts, LLMRequest body) {
    	return webClient.post()
                .uri("/v1/chat/completions")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", openaiApiKey))
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .bodyToMono(LLMResponse.class)
                .map(response -> response.getContent())
                .onErrorResume(ex -> {
	            	ex.printStackTrace();
	            	if (attempts > 0) {
	            		return requestLLM(attempts - 1, body);	            		
	            	}
	            	throw new RuntimeException(ex.getMessage());
	            });
    }
}