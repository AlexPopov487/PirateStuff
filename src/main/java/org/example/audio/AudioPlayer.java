package org.example.audio;

import org.example.Config;
import org.example.utils.ResourceLoader;

import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.util.Random;

public class AudioPlayer {
    private final Clip[] songs;
    private final Clip[] effects;
    private int currentSongId;
    private final Random random = new Random();
    private final AudioControls audioControls;

    public AudioPlayer() {
        this.audioControls = new AudioControls();
        songs = ResourceLoader.getSongs();
        effects = ResourceLoader.getEffects();
        updateSongVolume();
        updateEffectsVolume();
        playSong(Config.Audio.MENU_SONG_INDEX);
    }

    public AudioControls getAudioControls() {
        return audioControls;
    }

    public void toggleSongMute() {
        audioControls.setSongMuted(!audioControls.isSongMuted());
        for (Clip song : songs) {
            setClipMuted(song);
        }
    }

    public int getAttackEffectRandomIndex() {
       return Config.Audio.ATTACK_1_EFFECT_INDEX + random.nextInt(3);
    }

    // todo the volume sets exponentially
    public void setVolume(float volume) {
        audioControls.setVolume(volume);
        updateSongVolume();
        updateEffectsVolume();
    }

    public void playLevelSong(int levelIndex) {
        if (levelIndex % 2 == 0) {
            playSong(Config.Audio.LEVEL_1_SONG_INDEX);
        } else
            playSong(Config.Audio.LEVEL_2_SONG_INDEX);
    }

    public void stopPlaying() {
        if (songs[currentSongId].isActive()) {
            songs[currentSongId].stop();
        }

        for (Clip effect : effects) {
            if (effect.isActive()) {
                effect.stop();
            }
        }
    }

    public void playLvlCompletedEffect() {
        stopPlaying();
        playEffect(Config.Audio.LEVEL_COMPLETED_EFFECT_INDEX);
    }

    public void playEffect(int effectIndex) {
        // set clip at start position
        effects[effectIndex].setMicrosecondPosition(0);
        effects[effectIndex].start();
    }

    public void playSong(int songIndex) {
        stopPlaying();

        currentSongId = songIndex;
        updateSongVolume();
        songs[currentSongId].setMicrosecondPosition(0);
        songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);
    }


    public void toggleEffectMute() {
        audioControls.setSongMuted(!audioControls.isSongMuted());
        for (Clip effect : effects) {
            setClipMuted(effect);
        }
        // For user to check that the effect is unmuted, the random effect  is played once
        if (!audioControls.isSongMuted()) {
            playEffect(Config.Audio.ATTACK_1_EFFECT_INDEX);
        }
    }

    private void updateSongVolume() {
        setClipVolume(songs[currentSongId]);
    }

    private void updateEffectsVolume() {
        for (Clip effect : effects) {
            setClipVolume(effect);
        }
    }

    // todo just changing volume becomes really cumbersome, opt for external audio libraries instead
    private void setClipVolume(Clip clip) {
        FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float range = control.getMaximum() - control.getMinimum();
        float updatedGain = (range * audioControls.getVolume()) + control.getMinimum();
        control.setValue(updatedGain);
    }

    private void setClipMuted(Clip clip) {
        BooleanControl control = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
        control.setValue(audioControls.isSongMuted());
    }
}
