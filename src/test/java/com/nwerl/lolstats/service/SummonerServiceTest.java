package com.nwerl.lolstats.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwerl.lolstats.config.ApiRequestConfig;
import com.nwerl.lolstats.web.domain.match.MatchRepository;
import com.nwerl.lolstats.web.domain.summoner.SummonerRepository;
import com.nwerl.lolstats.web.dto.riotApi.league.LeagueItemDto;
import com.nwerl.lolstats.web.dto.riotApi.league.LeagueListDto;
import com.nwerl.lolstats.web.dto.riotApi.summoner.SummonerDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@ImportAutoConfiguration(classes = {ApiRequestConfig.class})
@RestClientTest(value = SummonerService.class)
public class SummonerServiceTest {
    @Autowired
    private SummonerService summonerService;
    @Autowired
    private MockRestServiceServer mockServer;
    @MockBean
    private SummonerRepository summonerRepository;
    @Value("${apikey}")
    private String apiKey;

    @Test
    public void getSummonerInfoByName_Test() throws JsonProcessingException {
        //given
        String name = "Vehumet";
        String uri  = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/"+name+"?api_key="+apiKey;
        ObjectMapper objectMapper = new ObjectMapper();


        String expectResult = objectMapper.writeValueAsString(SummonerDto.builder().name("Vehumet").build());
        mockServer.expect(requestTo(uri))
                .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));

        //when
        SummonerDto summonerDto = summonerService.getSummonerInfoByName(name);

        //then
        assertThat(summonerDto.getName(), is(name));
    }
}
