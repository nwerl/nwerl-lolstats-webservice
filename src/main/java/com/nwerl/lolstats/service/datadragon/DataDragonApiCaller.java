package com.nwerl.lolstats.service.datadragon;

import com.fasterxml.jackson.databind.JsonNode;


public interface DataDragonApiCaller {
    String callApiCurrentLOLVersion();
    JsonNode callListApi(String jsonName);
    byte[] callImgApi(String assetName, String imgName);
}