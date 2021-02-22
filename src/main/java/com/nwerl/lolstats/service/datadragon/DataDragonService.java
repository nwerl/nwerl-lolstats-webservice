package com.nwerl.lolstats.service.datadragon;

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
        Map<Long, String> champions = dataDragonApiCaller.callChampionListApi(paths.getJsonName());

        for(Map.Entry<Long, String> champion : champions.entrySet()) {
            Path filePath = Paths.get(basePath+"/"+paths.getFolderName()+"/"+champion.getKey()+".png");
            if(Files.exists(filePath))  continue;

            byte[] imageBytes = dataDragonApiCaller.callImgApi(paths.getApiPath(), champion.getValue());
            Files.write(filePath, imageBytes);
        }

        log.info("Update Champions finished");
    }

    public void updateItems() throws IOException {
        log.info("Update Items started");

        DataDragonPath paths = DataDragonPath.ITEM;

        List<String> items = dataDragonApiCaller.callItemListApi(paths.getJsonName());

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

        Map<Long, String> spells = dataDragonApiCaller.callSpellListApi(paths.getJsonName());

        for(Map.Entry<Long, String> spell : spells.entrySet()) {
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

        Map<Long, String> runeStyles = dataDragonApiCaller.callRuneStyleListApi(paths.getJsonName());

        for(Map.Entry<Long, String> runeStyle : runeStyles.entrySet()) {
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

        Map<Long, String> runes = dataDragonApiCaller.callRuneListApi(paths.getJsonName());

        for(Map.Entry<Long, String> rune : runes.entrySet()) {
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