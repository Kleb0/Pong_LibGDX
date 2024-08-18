package io.Github.Pong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;

public class Player
{
    private Texture playerTexture;
    private Body body;
    private World world;

    private final float speed = 150;
    private float gameWidth;
    private float gameHeight;

    //the player class constructor
    public Player(World world, String texturePath, float x, float y, float gameWidth, float gameHeight)
    {
        this.world = world;
        this.playerTexture = new Texture(texturePath);
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;  // we will use the kinematic body to control our player
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);

        PolygonShape box = new PolygonShape();
        box.setAsBox(playerTexture.getWidth()/2f, playerTexture.getHeight()/ 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.isSensor = false; // with this, the player is able to detect collisions
        body.createFixture(fixtureDef);
        box.dispose();
    }

    public void update(float deltaTime)
    {
        float velocityY = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            velocityY = speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            velocityY = -speed;
        }

        body.setLinearVelocity(0, velocityY);

        //Here we prevent the player to reach the game limit
        if (body.getPosition().y < playerTexture.getHeight() /2f)
        {
            body.setTransform(body.getPosition().x, playerTexture.getHeight() /2f, 0);
        }
        if (body.getPosition().y > gameHeight - playerTexture.getHeight() /2f)
        {
            body.setTransform(body.getPosition().x, gameHeight - playerTexture.getHeight() /2f, 0);
        }

    }

    public void render(SpriteBatch batch)
    {
        batch.draw(playerTexture, body.getPosition().x - playerTexture.getWidth() / 2f, body.getPosition().y - playerTexture.getHeight() / 2f);

    }

    public Body getBody()
    {
        return body;
    }

    public void dispose()
    {
        playerTexture.dispose();
    }


    public float getHeight()
    {
        return playerTexture.getHeight();
    }

}
