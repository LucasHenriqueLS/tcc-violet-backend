package br.com.tcc.json.request.textToSpeech;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TextToSpeechRequest {

	private Input input;

    private Voice voice;

    private AudioConfig audioConfig;
    
    public TextToSpeechRequest(String text) {
    	input = new Input(text);
    	voice = new Voice();
    	audioConfig = new AudioConfig();
    }
}