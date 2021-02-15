package com.nwerl.lolstats.service;

import com.nwerl.lolstats.web.domain.champion.Champion;
import com.nwerl.lolstats.web.domain.champion.ChampionRepository;
import com.nwerl.lolstats.web.dto.riotApi.ddragon.RiotChampionsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.event.EventListener;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DDragonService {
    private final RestTemplate restTemplate;
    private final ChampionRepository championRepository;
    private String version;

    public DDragonService (RestTemplateBuilder restTemplateBuilder,
                            ChampionRepository championRepository) {
        restTemplate = restTemplateBuilder.requestFactory(()->new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .setConnectTimeout(Duration.ofMillis(20000))
                .setReadTimeout(Duration.ofMillis(20000))
                .additionalMessageConverters(new StringHttpMessageConverter(Charset.forName("UTF-8")))
                .build();

        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory
                (UriComponentsBuilder.newInstance()
                        .scheme("https").host("ddragon.leagueoflegends.com")));

        this.championRepository = championRepository;
    }

    @PostConstruct
    public String callApiCurrentLOLVersion() {
        String uri =  UriComponentsBuilder.newInstance()
                    .path("/api").path("/versions.json")
                    .build().toString();

        version = (String) (restTemplate.getForObject(uri, List.class).get(0));

        return version;
    }

    public RiotChampionsDto callApiChampionList() {
        String uri = UriComponentsBuilder.newInstance()
                    .path("/cdn").path("/"+version).path("/data").path("/ko_KR").path("/champion.json")
                    .build().toString();

        return restTemplate.getForObject(uri, RiotChampionsDto.class);
    }

    public void saveChampionList(List<Champion> champions) {
        for(Champion champ : champions) {
            if(Optional.ofNullable(champ.getThumbNail()).isPresent())
                championRepository.save(champ);
        }
    }

    public byte[] callApiChampionThumbNail(String championName) {
        String uri = UriComponentsBuilder.newInstance()
                    .path("/cdn").path("/"+version).path("/img/champion").path("/"+championName+".png")
                    .build().toString();

        return restTemplate.getForObject(uri, byte[].class);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void updateChampions() {
        log.info("Update Champions started");
        List<Champion> champions = callApiChampionList().toEntity();
        for(Champion champion : champions) {
            if(!championRepository.existsById(champion.getId()))
                champion.setThumbNail(callApiChampionThumbNail(champion.getName()));
        }
        saveChampionList(champions);
        log.info("Update Champions finished");
    }
}