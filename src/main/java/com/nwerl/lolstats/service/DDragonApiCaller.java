package com.nwerl.lolstats.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.jndi.toolkit.url.Uri;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.List;


@Component
public class DDragonApiCaller {
    private final RestTemplate restTemplate;
    private String version;

    public DDragonApiCaller (RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.requestFactory(()->new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .setConnectTimeout(Duration.ofMillis(20000))
                .setReadTimeout(Duration.ofMillis(20000))
                .additionalMessageConverters(new StringHttpMessageConverter(Charset.forName("UTF-8")))
                .build();

        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory
                (UriComponentsBuilder.newInstance()
                        .scheme("https").host("ddragon.leagueoflegends.com")));
    }

    @PostConstruct
    private String callApiCurrentLOLVersion() {
        String uri =  UriComponentsBuilder.newInstance()
                .path("/api").path("/versions.json")
                .build().toString();

        version = (String) (restTemplate.getForObject(uri, List.class).get(0));

        return version;
    }

    public JsonNode callListApi(String jsonName) {
        String uri = UriComponentsBuilder.newInstance()
                .path("/cdn").path("/"+version).path("/data").path("/ko_KR").path("/"+jsonName+".json")
                .build().toString();

        return restTemplate.getForObject(uri, JsonNode.class);
    }

    public byte[] callImgApi(String assetName, String imgName) {
        String uri = UriComponentsBuilder.newInstance()
                .path("/cdn").path("/"+version).path("/img/"+assetName).path("/"+imgName+".png")
                .build().toString();

        if(assetName.contains("perk-images/Styles")) {
            uri = uri.replace("/"+version, "");
        }

        System.out.println(uri);

        return restTemplate.getForObject(uri, byte[].class);
    }
}
