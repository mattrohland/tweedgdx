package com.tweedgdx.helpers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.JsonValue;
import com.tweedgdx.systems.CameraSystem;
import com.tweedgdx.systems.PhysicsSystem;


public class Box2dDebuggerRenderBlock implements RenderBlockInterface{
    public Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    public JsonValue instructions;

    private Engine entityEngine;
    private OrthographicCamera camera;
    private World world;

    public Box2dDebuggerRenderBlock(Engine entityEngine, JsonValue instructions) {
        this.instructions = instructions;
        this.entityEngine = entityEngine;
        this.world = this.entityEngine.getSystem(PhysicsSystem.class).world;
        this.camera = this.entityEngine.getSystem(CameraSystem.class).camera;

        // Debug Box2d
        this.debugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);

    }

    @Override
    public void draw() {
        // Debug Renderer
        this.debugRenderer.render(this.world, this.camera.combined.cpy().scale(this.entityEngine.getSystem(PhysicsSystem.class).getInPixels(1f), this.entityEngine.getSystem(PhysicsSystem.class).getInPixels(1f), 1f));
    }

    @Override
    public void addEntity(Entity entity) {
        // Do nothing.
    }

    @Override
    public void removeEntity(Entity entity) {
        // Do nothing.
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        // Do nothing.
    }
}
