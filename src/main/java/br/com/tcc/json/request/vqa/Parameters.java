package br.com.tcc.json.request.vqa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Parameters {

    private Integer sampleCount;

    private String language;
    
    public Parameters() {
    	sampleCount = 1;
	  	language = "en";
    }
}