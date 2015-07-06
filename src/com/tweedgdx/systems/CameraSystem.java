package com.tweedgdx.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;


public class CameraSystem extends EntitySystem{
    private Box2DDebugRenderer debugRenderer;
    private Engine entityEngine;

    public OrthographicCamera camera;

    @Override
    public void addedToEngine(Engine entityEngine){
        super.addedToEngine(entityEngine);

        this.entityEngine = entityEngine;

        // Set Camera
        this.camera = new OrthographicCamera();

        // Debug Box2d
        this.debugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);

        // Update Camera
        this.camera.update();

        // Graphics
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // Debug Renderer
        this.debugRenderer.render(this.entityEngine.getSystem(PhysicsSystem.class).world, this.camera.combined);

        super.update(deltaTime);
    }

    public void resize(float width, float height){
        this.camera.viewportWidth = width;
        this.camera.viewportHeight = height;
        this.camera.position.set(this.camera.viewportWidth / 2f, this.camera.viewportHeight / 2f, 0);
    }
}
