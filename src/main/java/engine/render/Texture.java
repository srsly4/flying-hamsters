package engine.render;

import de.matthiasmann.twl.utils.PNGDecoder;
import engine.EngineException;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

/**
 * Created by Szymon Piechaczek on 19.12.2016.
 */
public class Texture {
    private final int textureId;


    public Texture(String fname) throws EngineException {
        this.textureId = Texture.loadTexture(fname);
    }
    public Texture(int id) {
        this.textureId = id;
    }

    public int getId(){ return textureId; }
    public void bind(){
        glBindTexture(GL_TEXTURE_2D, textureId);
    }

    public void cleanup(){
        glDeleteTextures(textureId);
    }

    private static int loadTexture(String filename) throws EngineException{
        try{
            PNGDecoder decoder = new PNGDecoder(Texture.class.getResourceAsStream(filename));

            ByteBuffer buff = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buff, decoder.getWidth()*4, PNGDecoder.Format.RGBA);
            buff.flip();

            //opengl texture
            int tid = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, tid);
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buff);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            //glGenerateMipmap(GL_TEXTURE_2D);

            return tid;
        }
        catch(IOException|NullPointerException e)
        {
            EngineException ex = new EngineException("Could not load texture: " + filename);
            ex.initCause(e);
            throw ex;
        }

    }
}
