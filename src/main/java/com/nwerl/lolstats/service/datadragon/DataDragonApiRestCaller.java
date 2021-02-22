package com.nwerl.lolstats.service.datadragon;

import com.nwerl.lolstats.web.dto.riotApi.datadragon.DataDragonChampionListDto;
import com.nwerl.lolstats.web.dto.riotApi.datadragon.DataDragonItemListDto;
import com.nwerl.lolstats.web.dto.riotApi.datadragon.DataDragonRuneListDto;
import com.nwerl.lolstats.web.dto.riotApi.datadragon.DataDragonSpellListDto;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

    public Map<Long, String> callChampionListApi(String jsonName) {
        return restTemplate.getForObject(String.format(listApiURi, version, jsonName), DataDragonChampionListDto.class).toMap();
    }

    public List<String> callItemListApi(String jsonName) {
        return restTemplate.getForObject(String.format(listApiURi, version, jsonName), DataDragonItemListDto.class).toList();
    }

    public Map<Long, String> callRuneStyleListApi(String jsonName) {
        return Arrays.asList(restTemplate.getForObject(String.format(listApiURi, version, jsonName), DataDragonRuneListDto[].class)).stream()
                .collect(Collectors.toMap(DataDragonRuneListDto::getId, DataDragonRuneListDto::getIcon));
    }

    public Map<Long, String> callRuneListApi(String jsonName) {
        return Arrays.asList(restTemplate.getForObject(String.format(listApiURi, version, jsonName), DataDragonRuneListDto[].class)).stream()
                .flatMap(runeList -> runeList.getSlots().stream())
                .flatMap(slot -> slot.getRunes().stream())
                .collect(Collectors.toMap(DataDragonRuneListDto.RiotSlotsDto.RiotRunesDto::getId, DataDragonRuneListDto.RiotSlotsDto.RiotRunesDto::getIcon));
    }

    public Map<Long, String> callSpellListApi(String jsonName) {
        return restTemplate.getForObject(String.format(listApiURi, version, jsonName), DataDragonSpellListDto.class).toMap();
    }

    public byte[] callImgApi(String path, String imgName) {
        String versionString = "";

        if(!path.contains(DataDragonPath.RUNE_STYLE.getApiPath()))
            versionString = "/"+version;

        return restTemplate.getForObject(String.format(imageApiUri, versionString, path, imgName), byte[].class);
    }
}
