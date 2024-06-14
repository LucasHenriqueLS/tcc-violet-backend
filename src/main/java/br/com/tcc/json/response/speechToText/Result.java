package br.com.tcc.json.response.speechToText;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Result {

    private List<Alternative> alternatives;

    private String resultEndTime;

    private String languageCode;
}
