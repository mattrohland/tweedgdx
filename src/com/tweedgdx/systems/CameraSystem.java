package com.tweedgdx.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.JsonValue;


public class CameraSystem extends EntitySystem{
    private Engine entityEngine;

    public OrthographicCamera camera;
    public JsonValue instructions;

    public CameraSystem(JsonValue instructions){
        this.instructions = instructions;
    }

    @Override
    public void addedToEngine(Engine entityEngine){
        super.addedToEngine(entityEngine);

        this.entityEngine = entityEngine;

        // Set Camera
        this.camera = new OrthographicCamera();
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);

        // Update Camera
        this.camera.update();
    }

    public void resize(float width, float height){
        this.camera.viewportWidth = width;
        this.camera.viewportHeight = height;
        this.camera.position.set(this.camera.viewportWidth / 2f, this.camera.viewportHeight / 2f, 0);
    }
}
