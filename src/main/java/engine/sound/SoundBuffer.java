package engine.sound;

import engine.EngineException;
import engine.EngineResourceException;
import engine.Utils;
import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.openal.AL10.*;

/**
 * Created by Sirius on 27.12.2016.
 */
public class SoundBuffer {

    private AudioFormat format;
    private long length;
    private int bufferId;
    public SoundBuffer(String resourceName) throws EngineException{
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(new BufferedInputStream(Utils.class.getResourceAsStream(resourceName)));
            format = stream.getFormat();
            int alFormat = -1;
            switch(format.getChannels()) {
                case 1: // mono
                    switch(format.getSampleSizeInBits()) {
                        case 8:
                            alFormat = AL_FORMAT_MONO8;
                            break;
                        case 16:
                            alFormat = AL_FORMAT_MONO16;
                            break;
                    }
                    break;
                case 2: //stereo
                    switch(format.getSampleSizeInBits()) {
                        case 8:
                            alFormat = AL_FORMAT_STEREO8;
                            break;
                        case 16:
                            alFormat = AL_FORMAT_STEREO16;
                            break;
                    }
                    break;
            }

            byte[] b = IOUtils.toByteArray(stream); //we're loading all file to memory
            ByteBuffer buff = BufferUtils.createByteBuffer(b.length);
            buff.put(b);
            buff.flip();

            IntBuffer buffIdBuff = BufferUtils.createIntBuffer(1);
            alGenBuffers(buffIdBuff);

            alBufferData(buffIdBuff.get(0), alFormat, buff, (int)format.getSampleRate());
            length = (long)(1000f * stream.getFrameLength() / format.getFrameRate());

            bufferId = buffIdBuff.get(0);

        }
        catch (UnsupportedAudioFileException|IOException e){
            throw (EngineResourceException)(new EngineResourceException("Could not load sound: " + resourceName).initCause(e));
        }
    }

    public int getBufferId(){
        return bufferId;
    }

    public void cleanUp(){
        alDeleteBuffers(bufferId);
    }

}
