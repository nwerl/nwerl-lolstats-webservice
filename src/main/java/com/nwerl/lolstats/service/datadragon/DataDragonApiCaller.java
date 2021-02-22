package com.nwerl.lolstats.service.datadragon;

import java.util.List;
import java.util.Map;


public interface DataDragonApiCaller {
    String callApiCurrentLOLVersion();

    byte[] callImgApi(String assetName, String imgName);
    Map<Long, String> callChampionListApi(String jsonName);
    List<String> callItemListApi(String jsonName);
    Map<Long, String> callRuneStyleListApi(String jsonName);
    Map<Long, String> callRuneListApi(String jsonName);
    Map<Long, String> callSpellListApi(String jsonName);
}