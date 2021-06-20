package com.nwerl.lolstats.web;

import com.nwerl.lolstats.service.match.MatchReferenceService;
import com.nwerl.lolstats.web.dto.view.MatchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RestController
public class MatchApiController {
    private final MatchReferenceService matchReferenceService;

    public static final String NEXT_PAGE_REL = "next-page";

    @GetMapping(value = "/api/matches/{summonerName}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity getMatchPage(@PathVariable String summonerName,
                                       @RequestParam(value = "ts", defaultValue = MatchReferenceService.NOT_INIT_GAMECREATION + "") Long gameCreation) {

        //Collection Entity의 경우 다음과 같이 변환함.
        List<MatchDto> matchPage = matchReferenceService.getMatchPageBySummonerName(summonerName, gameCreation);
        if(matchPage.isEmpty())
            return ResponseEntity.notFound().build();
        Long lastGameCreation = matchPage.get(matchPage.size() - 1).getGameCreation();

        //1. List<MatchDto>를 List<EntityModel>로 변환.
        //*EntityModel 원소 하나 하나에 link 삽입하려면 EntityModel 만들 때 link 파라미터로 넣어주면 된다.
        List<EntityModel> entityModels = matchPage.stream()
                .map(matchDto -> new EntityModel<>(matchDto)).collect(Collectors.toList());

        //2. List<EntityModel>을 CollectionModel로 변환.
        //아래 예제에서는 전체 CollectionModel 하나에 대응되는 link만 있으면 되므로 link 삽입함.
        CollectionModel collectionModel = new CollectionModel<>(entityModels, linkTo(methodOn(MatchApiController.class).getMatchPage(summonerName, lastGameCreation))
                .withRel(NEXT_PAGE_REL));

        return ResponseEntity.ok(collectionModel);
    }
}