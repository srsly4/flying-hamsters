package engine.render;

import static org.lwjgl.opengl.GL11.*;

import de.matthiasmann.twl.utils.PNGDecoder;
import engine.EngineException;
import engine.EngineResourceException;
import engine.Utils;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import sun.misc.IOUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * Created by Sirius on 21.12.2016.
 */
public class FontBuffer {

    private int textureId;
    private int size;
    private HashMap<Character, CharInfo> charMap;
    private int width;
    private int height;
    public FontBuffer(String fileName, int size, Vector3f color) throws EngineException{
        try {
            charMap = new HashMap<>();

            this.size = size;
            textureId = glGenTextures();

            BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = img.createGraphics();
            Font fnt = Font.createFont(Font.TRUETYPE_FONT, Utils.class.getResourceAsStream(fileName)).deriveFont((float)size);
            g2d.setFont(fnt);
            FontMetrics fm = g2d.getFontMetrics();

            String chars = getAllAvailableChars("ISO-8859-1");
            width = 0;
            height = 0;

            //get characters widths, heights and X locations
            for (char c : chars.toCharArray())
            {
                CharInfo ci = new CharInfo(width, fm.charWidth(c));
                charMap.put(c, ci);
                width += ci.getWidth();
                height = Math.max(height, fm.getHeight());
            }
            g2d.dispose();

            img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            g2d = img.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setFont(fnt);
            fm = g2d.getFontMetrics();
            g2d.setColor(new Color(color.x, color.y, color.z));
            g2d.drawString(chars, 0, fm.getAscent());
            g2d.dispose();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(img, "png", out);
            out.flush();

            InputStream is = new ByteArrayInputStream(out.toByteArray());
            PNGDecoder decoder = new PNGDecoder(is);

            ByteBuffer buff = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buff, decoder.getWidth()*4, PNGDecoder.Format.RGBA);
            buff.flip();

            //create a texture from all characters
            glBindTexture(GL_TEXTURE_2D, textureId);
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buff);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        }
        catch (IOException|NullPointerException|ArrayIndexOutOfBoundsException|FontFormatException e){
            throw (EngineResourceException)(new EngineResourceException("Could not load font: " + fileName).initCause(e));
        }
    }

    private String getAllAvailableChars(String charsetName) {
        CharsetEncoder ce = Charset.forName(charsetName).newEncoder();
        StringBuilder result = new StringBuilder();
        for (char c = 0; c < Character.MAX_VALUE; c++) {
            if (ce.canEncode(c)) {
                result.append(c);
            }
        }
        return result.toString();
    }

    public int getTextureId() { return textureId; }
    public CharInfo getCharInfo(Character c){ return charMap.get(c); }

    public int getSize() {
        return size;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void bind(){
        glBindTexture(GL_TEXTURE_2D, textureId);
    }

    public void cleanup(){
        glDeleteTextures(textureId);
    }

    public static class CharInfo {

        private final int startX;

        private final int width;

        public CharInfo(int startX, int width) {
            this.startX = startX;
            this.width = width;
        }

        public int getStartX() {
            return startX;
        }

        public int getWidth() {
            return width;
        }
    }

}
