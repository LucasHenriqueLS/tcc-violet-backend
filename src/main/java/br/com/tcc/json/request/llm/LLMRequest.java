package br.com.tcc.json.request.llm;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LLMRequest {

	private String model;

    private List<Message> messages;
    
    public LLMRequest(String content) {
    	model = "gpt-3.5-turbo";
    	messages = List.of(new Message(content));
    }
}