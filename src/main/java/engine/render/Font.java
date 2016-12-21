package engine.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBTruetype.stbtt_BakeFontBitmap;

import engine.EngineException;
import engine.EngineResourceException;
import engine.Utils;
import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.*;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by Sirius on 21.12.2016.
 */
public class Font {

    private int textureId;
    private STBTTBakedChar.Buffer buffer;
    public Font(String fileName, int size) throws EngineException{
        try {
            textureId = glGenTextures();
            buffer = STBTTBakedChar.malloc(96);

            ByteBuffer ttf = ByteBuffer.wrap(
                    IOUtils.toByteArray(
                            Utils.class.getResourceAsStream(fileName)));
            ByteBuffer bitmap = BufferUtils.createByteBuffer(512*512);
            stbtt_BakeFontBitmap(ttf, size, bitmap, 512, 512, 32, buffer);

            glBindTexture(GL_TEXTURE_2D, textureId);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, 512, 512, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        }
        catch (IOException|NullPointerException|ArrayIndexOutOfBoundsException e){
            throw (EngineResourceException)(new EngineResourceException("Could not load font: " + fileName).initCause(e));
        }


    }
}
