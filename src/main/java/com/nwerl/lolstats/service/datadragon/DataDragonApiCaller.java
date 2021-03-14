package com.nwerl.lolstats.service.datadragon;

import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Map;

@Retryable(value = HttpServerErrorException.class, maxAttempts = 3)
public interface DataDragonApiCaller {
    public static final String LOL_VERSION_URI = "/api/versions.json";
    public static final String IMAGE_LIST_URI = "/cdn/%s/data/ko_KR/%s.json";
    public static final String IMAGE_URI = "/cdn%s/img%s/%s.png";

    String callApiCurrentLOLVersion();

    byte[] callImgApi(String assetName, String imgName);
    Map<Long, String> callChampionListApi(String jsonName);
    List<String> callItemListApi(String jsonName);
    Map<Long, String> callRuneStyleListApi(String jsonName);
    Map<Long, String> callRuneListApi(String jsonName);
    Map<Long, String> callSpellListApi(String jsonName);
}