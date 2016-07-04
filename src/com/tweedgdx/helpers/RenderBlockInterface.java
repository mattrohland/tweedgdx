package com.tweedgdx.helpers;

import com.badlogic.ashley.core.Entity;


public interface RenderBlockInterface {
    void draw();

    void addEntity(Entity entity);

    void removeEntity(Entity entity);

    void processEntity(Entity entity, float deltaTime);
}
