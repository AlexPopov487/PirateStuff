package ru.alexp.audio;

public class AudioControls {
    private float volume;
    private boolean isSongMuted;
    private boolean isEffectMuted;

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public boolean isSongMuted() {
        return isSongMuted;
    }

    public void setSongMuted(boolean songMuted) {
        isSongMuted = songMuted;
    }

    public boolean isEffectMuted() {
        return isEffectMuted;
    }

    public void setEffectMuted(boolean effectMuted) {
        isEffectMuted = effectMuted;
    }
}
