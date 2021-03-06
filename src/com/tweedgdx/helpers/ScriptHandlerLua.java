package com.tweedgdx.helpers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.tweedgdx.components.AliasComponent;
import com.tweedgdx.components.PhysicsBodyComponent;
import com.tweedgdx.components.PositionComponent;
import com.tweedgdx.components.RenderComponent;
import com.tweedgdx.components.ScriptComponent;
import com.tweedgdx.systems.CameraSystem;
import com.tweedgdx.systems.InputSystem;
import com.tweedgdx.systems.PhysicsSystem;
import com.tweedgdx.systems.ScriptSystem;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;;


public class ScriptHandlerLua implements ScriptHandlerInterface {

    private Globals globals = JsePlatform.standardGlobals();
    private LuaValue scriptFileContentRaw;
    private boolean scriptFileExists;
    private Engine engine;
    private LuaValue[] context;

    public ScriptHandlerLua(String scriptLocation, Engine engine){
        this.engine = engine;

        if(!Gdx.files.internal(scriptLocation).exists()){
            this.scriptFileExists = false;

            Gdx.app.error("SCRIPT", scriptLocation+" does NOT exist.");
        }else{
            this.scriptFileExists = true;

            this.scriptFileContentRaw = globals.loadfile(scriptLocation);
            this.scriptFileContentRaw.call();
        }
    }

    @Override
    public boolean isCallable(){
        return this.scriptFileExists;
    }

    @Override
    public void callInit(Entity entity){
        this.context = this.createScriptContext(entity);

        this.call("init");
    }

    @Override
    public void call(String methodName){
        LuaValue method = globals.get(methodName);

        if(method.isfunction()){
            method.invoke(this.context);
        }
    }

    private LuaValue[] createScriptContext(Entity entity){
        return new LuaValue[]{
            CoerceJavaToLua.coerce(new EntityContext(entity)),
            CoerceJavaToLua.coerce(new GdxContext())
        };
    }

    private class EntityContext{
        public LuaValue components;
        public LuaValue instance;
        public LuaValue engine = CoerceJavaToLua.coerce(ScriptHandlerLua.this.engine);
        public LuaValue engineSystems;


        EntityContext(Entity entity){
            this.instance = CoerceJavaToLua.coerce(entity);
            this.components = CoerceJavaToLua.coerce(new EntityComponentsContext(entity));
            this.engineSystems = CoerceJavaToLua.coerce(new EngineSystemsContext(ScriptHandlerLua.this.engine));
        }

        private class EntityComponentsContext{
            public LuaValue position;
            public LuaValue physicsBody;
            public LuaValue alias;
            public LuaValue render;
            public LuaValue script;

            EntityComponentsContext(Entity entity){
                this.alias = CoerceJavaToLua.coerce(entity.getComponent(AliasComponent.class));
                this.physicsBody = CoerceJavaToLua.coerce(entity.getComponent(PhysicsBodyComponent.class));
                this.position = CoerceJavaToLua.coerce(entity.getComponent(PositionComponent.class));
                this.render = CoerceJavaToLua.coerce(entity.getComponent(RenderComponent.class));
                this.script = CoerceJavaToLua.coerce(entity.getComponent(ScriptComponent.class));
            }
        }

        private class EngineSystemsContext{
            public LuaValue camera;
            public LuaValue physics;
            public LuaValue input;
            public LuaValue script;

            EngineSystemsContext(Engine engine){
                this.camera = CoerceJavaToLua.coerce(engine.getSystem(CameraSystem.class));
                this.physics = CoerceJavaToLua.coerce(engine.getSystem(PhysicsSystem.class));
                this.input = CoerceJavaToLua.coerce(engine.getSystem(InputSystem.class));
                this.script = CoerceJavaToLua.coerce(engine.getSystem(ScriptSystem.class));
            }

        }

    }

    private class GdxContext{
        public LuaValue app = CoerceJavaToLua.coerce(Gdx.app);
        public LuaValue input = CoerceJavaToLua.coerce(Gdx.input);
        public LuaValue inputKeys = CoerceJavaToLua.coerce(new Input.Keys());
        public LuaValue graphics = CoerceJavaToLua.coerce(Gdx.graphics);
    }
}
