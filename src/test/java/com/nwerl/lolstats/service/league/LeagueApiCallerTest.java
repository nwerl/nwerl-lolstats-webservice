package com.nwerl.lolstats.service.league;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwerl.lolstats.web.dto.riotapi.league.RiotLeagueItemDto;
import com.nwerl.lolstats.web.dto.riotapi.league.RiotLeagueListDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
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
        List<RiotLeagueItemDto> list = new ArrayList<>();
        list.add(RiotLeagueItemDto.builder().summonerName("Vehumet").build());

        String expectResult = objectMapper.writeValueAsString(new RiotLeagueListDto(list));
        mockServer.expect(requestTo(uri))
                .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));

        //when
        RiotLeagueListDto riotLeagueListDto = leagueApiCaller.fetchChallengerLeagueListFromRiotApi();

        //then
        assertThat(riotLeagueListDto.getEntries().get(0).getSummonerName(), is(name));
    }

    @Test
    public void getChallengerLeagueList_5xxError_Test() {

    }
}