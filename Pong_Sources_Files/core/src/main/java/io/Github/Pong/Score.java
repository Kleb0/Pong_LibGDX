package io.Github.Pong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Score
{

    private int score;
    private BitmapFont font;

    public Score()
    {
        this.score = 0;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int)(24 * Gdx.graphics.getDensity());
        parameter.borderWidth = 0;
        parameter.shadowOffsetX = 0;
        parameter.shadowOffsetY = 0;
        parameter.minFilter = Texture.TextureFilter.Nearest;
        parameter.magFilter = Texture.TextureFilter.Nearest;
        parameter.genMipMaps = true;


        this.font = generator.generateFont(parameter);
        generator.dispose();

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

                    pixmap.drawPixel(x, y, 0x0000000);
                }
            }
        }

        Texture newFontTexture = new Texture(pixmap);
        pixmap.dispose();

        font.getRegion().setTexture(newFontTexture);



    }

    public void increment (int amount)
    {
        score += amount;
    }

    public void render(SpriteBatch batch, OrthographicCamera camera)
    {
        float x = camera.position.x - camera.viewportWidth / 2 +10; // 10 pixels from the left board
        float y = camera.position.y + camera.viewportHeight / 2 -10; // 10 pixels from the superior board

        font.draw(batch, "Score : " + score, x, y);
    }

    // we will override the function render if the opponent score is true
    public void render(SpriteBatch batch, OrthographicCamera camera, boolean isOpponentScore)
    {
        float x;
        if (isOpponentScore)
        {
            x = camera.position.x + camera.viewportWidth / 2 - 100; // adjust from the left
        }
        else
        {
            x = camera.position.x - camera.viewportWidth / 2 + 10; // 10 pixels from the left bord
        }
        float y = camera.position.y + camera.viewportHeight / 2 -10; // 10 pixels from the superior bord

        font.draw(batch,"Score : " + score, x, y );
    }

    public void dispose()
    {
        font.dispose();
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }


}
