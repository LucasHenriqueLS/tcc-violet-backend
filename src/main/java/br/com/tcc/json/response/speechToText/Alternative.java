package br.com.tcc.json.response.speechToText;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Alternative {

    private String transcript;

    private Double confidence;
}
