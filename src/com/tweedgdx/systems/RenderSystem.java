package com.tweedgdx.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.tweedgdx.components.RenderComponent;

public class RenderSystem extends IntervalSystem{

    ImmutableArray<Entity> entities;

    public RenderSystem(float interval){
        super(interval);
    }

    @Override
    public void addedToEngine(Engine engine){
        entities = engine.getEntitiesFor(Family.getFor(RenderComponent.class));
    }

    @Override
    protected void updateInterval(){
        Entity entity;

        for(int i=0; i<entities.size(); i++){
            entity = entities.get(i);
            entity.toString();
            //Gdx.app.debug("ENTITY", entity.toString());
        }
    }
}
