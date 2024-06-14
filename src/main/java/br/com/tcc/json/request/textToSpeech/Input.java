package br.com.tcc.json.request.textToSpeech;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Input {

    private String text;
    
    public Input(String text) {
    	this.text = text;
    }
}