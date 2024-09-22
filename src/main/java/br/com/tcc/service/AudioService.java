package br.com.tcc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import br.com.tcc.json.request.speechToText.SpeechToTextRequest;
import br.com.tcc.json.request.textToSpeech.TextToSpeechRequest;
import br.com.tcc.json.response.speechToText.SpeechRecognitionResponse;
import br.com.tcc.json.response.textToSpeech.TextSynthesizeResponse;
import reactor.core.publisher.Mono;

@Service
public class AudioService {

	@Value("${project_id}")
	private String projectId;
	
	@Autowired
	private GoogleCredentialsService googleCredentialsService;
	
	private final WebClient speechRecognizeWebClient;
	
	private final WebClient textSynthesizeWebClient;

    public AudioService() {
        speechRecognizeWebClient = WebClient.builder()
        					 .baseUrl("https://speech.googleapis.com")
        					 .build();
        textSynthesizeWebClient = WebClient.builder()
							 .baseUrl("https://texttospeech.googleapis.com")
							 .build();
    }
    
    public Mono<String> transcribeAudio(String audio) {
        return requestSpeechToText(new SpeechToTextRequest(audio));
    }

    public Mono<String> requestSpeechToText(SpeechToTextRequest body) {
        return speechRecognizeWebClient.post()
                .uri("/v1/speech:recognize")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", googleCredentialsService.getToken()))
                .header("x-goog-user-project", projectId)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .bodyToMono(SpeechRecognitionResponse.class)
                .map(response -> response.getResults().get(0).getAlternatives().get(0).getTranscript());
    }
    
    public Mono<String> synthesizeText(String text) {
        return requestTextToSpeech(new TextToSpeechRequest(text));
    }

    public Mono<String> requestTextToSpeech(TextToSpeechRequest body) {
    	return textSynthesizeWebClient.post()
                .uri("/v1/text:synthesize")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", googleCredentialsService.getToken()))
                .header("x-goog-user-project", projectId)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .bodyToMono(TextSynthesizeResponse.class)
                .map(response -> response.getAudioContent());
	}
    
    public Mono<String> IAmSorry() {
    	return Mono.just("//NExAAAAANIAAAAAExBTUUzLjEwMFVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV"
    			+ "VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVTEFNRTMu//NExFMAAANIAAAAADEwMFVVVVVVVVVVVVVVVVVVVVVVVVV"
    			+ "VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVTEFNRTMu//NExKYAAA"
    			+ "NIAAAAADEwMFVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV"
    			+ "VVVVVVVVVVVVVVVVVVVTEFNRTMu//NExKwAAANIAAAAADEwMFVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV"
    			+ "VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVTEFNRTMu//NExKwAAANIAAAAADEwMFVVVVV"
    			+ "VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV"
    			+ "VVTEFNRTMu//NExKwAAANIAAAAADEwMFVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV"
    			+ "VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVTEFNRTMu//NExKwAAANIAAAAADEwMFVVVVVVVVVVVVVVVVVVVVVV"
    			+ "VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV8ZYI54KQ//NExKw"
    			+ "AAANIAAAAAAFp4V4X/8Tm5v/jrFxmBX/9nHLGTJz/8TeHRAMIAPhHnP/8WQXSfI0TubCl///zBhc4tQcoAcAuAnAsx/"
    			+ "//+IXNnIeOMUGVicL5QIgUP////8tE//NExKwAAANIAAAAAPkiQ4UoDY4L8TmIBlMcZuUyfcElP3p6p/q9PqR/72rUp"
    			+ "v9XWg63//7KrTW//0NBtlprW5fS//axutzMvm5ogtSiYS5dMiabjGNzUwLdDb/1pmZm//NExKwAAANIAUAAAG5qspkY"
    			+ "e6hLz6G5eJ44w5QcwJwIyF1C5DvHAagOtYSa9P98ypvOcfGf/r///H+NQY9fTHzj//ev8a/+vjVcXzv51v//7+8fH8v"
    			+ "xvGq+2om9Zjy6//NExP8asyncAY2QAMx77n76+XlPDhwYFaXhK3tjfV9I1wHrY2SNS9K+YnLCrdwmeuXzPEUz1vZ0Nw"
    			+ "yJtVyqVff4OUsDA1l26tT5kHArD/fsxdBbCWHCepYlW1TBqgt1//NExOcXoyqAAYVoAPv5o1lL+2ysvapPMcEDspyOp"
    			+ "Gn2rZfPctfj19KYz9/f/z8+uM/53WK3vZoWmOFAxeNTb7voMJ7Dy6yrtzsUjt68a4Vdx4zyHWjE808hwFa6Tzeu//NE"
    			+ "xNshWx6EAcF4AdVKXD42kKT6EMh4wE+di7IUSVvaDeTCFp1Lrh0hp0G+HSXIYQXo+iXFWXxQO61DPnm+S+rozmVfIRz"
    			+ "p1+hCKoQFCEIc5FJiYu3Rp3RC2bP+b2p///NExKggqyaEAAievP019fesfVM71E1emocd+/vFpfGNzxMUdZpCiT4pmJ"
    			+ "BjZj1j51HgKRQRJYzJVIrzI2uUrx+trqZ2rz0WICpkOQ715Osy5ZULbO0KxWxHbNMaqbFg//NExHggWx6MAAlevVWdh"
    			+ "q0PdbXDhOqFC7FHJD+mSbqkaa8vIztFPrfu2//GuK04wHsnyAWQgpA2DMT+FPM32dPYqGw8p5KW1dePn9midCkL0Sc0"
    			+ "FItUjNNJlSeWRFi7//NExEkhSyKUABmSvKiVbqtQmtReMENqzICFAu0LImFyFIbkISQUIYHULBEJ5ikhIlcmUVIUPOL"
    			+ "jagpWFQWFgaKgkRCcUlxgxtX5u3yvGyAIayEcBIRFpA+kmtzcNz/m//NExBYXYwqcANFKuSEiYDh8e72a///7TXoNKg"
    			+ "WIB0HCKJYTA5EYqxAUIq7HO6f9NiIajI53KzWl1b2QzmfzG+Ut7URy0K6t6Gr/MbMLOhuhqjAxpbVre+vCwgyWQI4A/"
    			+ "/NExAsVGeKUANtamEYA4NqzW2JSBzda1JOXGWz5wrKb/V/nvrZurWPbXUdH4KQ8fSQjuDliUG6F0k0C+7+XkGfqRvdp"
    			+ "itkF+c//9npSWcyh6d2RKvIqlH7wryhP8KPB//NExAkS+W6QANtWlPMBlyiYPnO2cD4bya0eZG31LEw/mTfz//xX/yi"
    			+ "chtNlwLOWc1x5EbQbbPW60n50SJrEbyQSEg0KuT///5inVe0u51dmtJu5NtyHAEwm+O91Q4hB//NExBAWet6AANtauc"
    			+ "fLph6K1qVOGjTtUcD4WfhtDDn/qN/7P9SXr7LQWyROCLBxlw2rRNR2jJdFt6KP6P+p0P+p6//////el1rRU2+il65km"
    			+ "YklFLeylFepPyBS8tgY//NExAkTCTZ0AOvUcGoUnGKOBg8odGsphrsYAAkP5kveuGxRTxr+ustwBYbHVZUC9XtOPO+r"
    			+ "32oe3vcekvT0eh2VOqU7///90WEwwByxxcdVZq5bsOy4THAYbGwf4+Km//NExA8VUsZsAVs4ATgyCjFc1PnNMcMLAFQ"
    			+ "tRkdSvWwgdzHXCckQFLjxMspAdRjmOqtHOVjF0dlpsv83O/////6srXf////////+1FLPnO5227aVMIFGTCx1gi+TTjS7//NExAwTgMpcAZowAIaTmXJqKFvGiu9NzudmVtAs3TY/OQSLKrWIhXvOvLfPLImwmFDakFNW5IEV6nVxVvWhBwMXa/xCIHBgo5yv7/b/l0cgo+S0CeK0Gp6dQPomApS///NExBEWUuqsAYkoAGPdxLojI0SO5EZSnQhJCMoqizf8jSUMhnTybd2OdjbxMA1H+VRVqnU553JykEBcxRdmGioh1GutDGSx87/TuotYrKpaKIXletW/3nPp26EKU//E//NExAoT0aaUAdpoAGjosIX9BM1yyFRYcThb8Wua5G5fL9FyQIoJOIsuKRSqWOdzBvtver/0/u6fdlrT0VetVVk1mZ9hCxwsRs6FiFhp////+hXeOWdJK0tAumnQFw8A//NExA0V4ZaQAN5ElA8APd9+VCAYbhSjict/srpmUuVLr2Oe2+GB2OTNn/1qpS7x5//crf/1Qz+io/WcYMZLMdjPTODZZsGiH/FwGz///WXD5EA1MoARtdZdvW4OAoOZ//NExAgSOZ6MAN4ElI8ZrBOrEgDrdm4mAtLudbGVT0cf1NJe1De3nSPG9D9bw/901Wz///eZG0/X///KX2fZ70zZFEg1Z/6////lW0fSlGMqirXi7JluInpT8NDtPcCD//NExBIRkLJ8AOYYTFSRleawUjnO/XZGXSNnmr0bCQKj7aQryWu/ZnWj07DAFrNPgRuzIC/7lRf//////6mpjUoqajTRl2keTCoCNSRE1yEESW7CBqi6SgsV/ce/m8co//NExB4QaLKAAOYSTJ7/LSVdtKBQEUUvCDBOSs8CAKP6OnmXgc5/MO///////49EfXtrSF5limEpknD59CRjL1aQpSgFAe4jrcQikN5RSMHgkCQLFh+v/iPeOeYf77t3//NExC8RmLaAAO4YTG5CAIIYnqm39Jt7LuoQHP/////8wc9lKpRHZRBaHQKxszkJExZJSsqEQZxKX7lq1IgefC62GyoOmLe1m594tcvEFCtbEN8HZClhJzXSne/2V1Kd//NExDsQiK6EAOZeTHf////s//XVmcpuIIvmG4yZ0DKlj9zywJv2nAHIM6jd1RprZW6RtB9AxEIm0p1pz970d2v8x4rigZwztx/l2U+s+VcY/////9b3gO515ddKprNq//NExEsR6KqIAOZeTCqYR+roYnahI58YCHqAD9fZwMtPt+u2AowE0WFDXTWnW6PeEBUDcT6iQFKW4/LNx+dk8h5dP8A1DP/////mRgucqi4EsJaax7rrsmyxzAn71kCD//NExFYSYM6MANPUcCM6ARsqhFCFZZJjBO1yAXtbkEg8UQCAaXQc3EzeTyex+IDhynhwNQ/n6nVGpy8Tf///7kLNWOGIF5hYPn5Cw/CnQYPNJVBDU7IACMJlMKx6IAC1//NExF8ScTaQANtKcLQRBflX6ErdG6L5j+vVzdGF3i9lQRAGC9XHBont6eYrccdQgEVX////q2ErnzCYCrWNM/qfQLQx4OlNuGyqjrVy3cBEJh9BgTpob5RvJff3/83o//NExGgRMaKYANKOlDxJ2iIJR1hsJzEUcDZbq3m9H9TTceVpd1f///7q6i22VdCVdb0Yf9SgxPHONoEx0IIiKhYyyCwvFZ/MA2YgU2dAix9qBKGN1FAc089yZZ9vWj6f//NExHYRma6UANqOlDdX40Clcw0QNoHh///9PKXjTv///q9Os6SlutWHXahkHAGMRuZrBuRDYLAMgaIxtMkBFL4JxNRVoByn3pKkF2Mucwz7c5ErVNkgWAKLM1nMnujV//NExIISkbp8ANzKlCbX2bqURAUHXfo/wmDX//b+n9QUHoCx6nWNqVUVpCAJiMNmtLyeAHJMFF1JTGSYYlmUjdJAMya/2al3bqgKiUoVDaCipFIgHIlQ0+lYok8gNA0d//NExIoUcSJkAO4KcA7cQXrFaBan+ttk1//lvfxZr11PtSpAMo47YgLzBHs7QnL2PjDENT2Uant6qbw3h3Dmv9a0GWcmRqJ002syugel6ReTnHqAfUsXXfFTQfdK81s1//NExIsTWIpUAOYGTBPB00hQxOowrqXZ/XXItsZOH8MyL54g4whn+biYhd+tw9jxJb3ZM+HKElHEFy+yZ9kxij2AuBVGG/N6Z9kxOi4FoCoBtj9/TV8cQ9DwnInYmA8P//NExJASWPZMAVsQAPqZqFSDFM1GcKIJQbS8Vie//ugzoNZnQYVBORig8CJFYvHSaMQd4d///oNtt74yjET8tL5LjCHRhzkuD+Q13M2YohzNsQFNqcYgNg7wPxnT0927//NExJkg4yqEAY9oAPnnnnz/wOc8hGFGIRmZhUOioDD26lX5nQzd/dV9Ep//+pXu/zJ62o9JjGfMHlBVzfxK6VI+jz3lXLWcpar6AGWcdwJGlwq5WabD8bMIg9A9AuCD//NExGgToiKgAdgoAPmVdpdSpoBQTtbzATjhGGwTBEhJWWA6g7ZV9rgSSLO/4sMC4Ge1KeVQdRu///Q0fsk0iAgqsf2ZCvkSSvkwKHAMHPkRZlK27AQk2eR8p3Tl35ue//NExGwTSKqMANaSTJSMwF8CQM7ljC+dMb7kyf+pTyTx4JuT+60MBVP/clv///KguSEXcK2Q27DWzFoMHG7qOUw8yqPBw6+1ZAoFJ5pR6HyXphreEncajAD8eLFmVijb//NExHERaKKMANaeTPnWv/v////9a5CiWOj6e3tWr1OIH/6////8JoN0y4uqgSpDKhxnkcGWS1I+FBc2t2EgJfsfWEPaJHhMRyR446el5YZtytcs3S2cQYM8qvZPfr////NExH4SiZKEANvElPr1ATv1SREig99v////vY8KJIypKlosFbiIiI4O6OXNgYGokiESOj06A2Qr9d6I1LWq1/HDNpKgLLX5loVIClPdS83jNUp0Gw1U/iKj/d/4ieo8//NExIYR4TJ0AN4EcN////+qGvK4ii3QwFDgieICAtRZURgQ5hxh5HEvuCu3ukCO98cSwUKIAgoIOMaQgSPKkQFEuVcydTw7lSJ5/OyJENL1Dulqdf9n/9cBQrEgfyKO//NExJERsJZYAN5wTMDUJgdGsSC2oRnoNgUgcGBcwfV8xExMPTLMTDpdf//LSNWssjk1l///7YZGrKCEZl3oCos00BRT///izHfrFtYoZlhDz8TKKOUlxHDMGOXBD1I3//NExJ0QuIoIAMPGTIIKfwjJ0v/2KZk6PKZf6ozu39//qimKOOIByafiYyaf+Zf0u0mubAbKP/0mMQmgTrIBeoHcONJ7ovFhOcluAgSIiuE7A02dXx2Nnjl0DdxfHoG8//NExK0R+WmwAGIGlKOJ8pLg+BD+IPg+UJy78MCCXlwfk4wEHfKeXfEGADn/py701VeR1CiTGAIUmAyIHJ1iHBh3AkMxIBTBACBUBwAhJVHuRHhfBMd7RN27jv8onIgA//NExLgRIU20AHjElBiL1CQuG99bk6hf/2v/4XuwuaNHUP1CAki28Mn/qv9Z/p//f/r9inPDBDqVwobE80wkBBr3BHmDICjYYWDAIASXTlhURgYKAFZENYkXftJR2W3f//NExMYQyHo4ANPGSEkFyKL7XvQRSo/kYSLaWjhH7cfVvfBrDyU+eex2XsZR93duBpXf55xO7bTti7ZwApnA+D7ikXKAgcEBcMBhjIYBBxTkPvF3vn3vw+kvF3kC6lh///NExNUWkTpgAOsScNK3jqtFgsEQUOTYsHMHAV9RUFPvRMCHQSZuJzhP3Dz7Yu6FHRWHJ+5WhpW5Tz/RSZaypi7pyMofPwlgTQmsxh7X7tau3q3cOb/n/nn3f/ji6Y6z//NExM0euVJ8AOYMlI0IqQcaxJGwFDSNjjBSJZ7Gqx43JzZAqQLH1Tqvqfrp///0f/VM05rs7K5h44xaK0HRuEJy9FWTazsSt2AuAm+bZ45WOh5eGDqaVrAGalICWIft//NExKUiCuKMAOYOuLDI9TjIAfoOZZu709RKw7rifYes2EBRlEmHDcn3z6aJP3z///q5Yc/W8cMOa/5Uq7oJAELhiTsJOUyXxIeKMQhSkFxQYKEEhUy0ZkT///9f///K//NExG8fguaMAN4KuJU5WYOpV2UYCgw4giCqaoEiNJKYAVTJA8YlVp6tCGCgOViZZ0peohAJiIfmzCCwKGU87rXHrHTJ2FbZnmpZbARcJk6AGFZ6wzmI+sVSV6xny5Qu//NExEQhMuZ4AOaKuQuJ/6/PGrS39/r+fzPScVHGJqhhg+Z+pn1YweE1j1HCQsAr18v6O37///+9v6topDOKCzmEQkcQCAmVBZ4MtrHJpbO8azkl5h1anHhQYGAocQVL//NExBIVKS50AOZWcJ5ZVAaaBwyJ9WGHPnBjzNJTeWrEpTRVt0EaFF3Vthw+n2zXoqNfX8dOr/hSanbGbBpsGn7c6sO4iO/Z////VgrfrJJWW695EAXvYgARCYSVJ1p4//NExBAWaO5YAVxgAIcFkZ2cXXpcpciO8cZ1Kc5DDzIV2yyfk6ejfLwHkeWq46VgbFr7TObi75+elpcPYLjD1QFGHmWNvQ59Il933Xf6PZRovlQUe/ytvEkLo2rZyxCa//NExAkUgc6QAY+AAAaaEPdcugsBKRi8LqyJqFbhb6BZ8QnE7lcmx3DiKZPfJ9Nbz5ico/p9Btdf/92ZFer/1psmmy00GUsvA0HWflPLf/+f+KuaCxqjoqSD6Nw2uCFs//NExAoSaJKEAdl4AOddACX/WHb13W/bEw8OCLZulEH2tP0psrcu0mSiTb6kH1kFBcAqcd/2/+a9bklhAm9vbp//+j1MWsiQKMNDaEKXVc8KN+YmCgU6rIMCDepAM27E//NExBMSiK6MAN6wTG5JDH1wOOR6VNAMtjrLUtjl4WLDcEy7KvuJuvnlnzXcaiO7k//s8zQVEiDSkP/////01qjWAIlVpP193GgUzO98TCApy/jlk+IEvhi592S4Z2dT//NExBsRuLKMAN6yTAcoy1Io7Uaw7QtHXtvmO//O1+k/ynU7t6fdMCUITQbJcpf///6liJdiO+kigXHVPSQ2noKn5sV8GpWZR2O1WxGq7bvG/NruFAtsOK4tNT8yiDWE//NExCcRaK6MAN4wTKyH8u3s99j+D3yvKPitn+b/9KyVf+Qd/+n3PMnX/J3nP1vBuw5k/RZNj+/4+zlX38BtuVBQUSZ9kM6e3/////////7f/P+fnckk7tUWS1GPIxFB//NExDQSCuqkAMCEuQjuAAhkJRnILI0/kIygbgCCOkIrKvxNz//////pul/770R3XFTSKpxqCboJw4A405AtBYVGHQwsJSiw5DnigquLpccOjw4EREwmJigHDBYdTV/3//NExD4O6xqsAAgKvf/////cv///////////r//4iU/9qmf57WK5hiSuZpOJSYi5fsWCCWFFGX0SjG1DyUINjXWD55EORyCooSMEppIcwXUq38ZxlMAXArFWn2eO2L5p//NExFURaxqsAAhQvbvAxjNgP61LfwLZpu/r/p+p32/9//6fR/X/9qv/lR1mlKUBQ2pZtV9DG1K3qAgJXIFE4gQ0Xfy/8qVW89VUrWtN9Gr7Shg0B6GCJZGsfxzIsqxm//NExGIRKv6sAHhEuZN8wNVPZc3NvuQfCQu+bGnL+40fTfTnpc5GQw6P3/zQ8Pf////+/WrP/yqsrFazvAltt9fFcAEIB7MKMeqQLKCbcyIqj8sQDf+6b/hWmoxhITBE//NExHARGVKsAMtQlN5YeizQiDRYlkRA7B4Qw9DQ1nf+sULhE6f////2/6NNvfjcbqYOKdeEVkWg1nha6AHBb8HvLUYE8sxNaqRkik+3K//Nf//3r+zCwsLIxQtlC1qU//NExH4SqUKoAMzQcDxCBceaIj3/+VERZ////8O/U/cVpVqszUqMKCAxUbD8UhOFokGAtCtN9gZg0LJDssdaUovJ82cbVdwLet8I1N0qkpt//9LG8Ygg6hUJbv/stXqV//NExIYR8TqUANQQcHwJLEqBLySFykUqcMcNoPjAQCxp7OxYDDUeqbnxtHYN3+T7Zch6JZK/9jGMpckuVNf/zSX0bn1R2lA2lgev/8d7jiRMomMAsRQAgrHA2///qn3v//NExJEPURpYAVwQALP3Vj6PTVDZJQ+O7///9lMrZN/vTNWzSNRURP/////74YxlHJe9/7oWG4k8lJSTSo1NjypJbHReAJEgKWCBmhoMcBEYA0kJlVi79bejeZ+/p/+///NExKYhiyqMAZpYAGKCQao6OBGEjOXIlBoYecca+jkDCrQ9HnEmiLZz///5TEjqP+41vb7yHm9OWDm9t4RskHg6MbSnMqE7+Gfi/0N+n1v5xD0PUNJmXyYXCTHkEgKF//NExHIROW6gAdQ4ABUbiYFxConE83VUtBXWyfUmadzjUkP//92aeUEmvVY9jQTKELes+ZAhge4a7lHTPqMlDbm5rHLdQmfmE3nkPz389vU70Xog2YoYQjMEEuREosEg//NExIAUQb6cANnalD4XpO5CeMxZMnGTvQ/ohN6j1EWQHVeh21f///////ueYaXeQu6ssv5ymlgBgJvYtz/BDgLmefzqAzNxi+3/1+3/////8fNXp60haISTA14FGWhi//NExIIVarqgANKUuKWgIrXu5FhMANOss///7XjQqYBcAPssairmsa0pC9xcrzYXZQaVF6oVTDCZKAQuIIOcshn5fyvv2/meUlrmskWRaRcoWESiUbg1BTSp8eKFvvEA//NExH8REU6kAMlMlGFqJt7P///IFGV0dIpV/8N8IKQcRX5jPI2AcDDZNqyZGYbWgTiK1V7eHL9PPbOv80+vvaUoYGHcFWDt4VAQAWadZgPNHzY5ouqb///+tzbz2z0K//NExI0RyVKcAMBSlNY6zuFVkjss75mInAAPGgkioUOQNuRhHvzpt4iWnDj9CvVIHIsVAYCiyRE4DjGiAekoguHWix7/TcID9jFJ////tYHRWzRrz/eDPzD3CVYxn1YM//NExJgRGT6kAMzGcBPwDZD2YuXA9kWI/pjcHE9mR9Ar6MHEpOBzSIVTUeGAn7USgYT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fL/29KJKmWJDxWNCJB46DHArhYBANej9gRdSz9/9RhAR8j86eGYO8f/PdJ4i2hx/X////8i////z/////////437w1VWz//NExFsQgcq0AGhKlUvWrSqfd+NjvTmpFbImNmjPcRkH09vd3z1qjn3klZO6fMRJ+03idzSqtkqJfY18kt16/f//////////+vnmKnbEvzNvO63QsUPHFQjJanFhY2gq//NExGwSoxqsABBMvTaHMLy92MOxUbV7lHiYaNkR0RxkLMmjFHlDRzY3PIGuSLQIh9VIyTL/6////+D////l/////y/p++hO7/yGFMFFNwcyiBgbGptQQgiEtem3WeMe//NExHQSMxaoAAgQuOsNjXZNsIdkUKhkDEGtEUQFQ3AAQVqpGD0136YTiJBzl7gP3bw53JWAWyozAiIx2EShQojuTfL////9f9aUNUpnAUQAxjF6cxREVUOlDUOpg1Iq//NExH4RGxqwAAhGvCoLB3Kzuo80qd6ToieVCRaktY9nATHMEaQWa9LW4MSPJIWClNV42duZKu6mXrea1jfdtab7b2UcAAxyqR11W//r6/uU0ogw+tfGVgUgtenQhKHX//NExIwSQe6oAHhKmDOxGxTdOWHK92VGD+LM72aAJiAdoqlw1IGA8IpbUOWdLZqyZAx3oVpFx31t5//+hCoacUxjJb/uSzggATN/0rZ/////9Sq9/5WQRMzev2+5IDCn//NExJYQcUKcANZKcOrKgQkeA8Lm4zPoug4aT8ziFiHr+MTWHdiHquLtxLv7t0+v/RC+7f8jlWLoUr9ftrMs4oDCIQb/5URFKrOuVhAQcyNL8cUrMe5yopJUZztiDDHm//NExKcRiYKoAMzElM4YSoDLpHZ7SLO73KkZbG6XtEhPTNkOHzMa1/4XW529/Lo4oLaBdtW9/d9Ci3OO3eieb////+Wqyz7NGvYlUBim/Q7GQqbSKXLdVhTCVOSUHU0e//NExLMSEYKoAM5KlCqsWaVimaC/k6MPb1NM6fp0h6hsg2Tyzh8umuk6Ldf+lS0W3MjbunsNxESkg7I1h1oRe4Cmo0nuAKhq9SGeAuU3y0WSMLL0rSfeHnah7OGZNlef//NExL0TkZagAM5KlOJRIpKsFODbGjCw6nRap7RETTZv/fT9//Zpwun/18voT4IsixCVSxH2Gl10+56s79m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xjG3MJPN///66//NExHoUKTqcANYOcO//9vb/29v//nTJe19NqWWxKmZBOfrLY4ghJmdEYoiLuUdEY8QBBV1OozDUAGkuLUkf9L////+f/////////Xt72f+/sh66MdZVPH0H3nuYPjp6//NExHwUMxaYAMlKuBXZDzrpNcsh5NCDPj7XYiaPDg+VQm4tCYSTyJyn+j+CRvq9jUxcIu9b//+///8///////9vb/eqfev7sjdymYqtopGmNVDjiGcimfUjsyHZAplO//NExH4Rux6cAIBOvKQ6vDnaUQqAylAQgdU13lusxkZVPq4cqf4ykQDHQLlzEG+5hAtzG7v/m/2kflfRGFFCr+Xl7vCsQIv//TZ/5X/QJXfyx6dyoKlAViIOk//nWAAq//NExIoRGxqoAHhEvOt40qiT9OKaQDCNkRZ4AvjfQVE8HtRg2tZ/5Ya/gRbjkpfoQBF3YHCYuCBMtcC6ysA0LU6DNef/U6Rw/////+Tb9Iuqw5q6SgLxoI7ZWSDwGoH1//NExJgQATakANHGcA1QFoEEaw5ZUfSQ6zf6Kp/3G775JJPuMFR1k/SD0z7Smukj/ypR+4ade1X/54TvFT//////6uf/IeH3qSCMomDqxpCtYo+hDZ72UpeY//q/3/XH//NExKsSCS6oANSQcPciJfemSlsqVETOlQCgoST/vPS2VPYoUK2qaRNuSqO/9DLEnXKf/////9fHX6fUJAfWhnF8mI6mrex2QwO91EuiG8yvjH1Iw5iVmM4X5en8UuW///NExLURQT6wAMzWcJLKfr3imb8J4npaHAwM0s+AAxPiAQOxPQH//w8qfTD4FzMlqWoxaqk/h///8if//L/n9fcb5u//P/l7/+b0cYujaFLR6IFTn3B4VctJ0Q3vnlvW//NExMMR+TaoAMvScHgmBKzc0+8ej6evaF3us89iCFmRsY+8usPveZkIY37n6HePfjwTPq3//L//Wj//9f//////1//5R/ytvL3E4vhK7ZyOylKDqreiB411D6VkQJLy//NExM4RCaakAMlGlECO0bQlIl+nSBVzmI6yRausGmJyZky9FipDFL7eI1VDZAxollmdARZrKjE///////////P//////////23juzfe/xvuIXPzfupIU2looJl02MQo//NExNwWQwKsAGhMuStzDqSpJImftEG1Nm3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qqqqqTEFNRTMu//NExOAQ0KHQAGJMTDEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqTEFNRTMu//NExKwAAANIAAAAADEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NExKwAAANIAAAAAKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NExKwAAANIAAAAAKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
    }
}