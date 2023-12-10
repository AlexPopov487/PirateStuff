package org.example.utils;

public enum AtlasType {
    ATLAS_PLAYER("player_sprites.png"),
    ATLAS_LEVEL_BLOCKS("outside_sprites.png"),
    ATLAS_LEVEL_ONE("level_one_data.png");

    private final String atlasFileName;

    AtlasType(String atlasFileName) {
        this.atlasFileName = atlasFileName;
    }

    public String getAtlasFileName() {
        return atlasFileName;
    }
}
