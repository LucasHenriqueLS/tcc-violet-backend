package br.com.tcc.json.request.speechToText;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SpeechToTextRequest {

	private Config config;

    private Audio audio;
    
    public SpeechToTextRequest(String content) {
    	config = new Config();
    	audio = new Audio(content);
    }
}