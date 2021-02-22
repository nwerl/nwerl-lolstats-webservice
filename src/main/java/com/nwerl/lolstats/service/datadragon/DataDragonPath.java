package com.nwerl.lolstats.service.datadragon;

import lombok.Getter;

@Getter
public enum DataDragonPath {
    CHAMPION("champion", "champions","/champion"),
    ITEM("item", "items", "/item"),
    SPELL("summoner", "spells", "/spell"),
    RUNE_STYLE("runesReforged", "runeStyles", "/perk-images/Styles"),
    RUNE("runesReforged", "runes", "")
    ;

    private String jsonName;
    private String folderName;
    private String apiPath;

    DataDragonPath(final String jsonName, final String folderName, final String apiPath) {
        this.jsonName = jsonName;
        this.folderName = folderName;
        this.apiPath = apiPath;
    }
}
