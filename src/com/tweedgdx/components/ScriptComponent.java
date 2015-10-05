package com.tweedgdx.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.JsonValue;
import com.tweedgdx.helpers.ScriptHandlerLua;
import java.util.ArrayList;


public class ScriptComponent implements Component {
    public ArrayList<String> scriptLocations;
    public ArrayList<ScriptHandlerLua> scripts = new ArrayList<ScriptHandlerLua>();
    public JsonValue data;

    public ScriptComponent(String scriptLocation, JsonValue data){
        this.scriptLocations = new ArrayList<String>();
        this.scriptLocations.add(scriptLocation);
        this.data = data;
    }

    public ScriptComponent(ArrayList<String> scriptLocations, JsonValue data){
        this.scriptLocations = scriptLocations;
        this.data = data;
    }

    public static void addComponentToEntity(JsonValue instructions, Entity entity){
        if(
            instructions != null
            && instructions.get("location") != null
        ){
            entity.add(new ScriptComponent(instructions.getString("location"), instructions.get("data")));
        }
    }
}
