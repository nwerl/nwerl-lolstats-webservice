package com.nwerl.lolstats.service.datadragon;

import com.nwerl.lolstats.web.dto.riotapi.datadragon.DataDragonChampionListDto;
import com.nwerl.lolstats.web.dto.riotapi.datadragon.DataDragonItemListDto;
import com.nwerl.lolstats.web.dto.riotapi.datadragon.DataDragonRuneListDto;
import com.nwerl.lolstats.web.dto.riotapi.datadragon.DataDragonSpellListDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Retryable(value = HttpServerErrorException.class, maxAttempts = 3)
@Component
public class DataDragonApiRestCaller implements DataDragonApiCaller {
    private final RestTemplate restTemplate;
    private String version;

    public DataDragonApiRestCaller(RestTemplateBuilder restTemplateBuilder,
                                   @Value("${datadragon.protocol}") String scheme,
                                   @Value("${datadragon.hostname}") String hostname){
        restTemplate = restTemplateBuilder.requestFactory(()->new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .setConnectTimeout(Duration.ofMillis(20000))
                .setReadTimeout(Duration.ofMillis(20000))
                .additionalMessageConverters(new StringHttpMessageConverter(StandardCharsets.UTF_8))
                .build();

        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory
                (UriComponentsBuilder.newInstance().scheme(scheme).host(hostname)));
    }

    @PostConstruct
    public String callApiCurrentLOLVersion() {
        version = Optional.ofNullable(restTemplate.exchange(lolVersionUri, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {}).getBody())
                .map(l -> l.get(0))
                .orElse("");

        return version;
    }

    public Map<Long, String> callChampionListApi(String jsonName) {
        return Optional.ofNullable(restTemplate.getForObject(String.format(listApiUri, version, jsonName), DataDragonChampionListDto.class))
                .map(DataDragonChampionListDto::toMap)
                .orElse(Collections.emptyMap());
    }

    public List<String> callItemListApi(String jsonName) {
        return Optional.ofNullable(restTemplate.getForObject(String.format(listApiUri, version, jsonName), DataDragonItemListDto.class))
                .map(DataDragonItemListDto::toList)
                .orElse(Collections.emptyList());
    }

    public Map<Long, String> callRuneStyleListApi(String jsonName) {
        return Optional.ofNullable(restTemplate.exchange(String.format(listApiUri, version, jsonName), HttpMethod.GET, null, new ParameterizedTypeReference<List<DataDragonRuneListDto>>(){}).getBody())
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .collect(Collectors.toMap(DataDragonRuneListDto::getId, DataDragonRuneListDto::getIcon));
    }

    public Map<Long, String> callRuneListApi(String jsonName) {
        return Optional.ofNullable(restTemplate.exchange(String.format(listApiUri, version, jsonName), HttpMethod.GET, null, new ParameterizedTypeReference<List<DataDragonRuneListDto>>(){}).getBody())
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .flatMap(runeList -> runeList.getSlots().stream())
                .flatMap(slot -> slot.getRunes().stream())
                .collect(Collectors.toMap(DataDragonRuneListDto.RiotSlotsDto.RiotRunesDto::getId, DataDragonRuneListDto.RiotSlotsDto.RiotRunesDto::getIcon));
    }

    public Map<Long, String> callSpellListApi(String jsonName) {
        return Optional.ofNullable(restTemplate.getForObject(String.format(listApiUri, version, jsonName), DataDragonSpellListDto.class))
                .map(DataDragonSpellListDto::toMap)
                .orElse(Collections.emptyMap());
    }

    public byte[] callImgApi(String path, String imgName) {
        String versionString = "";

        if(!path.contains(DataDragonPath.RUNE_STYLE.getApiPath()))
            versionString = "/"+version;

        return Optional.ofNullable(restTemplate.getForObject(String.format(imageApiUri, versionString, path, imgName), byte[].class))
                .orElse(new byte[0]);
    }
}
