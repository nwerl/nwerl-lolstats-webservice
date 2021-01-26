package com.nwerl.lolstats.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwerl.lolstats.web.dto.SummonerDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(value = RiotApiRequestService.class)
public class RiotApiRequestServiceTest {
    @Autowired
    private RiotApiRequestService riotApiRequestService;
    @Autowired
    private MockRestServiceServer mockServer;
    @Value("${apikey}")
    private String apiKey;

    private String riotUrl = "https://kr.api.riotgames.com/lol";

    @Test
    public void getSummonerInfoByName_Test() throws JsonProcessingException {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        String summonerUrl = "/summoner/v4/summoners/by-name/";
        String name = "Vehumet";
        String expectResult = objectMapper.writeValueAsString(SummonerDto.builder().name("Vehumet").build());
        mockServer.expect(requestTo(riotUrl+summonerUrl+name+"?api_key="+apiKey))
                .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));

        //when
        SummonerDto summonerDto = riotApiRequestService.getSummonerInfoByName(name);

        //then
        assertThat(name, is(summonerDto.getName()));
    }
}
