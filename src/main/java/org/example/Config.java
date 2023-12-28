package org.example;


import static org.example.Game.SCALE;

public class Config {
    public static final int BUTTON_WIDTH_DEFAULT = 140;
    public static final int BUTTON_HEIGHT_DEFAULT = 56;
    public static final int BUTTON_HEIGHT = (int) (BUTTON_HEIGHT_DEFAULT * SCALE);
    public static final int BUTTON_WIDTH = (int) (BUTTON_WIDTH_DEFAULT * SCALE);


    public static final int SOUND_BUTTON_WIDTH_DEFAULT = 42;
    public static final int SOUND_BUTTON_HEIGHT_DEFAULT = 42;
    public static final int SOUND_BUTTON_WIDTH = (int) (SOUND_BUTTON_WIDTH_DEFAULT * SCALE);
    public static final int SOUND_BUTTON_HEIGHT = (int) (SOUND_BUTTON_HEIGHT_DEFAULT * SCALE);

    public static final int URM_BUTTON_WIDTH_DEFAULT = 56;
    public static final int URM_BUTTON_HEIGHT_DEFAULT = 56;
    public static final int URM_BUTTON_WIDTH = (int) (URM_BUTTON_WIDTH_DEFAULT * SCALE);
    public static final int URM_BUTTON_HEIGHT = (int) (URM_BUTTON_HEIGHT_DEFAULT * SCALE);

    public static final int VOLUME_BUTTON_WIDTH_DEFAULT = 28;
    public static final int VOLUME_BUTTON_HEIGHT_DEFAULT = 44;
    public static final int VOLUME_SLIDER_WIDTH_DEFAULT = 215;
    public static final int VOLUME_SLIDER_HEIGHT_DEFAULT = 44;
    public static final int VOLUME_BUTTON_WIDTH = (int) (VOLUME_BUTTON_WIDTH_DEFAULT * SCALE);
    public static final int VOLUME_BUTTON_HEIGHT = (int) (VOLUME_BUTTON_HEIGHT_DEFAULT * SCALE);
    public static final int VOLUME_SLIDER_WIDTH = (int) (VOLUME_SLIDER_WIDTH_DEFAULT * SCALE);
    public static final int VOLUME_SLIDER_HEIGHT = (int) (VOLUME_SLIDER_HEIGHT_DEFAULT * SCALE);

    public static class LevelEnv {
        public static final int BIG_CLOUD_WIDTH_DEFAULT = 448;
        public static final int BIG_CLOUD_HEIGHT_DEFAULT = 101;
        public static final int BIG_CLOUD_WIDTH = (int) (BIG_CLOUD_WIDTH_DEFAULT * SCALE);
        public static final int BIG_CLOUD_HEIGHT = (int) (BIG_CLOUD_HEIGHT_DEFAULT * SCALE);

        public static final int SMALL_CLOUD_WIDTH_DEFAULT = 74;
        public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 24;
        public static final int SMALL_CLOUD_WIDTH = (int) (SMALL_CLOUD_WIDTH_DEFAULT * SCALE);
        public static final int SMALL_CLOUD_HEIGHT = (int) (SMALL_CLOUD_HEIGHT_DEFAULT * SCALE);
    }

}
