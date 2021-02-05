package com.nwerl.lolstats.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwerl.lolstats.config.ApiRequestConfig;
import com.nwerl.lolstats.web.domain.league.LeagueItemRepository;
import com.nwerl.lolstats.web.dto.riotApi.league.LeagueItemDto;
import com.nwerl.lolstats.web.dto.riotApi.league.LeagueListDto;
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
@RestClientTest(value = LeagueService.class)
public class LeagueServiceTest {
    @Autowired
    private LeagueService leagueService;
    @Autowired
    private MockRestServiceServer mockServer;
    @MockBean
    private LeagueItemRepository leagueItemRepository;
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
        LeagueListDto leagueListDto = leagueService.getChallengerLeagueItem();

        //then
        assertThat(leagueListDto.getEntries().get(0).getSummonerName(), is(name));
    }
}