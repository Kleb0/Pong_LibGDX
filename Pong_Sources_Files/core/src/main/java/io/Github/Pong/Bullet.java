package io.Github.Pong;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Bullet {

    private Texture bulletTexture;
    private Body body;
    private World world;



    public Bullet(World world, String texturePath, float x, float y, float speedX, float speedY)
    {

        this.world = world;
        this.bulletTexture = new Texture(texturePath);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(bulletTexture.getWidth() * 0.25f); // the shape size is the texture width size multiplied by a float

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 1.0f; // this will allow our bullet to bounce


        body.createFixture(fixtureDef);
        circle.dispose();
        body.setLinearVelocity(speedX, speedY);
    }

    public void update(float deltaTime)
    {

    }

    public void render(SpriteBatch batch)
    {
        float width = bulletTexture.getWidth() * 0.5f;
        float height = bulletTexture.getHeight() * 0.5f;
        batch.draw(bulletTexture, body.getPosition().x - width /2, body.getPosition().y - height /2, width, height);
    }

    public void dispose()
    {
        bulletTexture.dispose();
        world.destroyBody(body);
    }

    public Body getBody()
    {
        return body;
    }

    public float getX(){
        return body.getPosition().x;
    }
    public float getY(){
        return body.getPosition().y;
    }
}
