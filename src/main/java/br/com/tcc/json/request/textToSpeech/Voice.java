package br.com.tcc.json.request.textToSpeech;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Voice {

    private String languageCode;

    private String name;

    private String ssmlGender;
    
    public Voice() {
    	languageCode = "pt-BR";
        name = "pt-BR-Neural2-C";
        ssmlGender = "FEMALE";
    }
}