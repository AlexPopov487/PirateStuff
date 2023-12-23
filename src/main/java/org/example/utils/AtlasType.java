package org.example.utils;

public enum AtlasType {
    ATLAS_PLAYER("player_sprites.png"),
    ATLAS_LEVEL_BLOCKS("outside_sprites.png"),
    ATLAS_LEVEL_ONE("level_one_data.png"),
    ATLAS_MENU_BUTTONS("button_atlas.png"),
    ATLAS_MENU_BACKGROUND("menu_background.png"),
    ATLAS_PAUSE_SCREEN_BACKGROUND("pause_menu.png"),
    ATLAS_SOUND_BUTTONS("sound_button.png"),
    ATLAS_URM_BUTTONS("urm_buttons.png"),
    ATLAS_VOLUME_BUTTONS("volume_buttons.png");

    private final String atlasFileName;

    AtlasType(String atlasFileName) {
        this.atlasFileName = atlasFileName;
    }

    public String getAtlasFileName() {
        return atlasFileName;
    }
}
