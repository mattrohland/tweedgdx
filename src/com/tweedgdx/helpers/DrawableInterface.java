package com.tweedgdx.helpers;

import com.badlogic.ashley.core.Entity;


public interface DrawableInterface{
    void draw(RenderBlockInterface renderBlock);
    void update(RenderBlockInterface renderBlock, Entity entity, float deltaTime);
}
