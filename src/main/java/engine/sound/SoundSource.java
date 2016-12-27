package engine.sound;

import static org.lwjgl.openal.AL10.*;

/**
 * Created by Sirius on 27.12.2016.
 */
public class SoundSource {

    SoundBuffer buffer;
    int sourceId;
    public SoundSource(){
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
        alSourcef(sourceId, AL_PITCH, pitch);
    }

    public void setGain(float gain){
        alSourcef(sourceId, AL_GAIN, gain);
    }

    public void setLooping(boolean loop){
        alSourcei(sourceId, AL_LOOPING, loop ? AL_TRUE : AL_FALSE);
    }

    public void setBufferSource(SoundBuffer buffer)
    {
        this.buffer = buffer;
        this.stop();
        alSourcei(sourceId, AL_BUFFER, buffer.getBufferId());
    }

    public void play(){
        alSourcePlay(sourceId);
    }

    public void stop(){
        alSourceStop(sourceId);
    }
    public void pause(){
        alSourcePause(sourceId);
    }

    public void cleanUp(){
        alDeleteSources(sourceId);
    }

}
