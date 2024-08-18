package io.Github.Pong;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */

public class Main extends ApplicationAdapter
{
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Player player;
    private Bullet bullet;
    private Opponent opponent;
    private Score playerScore;
    private Score opponentScore;

    private World world;
    private Box2DDebugRenderer debugRenderer;

    private final float gameWidth = 450;
    private final float gameHeight = 180;

    private boolean isGameOver;
    private EndGameScreen endGameScreen;

    @Override
    public void create()
    {

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 450, 240);

        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();

        createWorldEdges();

        //creation of the player at the center in y ;
        player = new Player(world , "sprites/Player.png", 50, 210, gameWidth, gameHeight);
        player.getBody().setUserData("player");

        bullet = new Bullet(world, "sprites/Bullet.png", 150, 150, 200, 50);
        bullet.getBody().setUserData("bullet");


        float opponentStartX = gameWidth - 50;
        float opponentStartY = (gameHeight - 50) / 2;
        opponent = new Opponent(world, "sprites/Player.png", opponentStartX, opponentStartY, 50, gameWidth, gameHeight);
        opponent.getBody().setUserData("opponent");

        playerScore = new Score();
        opponentScore = new Score();

        camera.position.set(gameWidth /2, gameHeight / 2, 0);
        camera.zoom = 1f;
        camera.update();

        world.setContactListener(new ContactListener()
        {
            @Override
            public void beginContact(Contact contact)
            {
                Body a = contact.getFixtureA().getBody();
                Body b = contact.getFixtureB().getBody();

                System.out.println("Collision detected " + a.getUserData() + " and " + b.getUserData());

                if ("player".equals(a.getUserData()) && "bullet".equals(b.getUserData()) ||
                    "bullet".equals(a.getUserData()) && "player".equals(b.getUserData()))
                {
                    handleBulletPlayerCollision();
                }

                if ("opponent".equals(a.getUserData()) && "bullet".equals(b.getUserData()) ||
                    "bullet".equals(a.getUserData()) && "opponent".equals(b.getUserData()))
                {
                    handleBulletOpponentCollision();
                }

                if ("bullet".equals(a.getUserData()) && isEdge(b) ||
                    "bullet".equals(b.getUserData()) && isEdge(a))
                {
                    handleEdgeCollision();
                }
            }

            @Override
            public void endContact(Contact contact)
            {

            }

            @Override
            public void preSolve(Contact contact, Manifold manifold)
            {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse contactImpulse)
            {

            }
        });
    }

    private void handleBulletPlayerCollision()
    {
        Vector2 currentVelocity = bullet.getBody().getLinearVelocity();

        float variation = (float)(Math.random() - 0.5) * 2 * 10;
        float newVelocityY = currentVelocity.y + variation;

        bullet.getBody().setLinearVelocity(-currentVelocity.x, newVelocityY);
    }

    private void handleBulletOpponentCollision()
    {
        Vector2 currentVelocity = bullet.getBody().getLinearVelocity();

        float variation = (float)(Math.random() - 0.5) * 2 * 10;
        float newVelocityY = currentVelocity.y + variation;

        bullet.getBody().setLinearVelocity(-currentVelocity.x, newVelocityY);
    }

    private boolean isEdge(Body body)
    {
        boolean isEdge = body.getType() == BodyDef.BodyType.StaticBody && body.getLinearVelocity().isZero();
        System.out.println("isEdge: " + isEdge);
        return isEdge;
    }

    private void handleEdgeCollision()
    {
        float bulletX = bullet.getBody().getPosition().x;
        System.out.println("Collision with a wall detected");
        System.out.println(("bulletX = " + bulletX + ", gameWidth : " + gameWidth));

        if (bulletX > 440)
        {
            playerScore.increment(1);
            System.out.println("Collision with the the right board, incrementation of playerScore");

        }
        else if (bulletX < 67)
        {
            opponentScore.increment(1);
            System.out.println("Collision with the the left board, incrementation of playerScore");
        }
    }

    private void createWorldEdges()
    {

        BodyDef edgeDef = new BodyDef();
        edgeDef.type = BodyDef.BodyType.StaticBody;

        Body edges = world.createBody(edgeDef);

        EdgeShape edgeShape = new EdgeShape();
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = edgeShape;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 1.0f;

        edges.setUserData("edge");
        //superior board
        edgeShape.set(0, gameHeight, gameWidth, gameHeight);
        edges.createFixture(fixtureDef);

        //inferior board
        edgeShape.set(0, 0, gameWidth, 0);
        edges.createFixture(fixtureDef);

        //Left board
        edgeShape.set(0, 0, 0, gameHeight);
        edges.createFixture(fixtureDef);

        //Right board
        edgeShape.set(gameWidth, 0, gameWidth, gameHeight);
        edges.createFixture(fixtureDef);
        edgeShape.dispose();
    }

    //if the bullet appears to be out of the bonds of our game limits,
    // we will need to continue the game. So we will destroy the bullet if it exceeds the limits.
    // and we will create another one

    private void checkIfBulletIsOutOfBonds()
    {
        float bulletX = bullet.getBody().getPosition().x;
        float bulletY = bullet.getBody().getPosition().y;

        if (bulletX < 0 || bulletX > gameWidth || bulletY < 0 || bulletY > gameHeight)
        {
            bullet.dispose();
            bullet = new Bullet(world, "sprites/Bullet.png", gameWidth / 2, gameHeight / 2, 200, 50);
            bullet.getBody().setUserData("bullet");
        }
    }

    private void checkForGameOver()
    {
        if (playerScore.getScore() >= 10)
        {
            isGameOver = true;
            endGameScreen = new EndGameScreen("VICTORY");
        }
        else if (opponentScore.getScore() >=10)
        {
            isGameOver = true;
            endGameScreen = new EndGameScreen("DEFEAT");
        }
    }

    @Override
    public void render()
    {


        if (!isGameOver)
        {
            float deltaTime = Gdx.graphics.getDeltaTime();
            world.step(1/60f, 6, 2);
            player.update(deltaTime);
            opponent.update(deltaTime, bullet.getY());
            bullet.update(deltaTime);
            checkIfBulletIsOutOfBonds();
            checkForGameOver();
        }

        ScreenUtils.clear(0f, 0f, 0f, 0f);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.render(batch);
        bullet.render(batch);
        opponent.render(batch);
        playerScore.render(batch, camera);
        opponentScore.render(batch, camera, true);


        if (isGameOver)
        {
            endGameScreen.render(batch, camera);
        }

        batch.end();

    }

    @Override
    public void dispose()
    {
        batch.dispose();
        player.dispose();
        opponent.dispose();
        bullet.dispose();
        playerScore.dispose();
        endGameScreen.dispose();
    }
}
