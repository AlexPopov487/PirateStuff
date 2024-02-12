package org.example.types;

public enum AtlasType {
    ATLAS_PLAYER("player_sprites.png"),
    ATLAS_LEVEL_BLOCKS("outside_sprites.png"),
//    ATLAS_LEVEL_ONE("level_one_data.png"),
    ATLAS_MENU_BUTTONS("button_atlas.png"),
    ATLAS_MENU_TEMPLATE("menu_template.png"),
    ATLAS_MENU_BACKGROUND("background_menu.png"),
    ATLAS_PAUSE_SCREEN_BACKGROUND("pause_menu.png"),
    ATLAS_SOUND_BUTTONS("sound_button.png"),
    ATLAS_URM_BUTTONS("urm_buttons.png"),
    ATLAS_PLAYING_BACKGROUND("playing_bg_img.png"),
    ATLAS_PLAYING_BACKGROUND_CLOUD_BIG("big_clouds.png"),
    ATLAS_PLAYING_BACKGROUND_CLOUD_SMALL("small_clouds.png"),
    ATLAS_ENEMY_CRAB("crabby_sprite.png"),
    ATLAS_STATUS_BAR("health_power_bar.png"),
    ATLAS_LEVEL_COMPLETED("completed_sprite.png"),
    ATLAS_POTION("potions_sprites.png"),
    ATLAS_CONTAINER("objects_sprites.png"),
    ATLAS_SPIKE_TRAP("trap_atlas.png"),
    ATLAS_CANNON("cannon_atlas.png"),
    ATLAS_PROJECTILE("ball.png"),
    ATLAS_DEATH_SCREEN("death_screen.png"),
    ATLAS_BACKGROUND_GAME_OVER("background_game_over.png"),
    ATLAS_BACKGROUND_SETTINGS_OVERLAY("options_background.png"),
    ATLAS_WATER("water.png"),
    ATLAS_WATER_TOP("water_atlas_animation.png"),
    ATLAS_GRASS("grass_atlas.png"),
    ATLAS_TREE_STRAIGHT("tree_one_atlas.png"),
    ATLAS_TREE_BEND("tree_two_atlas.png"),
    ATLAS_BACK_TREE_STRAIGHT("back_tree_one_atlas.png"),
    ATLAS_SHARK("shark.png"),
    ATLAS_PINK_STAR("pinkstar_atlas.png"),
    ATLAS_QUESTION_MARK("question_atlas.png"),
    ATLAS_FLAG("flag.png"),
    ATLAS_KEY("key.png"),
    ATLAS_CHEST("chest.png"),
    ATLAS_EXPLOSION("explosion.png"),
    ATLAS_SHIP("ship.png"),
    ATLAS_MESSAGE("messages.png"),
    ATLAS_VOLUME_BUTTONS("volume_buttons.png");

    private final String atlasFileName;

    AtlasType(String atlasFileName) {
        this.atlasFileName = atlasFileName;
    }

    public String getAtlasFileName() {
        return atlasFileName;
    }
}
