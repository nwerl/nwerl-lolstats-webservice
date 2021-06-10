package com.nwerl.lolstats.service.slack;

import com.nwerl.lolstats.web.dto.slack.SlackMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Slf4j
@Service
public class SlackSender {
    @Value("${slack.webhookurl}")
    private String webHookUrl;
    private RestTemplate restTemplate;

    public SlackSender(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.requestFactory(()->new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .setConnectTimeout(Duration.ofMillis(20000))
                .setReadTimeout(Duration.ofMillis(20000))
                .build();
    }


    public boolean send(SlackMessage msg) {
        try {
            restTemplate.postForEntity(this.webHookUrl, msg, String.class);
            return true;
        } catch(Exception e) {
            log.info("Slack Message Error Occured!");
            return false;
        }
    }
}