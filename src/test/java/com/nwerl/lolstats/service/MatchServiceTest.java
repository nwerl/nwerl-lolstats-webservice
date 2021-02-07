package com.nwerl.lolstats.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwerl.lolstats.config.RestTemplateConfig;
import com.nwerl.lolstats.web.domain.match.MatchRepository;
import com.nwerl.lolstats.web.dto.riotApi.matchreference.MatchListDto;
import com.nwerl.lolstats.web.dto.riotApi.matchreference.MatchReferenceDto;
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
@ImportAutoConfiguration(classes = {RestTemplateConfig.class})
@RestClientTest(value = MatchService.class)
public class MatchServiceTest {
    @Autowired
    private MatchService matchService;
    @Autowired
    private MockRestServiceServer mockServer;
    @MockBean
    private MatchRepository matchRepository;
    @Value("${apikey}")
    private String apiKey;

    @Test
    public void get_MatchReference_And_Match_Test() throws JsonProcessingException {
        //given
        String accountId = "thisis1234!";
        Long gameId = 1234L;
        String uri  = "https://kr.api.riotgames.com/lol/match/v4/matchlists/by-account/"+accountId+"?api_key="+apiKey+"&endIndex=1";
        List<MatchReferenceDto> list = new ArrayList<>();
        list.add(MatchReferenceDto.builder().gameId(gameId).build());
        ObjectMapper objectMapper = new ObjectMapper();

        String expectResult = objectMapper.writeValueAsString(MatchListDto.builder().matches(list).build());
        mockServer.expect(requestTo(uri)).andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));

        //when
        MatchReferenceDto matchReferenceDto = matchService.callApiLastMatchReference(accountId);

        //then
        assertThat(matchReferenceDto.getGameId(), is(gameId));
    }
}
