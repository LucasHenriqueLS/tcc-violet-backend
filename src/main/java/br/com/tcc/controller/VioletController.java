package br.com.tcc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import br.com.tcc.json.input.Input;
import br.com.tcc.service.AudioService;
import br.com.tcc.service.ImageService;
import br.com.tcc.service.LLMService;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@RestController
@RequestMapping("/violet")
public class VioletController {
	
	@Autowired
	private AudioService audioService;
	
	@Autowired
	private LLMService llmService;
	
	@Autowired
	private ImageService imageService;
	
	// Gerar API Key Google Cloud: gcloud auth application-default print-access-token

	@PostMapping("/ask")
    public Mono<ResponseEntity<String>> ask(@RequestBody Input input) {
		return Mono.zip(vqaAnswers(input).flatMap(c -> log(c)), imageService.generateCaptions(input.getImage()).flatMap(c -> log(c)))
	               .flatMap(tuple -> llmService.contextualizeAndTranslateAnswers(tuple.getT1().getT1(), tuple.getT1().getT2(), tuple.getT2()).flatMap(c -> log(c))
	              	   .flatMap(finalAnswer -> audioService.synthesizeText(finalAnswer))
	                	    .map(response -> ResponseEntity.ok().body(response)))
	               .onErrorResume(ex -> {
		            	ex.printStackTrace();
		            	return audioService.IAmSorry().map(IAmSorry -> ResponseEntity.status(getErrorStatusCode(ex)).body(IAmSorry));
		            });
    }

	private Mono<Tuple2<String, String>> vqaAnswers(Input input) {
		return audioService.transcribeAudio(input.getAudio())													.flatMap(c -> log(c))
				.flatMap(transcription -> llmService.summarizeAndTranslateQuestion(transcription)				.flatMap(c -> log(c))
					.flatMap(translatedQuestion -> imageService.ask(translatedQuestion, input.getImage())		.flatMap(c -> log(c))
						.flatMap(answers -> Mono.just(Tuples.of(translatedQuestion, answers)))));
	}


	@PostMapping("/describe")
    public Mono<ResponseEntity<String>> describe(@RequestBody Input input) {
		return imageService.generateCaptions(input.getImage()).flatMap(c -> log(c))
	               .flatMap(description -> llmService.describeImage(description).flatMap(c -> log(c))
	              	   .flatMap(audioDescription -> audioService.synthesizeText(audioDescription))
	                	    .map(response -> ResponseEntity.ok().body(response)))
	               .onErrorResume(ex -> {
		            	ex.printStackTrace();
		            	return audioService.IAmSorry().map(IAmSorry -> ResponseEntity.status(getErrorStatusCode(ex)).body(IAmSorry));
		            });
    }


	private HttpStatusCode getErrorStatusCode(Throwable ex) {
		return ex instanceof WebClientResponseException ? ((WebClientResponseException) ex).getStatusCode() : HttpStatusCode.valueOf(500);
	}

	private Mono<? extends String> log(String c) {
		System.out.println(String.format("==>> %s", c)); return Mono.just(c);
	}

	private Mono<? extends Tuple2<String, String>> log(Tuple2<String, String> c) {
		System.out.println(String.format("==>> %s", c)); return Mono.just(c);
	}
}