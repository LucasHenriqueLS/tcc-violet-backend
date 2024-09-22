package br.com.tcc.json.request.textToSpeech;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AudioConfig {

    private String audioEncoding;
    
    public AudioConfig() {
    	audioEncoding = "MP3";
    }
}