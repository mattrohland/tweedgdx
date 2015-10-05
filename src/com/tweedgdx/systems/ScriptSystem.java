package com.tweedgdx.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.tweedgdx.components.ScriptComponent;
import com.tweedgdx.helpers.ScriptHandlerLua;


public class ScriptSystem extends IteratingSystem{

    private ComponentMapper<ScriptComponent> scriptComponentMapper = ComponentMapper.getFor(ScriptComponent.class);
    private Engine entityEngine;
    private ScriptEntityListener scriptEntityListener;

    public ScriptSystem(){
        super(Family.all(ScriptComponent.class).get());
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
        }

        public void entityRemoved(Entity entity){
            ScriptComponent scriptComponent = ScriptSystem.this.scriptComponentMapper.get(entity);

            for(ScriptHandlerLua script : scriptComponent.scripts){
                script.call("entityRemoved");
            }
        }
    }

}
