package com.nwerl.lolstats.service.dDragon;

import com.fasterxml.jackson.databind.JsonNode;


public interface DDragonApiCaller {
    String callApiCurrentLOLVersion();
    JsonNode callListApi(String jsonName);
    byte[] callImgApi(String assetName, String imgName);
}