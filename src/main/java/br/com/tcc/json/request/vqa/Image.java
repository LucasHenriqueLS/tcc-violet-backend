package br.com.tcc.json.request.vqa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Image {

    private String bytesBase64Encoded;
    
    public Image(String bytesBase64Encoded) {
    	this.bytesBase64Encoded = bytesBase64Encoded;
    }
}