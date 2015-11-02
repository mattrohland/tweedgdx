package com.tweedgdx.helpers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.JsonValue;


public interface EntityFactoryInterface{
    public Entity create(String entityInstructionsJsonString);
    public Entity create(JsonValue entityInstructions);
}
