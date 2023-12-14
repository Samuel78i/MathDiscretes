package com.almasb.fxglgames.drop;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

import static com.almasb.fxgl.dsl.FXGL.*;

public class GaltonPlankApp extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(900);
        settings.setTitle("Galton's Plank Showcase");
        settings.setVersion("1.0");
    }


    /**
     * Handle the spawn of every element in the game except the ball, so the wall and the nails (the all triangle)
     */
    @Override
    protected void initGame() {
        entityBuilder()
                .buildScreenBoundsAndAttach(40);



        Entity firstTriangle = spawnFirstNail();
        double xFirstOne = firstTriangle.getX();
        double yFirstOne = firstTriangle.getY();
        double x = 0;
        double y = 0;
        for(int i = 1; i < 10; i++){
        	Entity e;
        	if(i==9) {
                spawnWall(xFirstOne - 89, yFirstOne + 35);
                spawnANail(xFirstOne - 90, yFirstOne + 30);
                spawnWall(xFirstOne - 29, yFirstOne + 35);
                e = spawnANail(xFirstOne - 30, yFirstOne + 30);}
        	else {
                e = spawnANail(xFirstOne - 30, yFirstOne + 30);
            }
            xFirstOne = e.getX();
            yFirstOne = e.getY();
            x = e.getX();
            y = e.getY();
            for(int j = 0; j<i; j++){
            	if(i==9) {
                    spawnWall(x + 61, y + 6);
                }
        		e = spawnANail(x + 60, y);
                x = e.getX();
                y = e.getY();
            }
        }
        spawnWall(x + 61, y + 6);
        spawnANail(x + 60, y);
    }


    /**
     * Handle the spawn of the ball on each click
     */
    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("ballSpawn") {
            @Override
            protected void onActionEnd() {
                //spawnBall(getInput().getMouseXWorld());
            	spawnBall();
            }
        }, MouseButton.PRIMARY);
    }



    private Entity spawnFirstNail(){
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);

        return entityBuilder()
                .at(400, 75)
                .bbox(new HitBox(BoundingShape.circle(4)))
                .view(new Circle(4, 4, 4, Color.GRAY))
                .with(physics)
                .buildAndAttach();


    }


    private Entity spawnANail(double x, double y) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);

        return entityBuilder()
                        .at(x, y)
                        .bbox(new HitBox(BoundingShape.circle(4)))
                        .view(new Circle(4, 4, 4, Color.GRAY))
                        .with(physics)
                        .buildAndAttach();

    }



    private void spawnBall() {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setFixtureDef(new FixtureDef().density(6.5f).friction(1.0f).restitution(0.05f));
        physics.setBodyType(BodyType.DYNAMIC);

        physics.setOnPhysicsInitialized(() -> physics.setLinearVelocity(0, 200));

        double n = Math.random() * (408 - 391) + 391;
        entityBuilder()
                .at(n, 10)
                .bbox(new HitBox(BoundingShape.circle(5)))
                .view(new Circle(5, 5, 5, Color.RED))
                .with(physics)
                .buildAndAttach();
    }
    
    private void spawnWall(double x, double y) {
    	PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);

        var t = texture("Wall.png")//réussir à enlever la texture
                .subTexture(new Rectangle2D(0, 0, 14, 550));

        entityBuilder()
                .at(x, y)
                .viewWithBBox(t)
                .with(physics)
                .buildAndAttach();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
