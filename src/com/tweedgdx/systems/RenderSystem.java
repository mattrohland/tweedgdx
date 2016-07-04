package com.tweedgdx.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.JsonValue;
import com.tweedgdx.components.RenderComponent;
import com.tweedgdx.helpers.Box2dDebuggerRenderBlock;
import com.tweedgdx.helpers.RenderBlockInterface;
import com.tweedgdx.helpers.SpriteRenderBlock;

import java.util.LinkedHashMap;


public class RenderSystem extends IntervalSystem {

    private RenderableEntitiesManagementSystem renderableEntitiesManagementSystem;
    private JsonValue instructions;

    protected LinkedHashMap<String, RenderBlockInterface> renderBlocks;

    public RenderSystem(JsonValue instructions) {
        super(instructions.getFloat("renderInterval", (1f / 60f)));
        this.instructions = instructions;
        this.cleanSlate();
    }

    @Override
    public void addedToEngine(Engine entityEngine) {
        // Setup renderBlocks based on the instructions.
        if (this.instructions.get("renderBlocks").isArray()) {
            this.renderBlocks = new LinkedHashMap<String, RenderBlockInterface>();

            for (int i = 0; i < this.instructions.get("renderBlocks").size; i++) {
                String type = this.instructions.get("renderBlocks").get(i).getString("type");

                if (type.equals("sprite")) {
                    this.renderBlocks.put(this.instructions.get("renderBlocks").get(i).getString("alias"), new SpriteRenderBlock(entityEngine, this.instructions));
                } else if (type.equals("box2dDebugger")) {
                    this.renderBlocks.put(this.instructions.get("renderBlocks").get(i).getString("alias"), new Box2dDebuggerRenderBlock(entityEngine, this.instructions));
                }

            }
        }

        // Instantiate a RenderableEntitiesManagementSystem and add it to the system.
        this.renderableEntitiesManagementSystem = new RenderableEntitiesManagementSystem();
        entityEngine.addSystem(this.renderableEntitiesManagementSystem);
    }

    @Override
    public void removedFromEngine(Engine entityEngine) {
        // Clean up renderBlocks
        this.renderBlocks = null;

        // Remove the RenderableEntitiesManagementSystem from the system.
        entityEngine.removeSystem(this.renderableEntitiesManagementSystem);
    }

    @Override
    protected void updateInterval() {
        /*
         The RenderSystem's updateInterval() method draws the renderBlocks in the established order.

         An interface is used to insure that all render blocks have a consistent draw() method set to invoke.
         */

        this.cleanSlate();
        for (String key : this.renderBlocks.keySet()) {
            this.renderBlocks.get(key).draw();
        }
    }

    protected void cleanSlate() {
        /*
         The following method renders a solid black screen.

         This occurs to insure that there is no remnants of previously drawn elements on the screen.
         */

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

    private class RenderableEntitiesManagementSystem extends IteratingSystem {
        /*
         This allows entities to be managed and maintained within the context of their specified render block.
         */

        private EntityListener renderableEntityListener;
        private ComponentMapper<RenderComponent> renderComponentMapper = ComponentMapper.getFor(RenderComponent.class);

        public RenderableEntitiesManagementSystem() {
            super(Family.all(RenderComponent.class).get());
        }

        @Override
        public void addedToEngine(Engine entityEngine) {
            super.addedToEngine(entityEngine);

            this.renderableEntityListener = new RenderableEntityListener();
            entityEngine.addEntityListener(Family.all(RenderComponent.class).get(), 500, this.renderableEntityListener);
        }

        @Override
        public void removedFromEngine(Engine entityEngine) {
            super.removedFromEngine(entityEngine);

            entityEngine.removeEntityListener(this.renderableEntityListener);
        }

        @Override
        protected void processEntity(Entity entity, float deltaTime) {
            RenderBlockInterface renderBlock = RenderSystem.this.renderBlocks.get(this.renderComponentMapper.get(entity).targettedBlockAlias);
            renderBlock.processEntity(entity, deltaTime);
        }

        private class RenderableEntityListener implements EntityListener {
            public void entityAdded(Entity entity) {
                RenderBlockInterface renderBlock = RenderSystem.this.renderBlocks.get(RenderableEntitiesManagementSystem.this.renderComponentMapper.get(entity).targettedBlockAlias);
                renderBlock.addEntity(entity);
            }

            public void entityRemoved(Entity entity) {
                RenderBlockInterface renderBlock = RenderSystem.this.renderBlocks.get(RenderableEntitiesManagementSystem.this.renderComponentMapper.get(entity).targettedBlockAlias);
                renderBlock.removeEntity(entity);
            }
        }
    }
}
