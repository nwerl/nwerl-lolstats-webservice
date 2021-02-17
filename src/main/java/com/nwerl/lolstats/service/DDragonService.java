package com.nwerl.lolstats.service;

import com.nwerl.lolstats.web.domain.champion.ChampionRepository;
import com.nwerl.lolstats.web.dto.riotApi.ddragon.RiotChampionListDto;
import com.nwerl.lolstats.web.dto.riotApi.ddragon.RiotItemListDto;
import com.nwerl.lolstats.web.dto.riotApi.ddragon.RiotSpellListDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DDragonService {
    private final RestTemplate restTemplate;
    private String version;
    private String basePath;

    public DDragonService (RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.requestFactory(()->new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .setConnectTimeout(Duration.ofMillis(20000))
                .setReadTimeout(Duration.ofMillis(20000))
                .additionalMessageConverters(new StringHttpMessageConverter(Charset.forName("UTF-8")))
                .build();

        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory
                (UriComponentsBuilder.newInstance()
                        .scheme("https").host("ddragon.leagueoflegends.com")));

        basePath = "/home/nwerl/IdeaProjects/nwerl-lolstats-webservice/src/main/resources/static/images";
    }

    @PostConstruct
    public String callApiCurrentLOLVersion() {
        String uri =  UriComponentsBuilder.newInstance()
                    .path("/api").path("/versions.json")
                    .build().toString();

        version = (String) (restTemplate.getForObject(uri, List.class).get(0));

        return version;
    }

    public RiotChampionListDto callApiChampionList() {
        String uri = UriComponentsBuilder.newInstance()
                    .path("/cdn").path("/"+version).path("/data").path("/ko_KR").path("/champion.json")
                    .build().toString();

        return restTemplate.getForObject(uri, RiotChampionListDto.class);
    }

    public byte[] callApiChampionImage(String championName) {
        String uri = UriComponentsBuilder.newInstance()
                    .path("/cdn").path("/"+version).path("/img/champion").path("/"+championName+".png")
                    .build().toString();

        return restTemplate.getForObject(uri, byte[].class);
    }

    public RiotItemListDto callApiItemList() {
        String uri = UriComponentsBuilder.newInstance()
                .path("/cdn").path("/"+version).path("/data/ko_KR").path("/item.json")
                .build().toString();

        return restTemplate.getForObject(uri, RiotItemListDto.class);
    }

    public byte[] callApiItemImage(String itemId) {
        String uri = UriComponentsBuilder.newInstance()
                .path("/cdn").path("/"+version).path("/img/item").path("/"+itemId+".png")
                .build().toString();

        return restTemplate.getForObject(uri, byte[].class);
    }

    public RiotSpellListDto callApiSpellList() {
        String uri = UriComponentsBuilder.newInstance()
                .path("/cdn").path("/"+version).path("/data/ko_KR").path("/summoner.json")
                .build().toString();

        return restTemplate.getForObject(uri, RiotSpellListDto.class);
    }

    public byte[] callApiSpellImage(String spellId) {
        String uri = UriComponentsBuilder.newInstance()
                .path("/cdn").path("/"+version).path("/img/spell").path("/"+spellId+".png")
                .build().toString();

        return restTemplate.getForObject(uri, byte[].class);
    }

    //@EventListener(ApplicationReadyEvent.class)
    public void updateChampions() throws IOException {
        log.info("Update Champions started");

        String assetName = "champion";
        Map<String, Long> champions = callApiChampionList().getData().values().stream()
                .collect(Collectors.toMap(RiotChampionListDto.RiotChampionDto::getId, RiotChampionListDto.RiotChampionDto::getKey));

        for(Map.Entry<String, Long> champion : champions.entrySet()) {
            Path path = Paths.get(basePath+champion.getValue()+".png");
            if(Files.exists(path))  continue;

            byte[] imageBytes = callApiChampionImage(champion.getKey());
            Files.write(path, imageBytes);
        }
        log.info("Update Champions finished");
    }

    public void updateItems() throws IOException {
        log.info("Update Items started");

        String basePath = "/home/nwerl/IdeaProjects/nwerl-lolstats-webservice/src/main/resources/static/images/items/";
        Set<String> itemList = callApiItemList().getData().keySet();

        for(String itemId : itemList) {
            Path path = Paths.get(basePath+itemId+".png");
            if(Files.exists(path))  continue;

            byte[] imgBytes = callApiItemImage(itemId);
            Files.write(path, imgBytes);
        }

        log.info("Update Items finished");
    }

    public void updateSpells() throws IOException {
        log.info("Update Items started");

        String basePath = "/home/nwerl/IdeaProjects/nwerl-lolstats-webservice/src/main/resources/static/images/spells/";
        Set<String> spellList = callApiSpellList().getData().keySet();

        for(String spellId : spellList) {
            Path path = Paths.get(basePath+spellId+".png");
            if(Files.exists(path))  continue;

            byte[] imgBytes = callApiSpellImage(spellId);
            Files.write(path, imgBytes);
        }

        log.info("Update Items finished");
    }
}