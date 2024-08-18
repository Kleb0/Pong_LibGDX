package io.Github.Pong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;


public class EndGameScreen {


    private BitmapFont font;
    private String message;

    public EndGameScreen(String message)
    {
        this.message = message;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int)(48 * Gdx.graphics.getDensity());
        parameter.borderWidth = 0;
        parameter.shadowOffsetX = 0;
        parameter.shadowOffsetY = 0;
        parameter.minFilter = Texture.TextureFilter.Nearest;
        parameter.magFilter = Texture.TextureFilter.Nearest;
        parameter.genMipMaps = true;

        this.font = generator.generateFont(parameter);
        generator.dispose();

        //we apply the block of code than for our Score display, in order to " clean " the texture

        Texture fontTexture = font.getRegion().getTexture();
        Pixmap pixmap = fontTexture.getTextureData().consumePixmap();


        for (int y = 0; y < pixmap.getHeight(); y++) {
            for (int x = 0; x < pixmap.getWidth(); x++) {
                int pixel = pixmap.getPixel(x, y);
                int r = (pixel & 0xff000000) >>> 24;
                int g = (pixel & 0x00ff0000) >>> 16;
                int b = (pixel & 0x0000ff00) >>> 8;
                int a = pixel & 0x000000ff;

                if (r < 255 || g < 255 || b < 255) {
                    pixmap.drawPixel(x, y, 0x00000000);
                }
            }
        }

        Texture newFontTexture = new Texture(pixmap);
        pixmap.dispose();

        font.getRegion().setTexture(newFontTexture);

    }

    public void render(SpriteBatch batch, OrthographicCamera camera)
    {

        float x = camera.position.x - camera.viewportWidth / 2 + 150;
        float y = camera.position.y + camera.viewportHeight / 2 - 100;

        font.draw(batch, message, x, y);
    }

    public void dispose()
    {
        font.dispose();
    }
}
