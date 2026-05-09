package fr.github.tcgame.model.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class AudioSettings {
    private float globalVolume = 0.5f;
    private float sfxVolume = 0.5f;
    private float musicVolume = 0.5f;

    public Music musicMenu;
    public Music musicGame;
    public Music actualMusic;

    public AudioSettings(){
        musicMenu=Gdx.audio.newMusic(Gdx.files.internal("musics/ph_music.mp3"));
        musicGame=Gdx.audio.newMusic(Gdx.files.internal("musics/ph_music.mp3"));
    }

    public enum TypeMusic{MENU,GAME}

    public float getGlobalVolume() { return globalVolume; }
    public void setGlobalVolume(float volume) {
        this.globalVolume = Math.max(0.0f, Math.min(1.0f, volume));
        this.updateSound();
    }

    public float getSfxVolume() { return sfxVolume; }
    public void setSfxVolume(float volume) {
        this.sfxVolume = Math.max(0.0f, Math.min(1.0f, volume));
        this.updateSound();
    }

    public float getMusicVolume() { return musicVolume; }
    public void setMusicVolume(float volume) {
        this.musicVolume = Math.max(0.0f, Math.min(1.0f, volume));
        this.updateSound();
    }

    public float getEffectiveSfxVolume() { return sfxVolume*globalVolume;}
    public float getEffectiveMusicVolume() { return musicVolume*globalVolume;}


    public void playMusic(TypeMusic typeMusic) {

        if (actualMusic!=null){
            actualMusic.stop();
        }

        switch (typeMusic){
            case MENU -> actualMusic=musicMenu;
            case GAME -> actualMusic=musicGame;
        }

        actualMusic.setLooping(true);
        this.updateMusicVolume();
        actualMusic.play();
    }

    public void updateSound(){
        updateMusicVolume();
    }

    public void updateMusicVolume(){
        if (actualMusic != null) {
            actualMusic.setVolume(getEffectiveMusicVolume());
        }
    }

    public void initMusic(){
        playMusic(TypeMusic.MENU);
    }
}
