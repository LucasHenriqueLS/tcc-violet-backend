package br.com.tcc.json.response.vqa;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VQAResponse {

	private List<String> predictions;

    private String deployedModelId;
    
    public String predictionsAsString() {
    	return predictions.stream().map(caption -> caption + ";").collect(Collectors.joining());
    }
}