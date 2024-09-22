package br.com.tcc.json.request.vqa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Instance {

	private Image image;
    
    public Instance(String bytesBase64Encoded) {
    	image = new Image(bytesBase64Encoded);
    }
}