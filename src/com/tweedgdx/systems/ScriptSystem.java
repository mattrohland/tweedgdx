package com.tweedgdx.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.JsonValue;
import com.tweedgdx.components.ScriptComponent;
import com.tweedgdx.helpers.ScriptHandlerLua;


public class ScriptSystem extends IteratingSystem{

    private ComponentMapper<ScriptComponent> scriptComponentMapper = ComponentMapper.getFor(ScriptComponent.class);
    private Engine entityEngine;
    private ScriptEntityListener scriptEntityListener;
    private JsonValue instructions;

    public ScriptSystem(JsonValue instructions){
        super(Family.all(ScriptComponent.class).get());
        this.instructions = instructions;
    }

    @Override
    public void addedToEngine(Engine entityEngine){
        super.addedToEngine(entityEngine);
        this.entityEngine = entityEngine;
        this.scriptEntityListener = new ScriptEntityListener();

        // Start Listening
        this.entityEngine.addEntityListener(Family.all(ScriptComponent.class).get(), 200, this.scriptEntityListener);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime){
        ScriptComponent scriptComponent = ScriptSystem.this.scriptComponentMapper.get(entity);

        for(ScriptHandlerLua script : scriptComponent.scripts){
            script.call("processEntity");
        }
    }

    private class ScriptEntityListener implements EntityListener{
        public void entityAdded(Entity entity){
            ScriptComponent scriptComponent = ScriptSystem.this.scriptComponentMapper.get(entity);

            for(String scriptLocation : scriptComponent.scriptLocations){
                ScriptHandlerLua script = new ScriptHandlerLua(scriptLocation, ScriptSystem.this.entityEngine);
                scriptComponent.scripts.add(script);
                script.callInit(entity);
            }

            for(ScriptHandlerLua script : scriptComponent.scripts){
                script.call("entityAdded");
            }

            ScriptSystem.this.entityEngine.getSystem(PhysicsSystem.class).world.setContactListener(new ScriptContactListener(entity));
        }

        public void entityRemoved(Entity entity){
            ScriptComponent scriptComponent = ScriptSystem.this.scriptComponentMapper.get(entity);

            for(ScriptHandlerLua script : scriptComponent.scripts){
                script.call("entityRemoved");
            }
        }
    }

    private class ScriptContactListener implements ContactListener{
        Entity entity;

        ScriptContactListener(Entity entity){
            super();
            this.entity = entity;
        }

        @Override
        public void beginContact(Contact contact){
            ScriptComponent scriptComponent = ScriptSystem.this.scriptComponentMapper.get(this.entity);

            for(ScriptHandlerLua script : scriptComponent.scripts){
                script.call("beginContact");
            }
        }

        @Override
        public void endContact(Contact contact){
            ScriptComponent scriptComponent = ScriptSystem.this.scriptComponentMapper.get(this.entity);

            for(ScriptHandlerLua script : scriptComponent.scripts){
                script.call("endContact");
            }
        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold){
            ScriptComponent scriptComponent = ScriptSystem.this.scriptComponentMapper.get(this.entity);

            for(ScriptHandlerLua script : scriptComponent.scripts){
                script.call("preSolve");
            }
        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse){
            ScriptComponent scriptComponent = ScriptSystem.this.scriptComponentMapper.get(this.entity);

            for(ScriptHandlerLua script : scriptComponent.scripts){
                script.call("postSolve");
            }
        }

    }

}
