package com.nwerl.lolstats.service.datadragon;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    public DataDragonService(DataDragonApiCaller dataDragonApiCaller,
                             @Value("${image.files.basepath}") String basePath) {
        this.mapper = new ObjectMapper();
        this.dataDragonApiCaller = dataDragonApiCaller;
        this.basePath = basePath;
    }

    //@EventListener(ApplicationReadyEvent.class)
    public void updateChampions() throws IOException {
        log.info("Update Champions started");

        DataDragonPath paths = DataDragonPath.CHAMPION;
        Map<String, JsonNode> champions = mapper.convertValue(dataDragonApiCaller.callListApi(paths.getJsonName()).get("data"),
                                                            new TypeReference<Map<String, JsonNode>>(){});

        for(Map.Entry<String, JsonNode> champion : champions.entrySet()) {
            String championId = champion.getValue().get("key").asText();

            Path filePath = Paths.get(basePath+"/"+paths.getFolderName()+"/"+championId+".png");
            if(Files.exists(filePath))  continue;

            byte[] imageBytes = dataDragonApiCaller.callImgApi(paths.getApiPath(), champion.getKey());
            Files.write(filePath, imageBytes);
        }

        log.info("Update Champions finished");
    }

    public void updateItems() throws IOException {
        log.info("Update Items started");

        DataDragonPath paths = DataDragonPath.ITEM;

        List<String> items = mapper.convertValue(dataDragonApiCaller.callListApi(paths.getJsonName()).get("data").fieldNames(),
                new TypeReference<List<String>>(){});

        for(String itemId : items) {
            Path filePath = Paths.get(basePath+"/"+paths.getFolderName()+"/"+itemId+".png");
            if(Files.exists(filePath))  continue;

            byte[] imgBytes = dataDragonApiCaller.callImgApi(paths.getApiPath(), itemId);
            Files.write(filePath, imgBytes);
        }

        log.info("Update Items finished");
    }

    public void updateSpells() throws IOException {
        log.info("Update Spells started");

        DataDragonPath paths = DataDragonPath.SPELL;

        Map<String, String> spells = new HashMap<>();

        JsonNode jsonNode = dataDragonApiCaller.callListApi(paths.getJsonName()).get("data");
        Iterator<String> it = jsonNode.fieldNames();

        while(it.hasNext()){
            String spellName = it.next();
            String spellId = jsonNode.get(spellName).get("key").asText();
            spells.put(spellId, spellName);
        }

        for(Map.Entry<String, String> spell : spells.entrySet()) {
            Path filePath = Paths.get(basePath+"/"+paths.getFolderName()+"/"+spell.getKey()+".png");
            if(Files.exists(filePath))  continue;

            byte[] imgBytes = dataDragonApiCaller.callImgApi(paths.getApiPath(), spell.getValue());
            Files.write(filePath, imgBytes);
        }

        log.info("Update Spells finished");
    }

    public void updateRuneStyles() throws IOException {
        log.info("Update RuneStyles started");

        DataDragonPath paths = DataDragonPath.RUNE_STYLE;

        List<JsonNode> mappedList = mapper.convertValue(dataDragonApiCaller.callListApi(paths.getJsonName()),
                new TypeReference<List<JsonNode>>(){});

        Map<String, String> runeStyles = new HashMap<>();
        for(JsonNode node : mappedList) {
            String runeId = node.get("id").asText();
            String icon = node.get("icon").asText();
            runeStyles.put(runeId, icon.substring(icon.lastIndexOf("/")+1, icon.lastIndexOf(".")));
        }

        for(Map.Entry<String, String> runeStyle : runeStyles.entrySet()) {
            Path filePath = Paths.get(basePath+"/"+paths.getFolderName()+"/"+runeStyle.getKey()+".png");
            if(Files.exists(filePath))  continue;

            byte[] imgBytes = dataDragonApiCaller.callImgApi(paths.getApiPath(), runeStyle.getValue());
            Files.write(filePath, imgBytes);
        }

        log.info("Update Runes finished");
    }

    public void updateRunes() throws IOException {
        log.info("Update Runes started");

        DataDragonPath paths = DataDragonPath.RUNE;
        List<JsonNode> mappedList = mapper.convertValue(dataDragonApiCaller.callListApi(paths.getJsonName()),
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
            Path path = Paths.get(basePath+"/"+paths.getFolderName()+"/"+rune.getKey()+".png");
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