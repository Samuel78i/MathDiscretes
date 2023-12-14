/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */

package com.almasb.fxglgames.drop;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.dsl.components.LiftComponent;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

// NOTE: this import above is crucial, it pulls in many useful methods

/**
 * This is an FXGL version of the libGDX simple game tutorial, which can be found
 * here - https://github.com/libgdx/libgdx/wiki/A-simple-game
 *
 * The player can move the bucket left and right to catch water droplets.
 * There are no win/lose conditions.
 *
 * Note: for simplicity's sake all of the code is kept in this file.
 * In addition, most of typical FXGL API is not used to avoid overwhelming
 * FXGL beginners with a lot of new concepts to learn.
 *
 * Although the code is self-explanatory, some may find the comments useful
 * for following the code.
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class DropApp extends GameApplication {

    /**
     * Types of entities in this game.
     */
    private Entity ball;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(900);
        settings.setTitle("Galton's Plank Showcase");
        settings.setVersion("1.0");
    }

    @Override
    protected void initGame() {
        entityBuilder()
                .buildScreenBoundsAndAttach(40);



        Entity firstTriangle = spawnFirstTriangle();

        double xFirstOne = firstTriangle.getX();
        double yFirstOne = firstTriangle.getY();
        double x = 0;
        double y = 0;
        for(int i = 1; i < 10; i++){
        	Entity e;
        	if(i==9) {
        		e = spawnWall(xFirstOne - 89, yFirstOne + 35);
        		e = spawnBoard(xFirstOne - 90, yFirstOne + 30);
        		e = spawnWall(xFirstOne - 29, yFirstOne + 35);
        		e = spawnBoard(xFirstOne - 30, yFirstOne + 30);}
        	else {e = spawnBoard(xFirstOne - 30, yFirstOne + 30);}
            xFirstOne = e.getX();
            yFirstOne = e.getY();
            x = e.getX();
            y = e.getY();
            for(int j = 0; j<i; j++){
            	if(i==9) {
            		e = spawnWall(x + 61, y+6);
            	}
        		e = spawnBoard(x + 60, y);
                x = e.getX();
                y = e.getY();
            }
        }
        Entity e = spawnWall(x + 61, y+6);
        e = spawnBoard(x + 60, y);
    }

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



    private Entity spawnFirstTriangle(){
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);

        return entityBuilder()
                .at(400, 75)
                .bbox(new HitBox(BoundingShape.circle(4)))
                .view(new Circle(4, 4, 4, Color.GRAY))
                .with(physics)
                .buildAndAttach();


    }


    private Entity spawnBoard(double x, double y) {
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

        physics.setOnPhysicsInitialized(() -> {
            physics.setLinearVelocity(0, 200);
        });

        double n = Math.random() * (408 - 391) + 391;
        ball = entityBuilder()
                .at(n, 10)
                .bbox(new HitBox(BoundingShape.circle(5)))
                .view(new Circle(5, 5, 5, Color.RED))
                .with(physics)
                .buildAndAttach();
    }
    
    private Entity spawnWall(double x, double y) {
    	PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);

        var t = texture("Wall.png")//réussir à enlever la texture
                .subTexture(new Rectangle2D(0, 0, 7, 550));

        return entityBuilder()
                        .at(x, y)
                        .viewWithBBox(t)
                        .with(physics)
                        .buildAndAttach();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
