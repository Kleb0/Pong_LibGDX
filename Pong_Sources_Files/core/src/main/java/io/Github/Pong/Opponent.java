package io.Github.Pong;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;

public class Opponent {
    private Texture oponentTexture;
    private Body body;
    private World world;


    private final float speed = 150;
    private float gameWidth;
    private float gameHeight;


    //as the opponnent will try to catch the ball, we will add some delay to allow player's victory


    private float reactionDelay = 0.5f;
    private float timeSinceLastMove = 0f;
    private float errorMargin = 10f; // error margin in pixels

    private Bullet bullet;

    public Opponent(World world, String texturePath, float x, float y, float speedY, float gameWidth, float gameHeight)
    {
        this.world = world;
        this.oponentTexture = new Texture(texturePath);
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);

        PolygonShape box = new PolygonShape();
        box.setAsBox(oponentTexture.getWidth() /2f, oponentTexture.getHeight() / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.isSensor = false; // this wall allow the opponent to intercept the ball
        body.createFixture(fixtureDef);
        box.dispose();


    }

    public void update(float deltaTime, float bulletY)
    {

        timeSinceLastMove += deltaTime;

        //with this, the opponent will try to intercept the bullet

        if (timeSinceLastMove >= reactionDelay)
        {
            //some randomisation will allow errors for the opponent
            float targetY = bulletY + MathUtils.random(-errorMargin, errorMargin);

            if(body.getPosition().y + oponentTexture.getHeight() /2 < targetY)
            {

                body.setLinearVelocity(0, speed);

            }
            else if ( body.getPosition().y + oponentTexture.getHeight() / 2 > targetY)
            {
                body.setLinearVelocity(0, -speed);

            }

            timeSinceLastMove = 0f;
        }

        if (body.getPosition().y < oponentTexture.getHeight() /2f)
        {
            body.setTransform(body.getPosition().x, oponentTexture.getHeight() / 2f, 0);
        }
        if (body.getPosition().y > gameHeight - oponentTexture.getHeight() / 2f)
        {
            body.setTransform(body.getPosition().x, gameHeight - oponentTexture.getHeight() / 2f, 0);
        }
    }

    public void render(SpriteBatch batch)
    {
        batch.draw(oponentTexture, body.getPosition().x - oponentTexture.getWidth() / 2f, body.getPosition().y - oponentTexture.getHeight() / 2f);
    }

    public void dispose()
    {
        oponentTexture.dispose();
    }


    public Body getBody()
    {
        return body;
    }
    public float getHeight()
    {
        return  oponentTexture.getHeight();
    }


}
