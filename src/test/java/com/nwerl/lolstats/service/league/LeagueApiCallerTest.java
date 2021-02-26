package com.nwerl.lolstats.service.league;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwerl.lolstats.web.dto.riotApi.league.LeagueItemDto;
import com.nwerl.lolstats.web.dto.riotApi.league.LeagueListDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@ImportAutoConfiguration(classes = {LeagueApiRestCaller.class})
@RestClientTest(value = LeagueApiRestCaller.class)
public class LeagueApiCallerTest {
    @Autowired
    private LeagueApiCaller leagueApiCaller;
    @Autowired
    private MockRestServiceServer mockServer;
    @Value("${apikey}")
    private String apiKey;


    @Test
    public void getChallengerLeagueItem_Test() throws JsonProcessingException {
        //given
        String name = "Vehumet";
        String uri  = "https://kr.api.riotgames.com/lol/league/v4/challengerleagues/by-queue/RANKED_SOLO_5x5?api_key="+apiKey;
        ObjectMapper objectMapper = new ObjectMapper();
        List<LeagueItemDto> list = new ArrayList<>();
        list.add(LeagueItemDto.builder().summonerName("Vehumet").build());

        String expectResult = objectMapper.writeValueAsString(new LeagueListDto(list));
        mockServer.expect(requestTo(uri))
                .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));

        //when
        LeagueListDto leagueListDto = leagueApiCaller.callApiChallengerLeagueItem();

        //then
        assertThat(leagueListDto.getEntries().get(0).getSummonerName(), is(name));
    }

    @Test
    public void getChallengerLeagueList_5xxError_Test() {
        //given
        String uri  = "https://kr.api.riotgames.com/lol/league/v4/challengerleagues/by-queue/RANKED_SOLO_5x5?api_key="+apiKey;
        mockServer.expect(requestTo(uri)).andRespond(withServerError());

        //when
        LeagueListDto leagueListDto = leagueApiCaller.callApiChallengerLeagueItem();

        //then

    }
}