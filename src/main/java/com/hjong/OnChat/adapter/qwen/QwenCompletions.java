package com.hjong.OnChat.adapter.qwen;

import com.hjong.OnChat.adapter.Adapter;
import com.hjong.OnChat.adapter.openai.OpenAiRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import static com.hjong.OnChat.adapter.Consts.QWEN;
import static com.hjong.OnChat.adapter.qwen.QwenResponseBody.QwenToOpenAI;
import static com.hjong.OnChat.entity.Consts.DONE;

/**
 * @author HJong
 * @version 1.0
 * @date 2024/3/17
 **/
@Slf4j
@Component("qwen")
public class QwenCompletions extends Adapter {

    @Override
    protected Flux<String> completions(OpenAiRequestBody request, String url, String apikey, Boolean enableProxy) {

        return getWebClient(enableProxy).post()
                .uri(super.buildEndpoint(url, QWEN))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apikey)
                .bodyValue(QwenRequestBody.builder(request))
                .retrieve()
                .bodyToFlux(QwenResponseBody.class)
                .retry(3)
                .flatMap(response -> QwenToOpenAI(response,request.getModel()));
    }

    @Override
    protected Flux<String> streamCompletions(OpenAiRequestBody request,String url, String apikey, Boolean enableProxy) {

        return getWebClient(enableProxy).post()
                .uri(super.buildEndpoint(url, QWEN))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apikey)
                .header(HttpHeaders.ACCEPT, "text/event-stream")
                .bodyValue(QwenRequestBody.builder(request))
                .retrieve()
                .bodyToFlux(QwenResponseBody.class)
                .retry(3)
                .flatMap(response -> QwenToOpenAI(response,request.getModel()))
                .concatWithValues(DONE);
    }



}
