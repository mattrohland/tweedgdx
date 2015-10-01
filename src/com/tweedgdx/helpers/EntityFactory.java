package com.tweedgdx.helpers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.JsonValue;


public interface EntityFactory{
    public Entity create(JsonValue entityInstructions);
}
