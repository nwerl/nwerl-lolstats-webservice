package com.nwerl.lolstats.service.summoner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwerl.lolstats.web.domain.summoner.SummonerRepository;
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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@ImportAutoConfiguration(classes = {SummonerApiRestCaller.class})
@RestClientTest(value = SummonerApiRestCaller.class)
public class SummonerApiCallerTest {
    @Autowired
    private MockRestServiceServer mockServer;
    @Value("${apikey}")
    private String apiKey;
    @Autowired
    private SummonerApiCaller summonerApiCaller;


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
        SummonerDto summonerDto = summonerApiCaller.callApiSummonerInfoByName(name);

        //then
        assertThat(summonerDto.getName(), is(name));
    }

    @Test
    public void getSummonerInfoBySummonerId_Test() throws JsonProcessingException {
        //given
        String id = "O7PJD4sKhmJu1jDhvXmU9oPTNQHiBbGkXqPkM62KZ_VAkSbd-avo2Sxc";
        String uri  = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/"+id+"?api_key="+apiKey;
        ObjectMapper objectMapper = new ObjectMapper();


        String expectResult = objectMapper.writeValueAsString(SummonerDto.builder().id(id).build());
        mockServer.expect(requestTo(uri))
                .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));

        //when
        SummonerDto summonerDto = summonerApiCaller.callApiSummonerInfoBySummonerId(id);

        //then
        assertThat(summonerDto.getId(), is(id));
    }
}
