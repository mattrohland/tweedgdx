package com.tweedgdx.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.JsonValue;
import com.tweedgdx.helpers.DrawableInterface;
import java.util.ArrayList;


public class RenderComponent implements Component {
    public String targettedBlockAlias;
    public JsonValue config;
    public ArrayList<DrawableInterface> drawables;

    public RenderComponent(String targettedBlockAlias, JsonValue config){
        this.targettedBlockAlias = targettedBlockAlias;
        this.config = config;
        this.drawables = new ArrayList<DrawableInterface>();
    }

    public static void addComponentToEntity(JsonValue instructions, Entity entity){
        if(instructions != null && instructions.get("targettedBlockAlias").isString()){
            if(instructions.get("config") != null){
                entity.add(new RenderComponent(instructions.getString("targettedBlockAlias"), instructions.get("config")));
            }
        }
    }
}
