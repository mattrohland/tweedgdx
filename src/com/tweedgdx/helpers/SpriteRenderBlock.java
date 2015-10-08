package com.tweedgdx.helpers;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.JsonValue;
import com.tweedgdx.components.PositionComponent;
import com.tweedgdx.components.RenderComponent;
import com.tweedgdx.systems.CameraSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class SpriteRenderBlock implements RenderBlockInterface{
    private ArrayList<Entity> entities = new ArrayList<Entity>();
    private JsonValue instructions;
    private OrthographicCamera camera;
    private ComponentMapper<RenderComponent> renderComponentMapper = ComponentMapper.getFor(RenderComponent.class);
    private Engine entityEngine;

    protected SpriteBatch batch;
    protected ComponentMapper<PositionComponent> positionComponentMapper = ComponentMapper.getFor(PositionComponent.class);

    public SpriteRenderBlock(Engine entityEngine, JsonValue instructions){
        this.batch = new SpriteBatch();
        this.entityEngine = entityEngine;
        this.instructions = instructions;
        this.camera = entityEngine.getSystem(CameraSystem.class).camera;
    }

    @Override
    public void draw(){
        this.batch.setProjectionMatrix(this.camera.combined);
        this.batch.begin();
        for(int i=0; i<this.entities.size(); i++){
            for(int j=0; j<this.renderComponentMapper.get(this.entities.get(i)).drawables.size(); j++){
                this.renderComponentMapper.get(this.entities.get(i)).drawables.get(j).draw(this);
            }
        }
        this.batch.end();
    }

    @Override
    public void addEntity(Entity entity){
        RenderComponent renderComponent = this.renderComponentMapper.get(entity);
        if(renderComponent.config.has("drawables") && renderComponent.config.get("drawables").isArray()){
            for(int i=0; i<renderComponent.config.get("drawables").size; i++){
                renderComponent.drawables.add(new SpriteDrawable(renderComponent.config.get("drawables").get(i)));
            }
        }
        this.entities.add(entity);
        this.sortEntities();
    }

    @Override
    public void removeEntity(Entity entity){
        this.entities.remove(entity);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime){
        for(int i=0; i<this.renderComponentMapper.get(entity).drawables.size(); i++) {
            this.renderComponentMapper.get(entity).drawables.get(i).update(this, entity, deltaTime);
        }
    }

    private void sortEntities(){
        /*
         Entities within a SpriteRenderBlock are sorted according to their z coordinate.

         "It's like 3D man!"
         */
        Collections.sort(this.entities, new Comparator<Entity>(){
            @Override
            public int compare(Entity entity1, Entity entity2) {
            return (
                SpriteRenderBlock.this.positionComponentMapper.get(entity1).z < SpriteRenderBlock.this.positionComponentMapper.get(entity2).z ? -1
                : SpriteRenderBlock.this.positionComponentMapper.get(entity1).z > SpriteRenderBlock.this.positionComponentMapper.get(entity2).z ? 1
                : 0
            );
            }
        });
    }
}
