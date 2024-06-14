package br.com.tcc.json.request.speechToText;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Audio {

	private String content;
	
	public Audio(String content) {
		this.content = content;
	}
}