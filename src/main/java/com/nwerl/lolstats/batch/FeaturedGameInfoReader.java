package com.nwerl.lolstats.batch;

import com.nwerl.lolstats.service.RiotApiRequestService;
import com.nwerl.lolstats.web.dto.FeaturedGameInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@StepScope
@Configuration
public class FeaturedGameInfoReader implements ItemReader<List<FeaturedGameInfoDto>> {
    private final RiotApiRequestService riotApiRequestService;
    private Boolean readFlag;

    @Autowired
    public FeaturedGameInfoReader(RiotApiRequestService riotApiRequestService) {
        this.riotApiRequestService = riotApiRequestService;
        this.readFlag = false;
    }

    @Override
    public List<FeaturedGameInfoDto> read() throws Exception {
        if(readFlag) {
            return null;
        }
        else {
            readFlag = true;
            return riotApiRequestService.getFeaturedGameInfo();
        }
    }
}