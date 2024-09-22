package br.com.tcc.json.request.speechToText;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Config {

	private String encoding;

    private String languageCode;

    private Boolean enableWordTimeOffsets;
    
    public Config() {
    	encoding = "LINEAR16";
    	languageCode = "pt-BR";
    	enableWordTimeOffsets = Boolean.FALSE;
    }
}