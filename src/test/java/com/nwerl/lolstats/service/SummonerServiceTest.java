package com.nwerl.lolstats.service;


import com.nwerl.lolstats.web.domain.summoner.Summoner;
import com.nwerl.lolstats.web.domain.summoner.SummonerRepository;
import com.nwerl.lolstats.web.dto.riotApi.summoner.SummonerDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class SummonerServiceTest {
    private SummonerService summonerService;

    @Mock
    private SummonerRepository summonerRepository;
    @Mock
    private RiotApiRequestService riotApiRequestService;

    @Test
    public void findByName_if_not_exists_in_DB() {
        //given
        Summoner repoEntity = Summoner.builder().accountId("1").build();
        SummonerDto apiDto = SummonerDto.builder().accountId("1").build();

        Mockito.when(summonerRepository.existsByName(any())).thenReturn(false);
        Mockito.when(summonerRepository.save(any())).thenReturn(repoEntity);
        Mockito.when(riotApiRequestService.getSummonerInfoByName(any())).thenReturn(apiDto);
        summonerService = new SummonerService(summonerRepository, riotApiRequestService);

        //when
        SummonerDto actualDto = summonerService.findByName("test");

        //then
        assertThat(actualDto.getAccountId(), is(apiDto.getAccountId()));
    }
}