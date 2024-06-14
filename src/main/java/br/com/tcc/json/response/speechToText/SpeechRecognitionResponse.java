package br.com.tcc.json.response.speechToText;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SpeechRecognitionResponse {

    private List<Result> results;

    private String totalBilledTime;

    private String requestId;
}
