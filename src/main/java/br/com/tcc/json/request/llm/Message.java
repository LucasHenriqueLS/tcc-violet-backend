package br.com.tcc.json.request.llm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Message {

    private String role;

    private String content;
    
    public Message(String content) {
    	role = "user";
    	this.content = content;
    }
}