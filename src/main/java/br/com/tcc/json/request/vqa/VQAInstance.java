package br.com.tcc.json.request.vqa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VQAInstance extends Instance {

	private String prompt;
    
    public VQAInstance(String prompt, String bytesBase64Encoded) {
    	super(bytesBase64Encoded);
    	this.prompt = prompt;
    }
}