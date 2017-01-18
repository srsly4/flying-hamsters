package engine.sound;

import static org.lwjgl.openal.AL10.*;

/**
 * Created by Sirius on 27.12.2016.
 */
public class SoundSource {

    SoundBuffer buffer;
    int sourceId;
    public SoundSource(){
        if (SoundManager.getInstance().isDisabled()) return;
        sourceId = alGenSources();
        alSource3f(sourceId, AL_POSITION, 0f, 0f, 0f);
        alSource3f(sourceId, AL_VELOCITY, 0f, 0f, 0f);
        setPitch(1f);
        setGain(1f);
        setLooping(false);
    }

    public SoundSource(SoundBuffer buffer){
        this();
        setBufferSource(buffer);
    }

    public void setPitch(float pitch){
        if (SoundManager.getInstance().isDisabled()) return;
        alSourcef(sourceId, AL_PITCH, pitch);
    }

    public void setGain(float gain){
        if (SoundManager.getInstance().isDisabled()) return;
        alSourcef(sourceId, AL_GAIN, gain);
    }

    public void setLooping(boolean loop){
        if (SoundManager.getInstance().isDisabled()) return;
        alSourcei(sourceId, AL_LOOPING, loop ? AL_TRUE : AL_FALSE);
    }

    public void setBufferSource(SoundBuffer buffer)
    {
        if (SoundManager.getInstance().isDisabled()) return;
        this.buffer = buffer;
        this.stop();
        alSourcei(sourceId, AL_BUFFER, buffer.getBufferId());
    }

    public void play(){
        if (SoundManager.getInstance().isDisabled()) return;
        alSourcePlay(sourceId);
    }

    public void stop(){
        if (SoundManager.getInstance().isDisabled()) return;
        alSourceStop(sourceId);
    }
    public void pause(){
        if (SoundManager.getInstance().isDisabled()) return;
        alSourcePause(sourceId);
    }

    public void cleanUp(){
        if (SoundManager.getInstance().isDisabled()) return;
        alDeleteSources(sourceId);
    }

}
