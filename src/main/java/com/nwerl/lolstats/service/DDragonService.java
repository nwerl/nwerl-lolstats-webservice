package com.nwerl.lolstats.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DDragonService {
    private final DDragonApiCaller dDragonApiCaller;
    private String basePath;
    private ObjectMapper mapper;

    public DDragonService (DDragonApiCaller dDragonApiCaller) {
        this.mapper = new ObjectMapper();
        this.dDragonApiCaller = dDragonApiCaller;
        basePath = "/home/nwerl/IdeaProjects/nwerl-lolstats-webservice/src/main/resources/static/images";
    }

    //@EventListener(ApplicationReadyEvent.class)
    public void updateChampions() throws IOException {
        log.info("Update Champions started");

        String assetName = "champion";
        String folderName = "champions";
        Map<String, JsonNode> champions = mapper.convertValue(dDragonApiCaller.callListApi(assetName).get("data"),
                                                            new TypeReference<Map<String, JsonNode>>(){});

        for(Map.Entry<String, JsonNode> champion : champions.entrySet()) {
            String championId = champion.getValue().get("key").asText();

            Path path = Paths.get(basePath+"/"+folderName+"/"+championId+".png");
            if(Files.exists(path))  continue;

            byte[] imageBytes = dDragonApiCaller.callImgApi(assetName, champion.getKey());
            Files.write(path, imageBytes);
        }
        log.info("Update Champions finished");
    }

    public void updateItems() throws IOException {
        log.info("Update Items started");

        String assetName = "item";
        String folderName = "items";
        List<String> items = mapper.convertValue(dDragonApiCaller.callListApi(assetName).get("data").fieldNames(),
                new TypeReference<List<String>>(){});

        for(String itemId : items) {
            Path path = Paths.get(basePath+"/"+folderName+"/"+itemId+".png");
            if(Files.exists(path))  continue;

            byte[] imgBytes = dDragonApiCaller.callImgApi(assetName, itemId);
            Files.write(path, imgBytes);
        }
        log.info("Update Items finished");
    }

    public void updateSpells() throws IOException {
        log.info("Update Spells started");

        String jsonName = "summoner", assetName = "spell";
        String folderName = "spells";

        Map<String, String> spells = new HashMap<>();

        JsonNode jsonNode = dDragonApiCaller.callListApi(jsonName).get("data");
        Iterator<String> it = jsonNode.fieldNames();

        while(it.hasNext()){
            String spellName = it.next();
            String spellId = jsonNode.get(spellName).get("key").asText();
            spells.put(spellId, spellName);
        }

        for(Map.Entry<String, String> spell : spells.entrySet()) {
            Path path = Paths.get(basePath+"/"+folderName+"/"+spell.getKey()+".png");
            if(Files.exists(path))  continue;

            byte[] imgBytes = dDragonApiCaller.callImgApi(assetName, spell.getValue());
            Files.write(path, imgBytes);
        }

        log.info("Update Spells finished");
    }

    //todo : match 컬렉션에 perk 잘못 들어감, perk 이미지는 URI 경로가 약간 다름.... 스킵할지 말지 고민하자.
    public void updateRunes() throws IOException {
        log.info("Update Items started");

        String jsonName = "runesReforged";
        String assetName = "perk-images/Styles";
        String folderName1 = "runeStyles", folderName2 = "runes";
        List<JsonNode> mappedList = mapper.convertValue(dDragonApiCaller.callListApi(jsonName),
                new TypeReference<List<JsonNode>>(){});

        List<String> runeStyles = new ArrayList<>();
        for(JsonNode node : mappedList) {
            String icon = node.get("icon").asText();
            runeStyles.add(icon.substring(icon.lastIndexOf("/")+1, icon.lastIndexOf(".")));
        }

        for(String runeStyle : runeStyles)
            log.info("{}", runeStyle);

        for(String runeStyle : runeStyles) {
            Path path = Paths.get(basePath+"/"+folderName1+"/"+runeStyle.substring(0, runeStyle.indexOf("_"))+".png");
            if(Files.exists(path))  continue;

            byte[] imgBytes = dDragonApiCaller.callImgApi(assetName, runeStyle);
            Files.write(path, imgBytes);
        }

//        Map<String, String> runes = new HashMap<>();
//        for(JsonNode node : mappedList) {
//            List<JsonNode> slotsList = node.findValues("slots");
//            log.info("{}",slotsList.size());
//            for(JsonNode slot : slotsList) {
//                runes.putAll(slot.findValues("runes").stream()
//                        .collect(Collectors.toMap(n -> n.get("id").asText(), n -> n.get("icon").asText())));
//            }
//        }
//
//        for(Map.Entry<String, String> rune : runes.entrySet()) {
//            Path path = Paths.get(basePath+"/"+folderName2+"/"+rune.getKey()+".png");
//            if(Files.exists(path))  continue;
//
//            String icon = rune.getValue();
//            byte[] imgBytes = dDragonApiCaller.callImgApi(icon.substring(0, icon.lastIndexOf("/")), icon.substring(icon.lastIndexOf("/")+1));
//            Files.write(path, imgBytes);
//        }


        log.info("Update Items finished");
    }
}