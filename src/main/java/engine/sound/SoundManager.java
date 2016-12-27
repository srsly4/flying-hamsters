package engine.sound;

import engine.EngineException;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.EXTEfx.ALC_MAX_AUXILIARY_SENDS;

/**
 * Created by Sirius on 27.12.2016.
 */
public class SoundManager {

    private long device;
    private HashMap<String, SoundBuffer> sounds;
    private HashMap<String, SoundSource> sources;

    private SoundManager() throws ALException{
        device = alcOpenDevice((ByteBuffer)null); //load default sound device
        ALCCapabilities alcCaps = ALC.createCapabilities(device);

        //alc attributes
        IntBuffer contextAttribList = BufferUtils.createIntBuffer(16);
        contextAttribList.put(ALC_REFRESH);
        contextAttribList.put(60);
        contextAttribList.put(ALC_SYNC);
        contextAttribList.put(ALC_FALSE);
        contextAttribList.put(ALC_MAX_AUXILIARY_SENDS);
        contextAttribList.put(2);
        contextAttribList.put(0);
        contextAttribList.flip();

        long context = alcCreateContext(device ,contextAttribList);
        if(!alcMakeContextCurrent(context)) {
            throw new ALException("Failed to make ALC context current", "alcCreateContext");
        }
        ALCapabilities alCaps = AL.createCapabilities(alcCaps);

        alListener3f(AL_VELOCITY, 0f, 0f, 0f);
        alListener3f(AL_ORIENTATION, 0f, 0f, -1f);

        sounds = new HashMap<>();
        sources = new HashMap<>();
    }

    public SoundSource createSoundSource(String name){
        SoundSource source = new SoundSource();
        sources.put(name, source);
        return source;
    }

    public void loadSound(String name, String resourceName) throws EngineException{
        SoundBuffer buff = new SoundBuffer(resourceName);
        sounds.put(name, buff);
    }

    public SoundSource loadSoundToSource(String sound, String source){
        SoundSource src = sources.get(source);
        src.setBufferSource(sounds.get(sound));
        return src;
    }

    public SoundSource getSoundSource(String name){
        return sources.get(name);
    }

    public SoundBuffer getSound(String name){
        return sounds.get(name);
    }

    public void cleanUp(){
        for (String sourceKey : sources.keySet())
            sources.get(sourceKey).cleanUp();
        for (String soundKey : sounds.keySet())
            sounds.get(soundKey).cleanUp();
        alcCloseDevice(device);
    }


    private static SoundManager instance;
    public static void initInstance() throws ALException{
        instance = new SoundManager();
    }
    public static SoundManager getInstance(){
        return instance;
    }
}
