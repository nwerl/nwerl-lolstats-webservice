package com.nwerl.lolstats.service.datadragon;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class DataDragonService {
    private final DataDragonApiCaller dataDragonApiCaller;
    private final String basePath;
    private ObjectMapper mapper;

    @Autowired
    public DataDragonService(DataDragonApiCaller dataDragonApiCaller) {
        this.mapper = new ObjectMapper();
        this.dataDragonApiCaller = dataDragonApiCaller;
        basePath = "/home/nwerl/IdeaProjects/nwerl-lolstats-webservice/src/main/resources/static/images";
    }

    //@EventListener(ApplicationReadyEvent.class)
    public void updateChampions() throws IOException {
        log.info("Update Champions started");

        String jsonName = "champion";
        String folderName = "champions";
        String apiPath = "/champion";

        Map<String, JsonNode> champions = mapper.convertValue(dataDragonApiCaller.callListApi(jsonName).get("data"),
                                                            new TypeReference<Map<String, JsonNode>>(){});

        for(Map.Entry<String, JsonNode> champion : champions.entrySet()) {
            String championId = champion.getValue().get("key").asText();

            Path filePath = Paths.get(basePath+"/"+folderName+"/"+championId+".png");
            if(Files.exists(filePath))  continue;

            byte[] imageBytes = dataDragonApiCaller.callImgApi(apiPath, champion.getKey());
            Files.write(filePath, imageBytes);
        }

        log.info("Update Champions finished");
    }

    public void updateItems() throws IOException {
        log.info("Update Items started");

        String jsonName = "item";
        String folderName = "items";
        String apiPath = "/item";

        List<String> items = mapper.convertValue(dataDragonApiCaller.callListApi(jsonName).get("data").fieldNames(),
                new TypeReference<List<String>>(){});

        for(String itemId : items) {
            Path filePath = Paths.get(basePath+"/"+folderName+"/"+itemId+".png");
            if(Files.exists(filePath))  continue;

            byte[] imgBytes = dataDragonApiCaller.callImgApi(apiPath, itemId);
            Files.write(filePath, imgBytes);
        }

        log.info("Update Items finished");
    }

    public void updateSpells() throws IOException {
        log.info("Update Spells started");

        String jsonName = "summoner";
        String folderName = "spells";
        String apiPath = "/spell";

        Map<String, String> spells = new HashMap<>();

        JsonNode jsonNode = dataDragonApiCaller.callListApi(jsonName).get("data");
        Iterator<String> it = jsonNode.fieldNames();

        while(it.hasNext()){
            String spellName = it.next();
            String spellId = jsonNode.get(spellName).get("key").asText();
            spells.put(spellId, spellName);
        }

        for(Map.Entry<String, String> spell : spells.entrySet()) {
            Path filePath = Paths.get(basePath+"/"+folderName+"/"+spell.getKey()+".png");
            if(Files.exists(filePath))  continue;

            byte[] imgBytes = dataDragonApiCaller.callImgApi(apiPath, spell.getValue());
            Files.write(filePath, imgBytes);
        }

        log.info("Update Spells finished");
    }

    public void updateRuneStyles() throws IOException {
        log.info("Update RuneStyles started");

        String jsonName = "runesReforged";
        String apiPath = "/perk-images/Styles";
        String folderName = "runeStyles";
        List<JsonNode> mappedList = mapper.convertValue(dataDragonApiCaller.callListApi(jsonName),
                new TypeReference<List<JsonNode>>(){});

        Map<String, String> runeStyles = new HashMap<>();
        for(JsonNode node : mappedList) {
            String runeId = node.get("id").asText();
            String icon = node.get("icon").asText();
            runeStyles.put(runeId, icon.substring(icon.lastIndexOf("/")+1, icon.lastIndexOf(".")));
        }

        for(Map.Entry<String, String> runeStyle : runeStyles.entrySet()) {
            Path filePath = Paths.get(basePath+"/"+folderName+"/"+runeStyle.getKey()+".png");
            if(Files.exists(filePath))  continue;

            byte[] imgBytes = dataDragonApiCaller.callImgApi(apiPath, runeStyle.getValue());
            Files.write(filePath, imgBytes);
        }

        log.info("Update Runes finished");
    }

    public void updateRunes() throws IOException {
        log.info("Update Runes started");

        String jsonName = "runesReforged";
        String folderName = "runes";
        List<JsonNode> mappedList = mapper.convertValue(dataDragonApiCaller.callListApi(jsonName),
                new TypeReference<List<JsonNode>>(){});

        Map<String, String> runes = new HashMap<>();
        for(JsonNode node : mappedList) {
            List<JsonNode> slotsList = StreamSupport.stream(node.get("slots").spliterator(), false)
                    .collect(Collectors.toList());
            for(JsonNode slot : slotsList) {
                List<JsonNode> runesList = StreamSupport.stream(slot.get("runes").spliterator(), false)
                        .collect(Collectors.toList());

                runes.putAll(runesList.stream()
                        .collect(Collectors.toMap(n -> n.get("id").asText(), n -> n.get("icon").asText())));
            }
        }

        for(Map.Entry<String, String> rune : runes.entrySet()) {
            Path path = Paths.get(basePath+"/"+folderName+"/"+rune.getKey()+".png");
            if(Files.exists(path))  continue;


            //imgPath e.g., perk-images/Styles/Domination/Electrocute/Electrocute.png
            String imgPath = rune.getValue();
            String apiPath = "/" + imgPath.substring(0, imgPath.lastIndexOf("/")+1);
            String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1, imgPath.lastIndexOf("."));

            byte[] imgBytes = dataDragonApiCaller.callImgApi(apiPath, imgName);
            Files.write(path, imgBytes);
        }


        log.info("Update Runes finished");
    }
}