package com.nwerl.lolstats.service.datadragon;


import com.nwerl.lolstats.web.dto.riotapi.datadragon.ChampionDto;
import com.nwerl.lolstats.web.dto.riotapi.datadragon.ItemDto;
import com.nwerl.lolstats.web.dto.riotapi.datadragon.RuneDto;
import com.nwerl.lolstats.web.dto.riotapi.datadragon.SpellDto;

import java.util.List;

public interface DataDragonApiCaller {
    public static final String LOL_VERSION_URI = "/api/versions.json";
    public static final String IMAGE_LIST_URI = "/cdn/%s/data/ko_KR/%s.json";
    public static final String IMAGE_URI = "/cdn%s/img%s/%s.png";

    String callApiCurrentLOLVersion();

    byte[] callImgApi(String assetName, String imgName);
    List<ChampionDto> callChampionListApi(String jsonName);
    List<ItemDto> callItemListApi(String jsonName);
    List<RuneDto> callRuneStyleListApi(String jsonName);
    List<RuneDto> callRuneListApi(String jsonName);
    List<SpellDto> callSpellListApi(String jsonName);
}