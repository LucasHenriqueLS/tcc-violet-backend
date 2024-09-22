package br.com.tcc.json.request.vqa;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VQARequest {

	private List<Instance> instances;

    private Parameters parameters;
    
    public VQARequest(String bytesBase64Encoded) {
    	instances = List.of(new Instance(bytesBase64Encoded));
    	parameters = new Parameters();
    }
    
    public VQARequest(String prompt, String bytesBase64Encoded) {
    	instances = List.of(new VQAInstance(prompt, bytesBase64Encoded));
    	parameters = new Parameters();
    }
}