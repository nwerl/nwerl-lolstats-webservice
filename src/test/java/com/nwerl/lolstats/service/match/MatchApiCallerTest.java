package com.nwerl.lolstats.service.match;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwerl.lolstats.web.domain.match.MatchRepository;
import com.nwerl.lolstats.web.dto.riotApi.matchreference.RiotMatchListDto;
import com.nwerl.lolstats.web.dto.riotApi.matchreference.RiotMatchReferenceDto;
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
@ImportAutoConfiguration(classes = {MatchApiRestCaller.class})
@RestClientTest(value = MatchApiRestCaller.class)
public class MatchApiCallerTest {
    @Autowired
    private MockRestServiceServer mockServer;
    @Autowired
    private MatchApiCaller matchApiCaller;
    @Value("${apikey}")
    private String apiKey;

    @Test
    public void get_MatchReference_And_Match_Test() throws JsonProcessingException {
        //given
        String accountId = "thisis1234!";
        Long gameId = 1234L;
        String uri  = "https://kr.api.riotgames.com/lol/match/v4/matchlists/by-account/"+accountId+"?api_key="+apiKey+"&endIndex=1";
        List<RiotMatchReferenceDto> list = new ArrayList<>();
        list.add(RiotMatchReferenceDto.builder().gameId(gameId).build());
        ObjectMapper objectMapper = new ObjectMapper();

        String expectResult = objectMapper.writeValueAsString(RiotMatchListDto.builder().matches(list).build());
        mockServer.expect(requestTo(uri)).andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));

        //when
        RiotMatchReferenceDto riotMatchReferenceDto = matchApiCaller.callApiLastMatchReference(accountId);

        //then
        assertThat(riotMatchReferenceDto.getGameId(), is(gameId));
    }
}
