package com.nwerl.lolstats.service.datadragon;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
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
public class DataDragonApiRestCaller implements DataDragonApiCaller {
    private final RestTemplate restTemplate;
    private String version;
    private static final String lolVersionUri = "/api/versions.json";
    private static final String listApiURi = "/cdn/%s/data/ko_KR/%s.json";
    private static final String imageApiUri = "/cdn%s/img%s/%s.png";

    public DataDragonApiRestCaller(RestTemplateBuilder restTemplateBuilder,
                                   @Value("${datadragon.protocol}") String scheme,
                                   @Value("${datadragon.hostname}") String hostname){
        restTemplate = restTemplateBuilder.requestFactory(()->new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .setConnectTimeout(Duration.ofMillis(20000))
                .setReadTimeout(Duration.ofMillis(20000))
                .additionalMessageConverters(new StringHttpMessageConverter(Charset.forName("UTF-8")))
                .build();

        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory
                (UriComponentsBuilder.newInstance().scheme(scheme).host(hostname)));
    }

    @PostConstruct
    public String callApiCurrentLOLVersion() {
        version = (String) (restTemplate.getForObject(lolVersionUri, List.class).get(0));
        return version;
    }

    public JsonNode callListApi(String jsonName) {
        return restTemplate.getForObject(String.format(listApiURi, version, jsonName), JsonNode.class);
    }

    public byte[] callImgApi(String path, String imgName) {
        String versionString = "";

        if(!path.contains(DataDragonPath.RUNE_STYLE.getApiPath()))
            versionString = "/"+version;

        return restTemplate.getForObject(String.format(imageApiUri, versionString, path, imgName), byte[].class);
    }
}
