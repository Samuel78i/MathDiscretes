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
    private static final int NUM_PLANKS = 10;
    private static final int BOARD_ROWS = 10;
    private static final int BOARD_COLUMNS = 20;
    private static final int BALL_RADIUS = 5;

    private Entity ball;

    public enum Type {
        BALL, NAIL
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("Galton's Plank Showcase");
        settings.setVersion("1.0");
    }

    @Override
    protected void initGame() {
        entityBuilder()
                .buildScreenBoundsAndAttach(40);
        for (int row = 0; row < 600; row += 50) {
            for (int col = 0; col < 800 - row; col += 50) {
                spawnBoard(row, col);
            }
        }
        spawnBall();
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("trtr") {
            private double x;
            private double y;

            @Override
            protected void onActionBegin() {
                x = getInput().getMouseXWorld();
                y = getInput().getMouseYWorld();
            }

            @Override
            protected void onActionEnd() {
                var endx = getInput().getMouseXWorld();
                var endy = getInput().getMouseYWorld();

                spawnBall();
            }
        }, MouseButton.PRIMARY);
    }



    private void spawnBoard(double x, double y) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setFixtureDef(new FixtureDef().density(25.5f).restitution(0.5f));
        physics.setBodyType(BodyType.STATIC);

        var t = texture("brick.png")
                .subTexture(new Rectangle2D(0, 0, 15, 14))
                .multiplyColor(Color.RED);

        entityBuilder()
                        .at(x, y)
                        .viewWithBBox(t)
                        .with(physics)
                        .buildAndAttach();

    }



    private void spawnBall() {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setFixtureDef(new FixtureDef().density(6.5f).friction(1.0f).restitution(0.05f));
        physics.setBodyType(BodyType.DYNAMIC);

        physics.setOnPhysicsInitialized(() -> {
            physics.setLinearVelocity(0, 50 * 5);
        });

        ball = entityBuilder()
                .at(400, 50)
                .bbox(new HitBox(BoundingShape.circle(15)))
                .view(texture("img_1.png", 15, 15))
                .with(physics)
                .with(new ExpireCleanComponent(Duration.seconds(5)).animateOpacity())
                .buildAndAttach();
    }

    private void dropBall() {
        //ball.getComponent(PhysicsComponent.class).setLinearVelocity(FXGLMath.random(-200, 200), 600);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
