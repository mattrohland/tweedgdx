package com.tweedgdx.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.tweedgdx.components.PhysicsBodyComponent;
import com.tweedgdx.helpers.EntityEngine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.JsonValue;
import com.tweedgdx.components.ScriptComponent;
import com.tweedgdx.helpers.ScriptHandlerLua;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;


public class ScriptSystem extends IteratingSystem {

    private ComponentMapper<ScriptComponent> scriptComponentMapper = ComponentMapper.getFor(ScriptComponent.class);
    private EntityEngine entityEngine;
    private ScriptEntityListener scriptEntityListener;
    private ScriptContactListener scriptContactListener;
    private JsonValue instructions;

    public Globals globals = JsePlatform.standardGlobals();

    public ScriptSystem(JsonValue instructions) {
        super(Family.all(ScriptComponent.class).get());
        this.instructions = instructions;
    }

    @Override
    public void addedToEngine(Engine entityEngine) {
        super.addedToEngine(entityEngine);
        this.entityEngine = (EntityEngine) entityEngine;
        this.scriptEntityListener = new ScriptEntityListener();
        this.scriptContactListener = new ScriptContactListener();

        // Start Listening
        this.entityEngine.addEntityListener(Family.all(ScriptComponent.class).get(), 200, this.scriptEntityListener);
        ScriptSystem.this.entityEngine.getSystem(PhysicsSystem.class).world.setContactListener(this.scriptContactListener);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ScriptComponent scriptComponent = ScriptSystem.this.scriptComponentMapper.get(entity);

        for (ScriptHandlerLua script : scriptComponent.scripts) {
            script.call("processEntity");
        }

        if (scriptComponent.removeEntity) this.entityEngine.removeEntity(entity);
    }

    public ScriptHandlerLua.EntityContext loadEntity(String entityKey) {
        return new ScriptHandlerLua.EntityContext(ScriptSystem.this.entityEngine.loadEntity(entityKey), ScriptSystem.this.entityEngine);
    }

    private class ScriptEntityListener implements EntityListener {
        public void entityAdded(Entity entity) {
            ScriptComponent scriptComponent = ScriptSystem.this.scriptComponentMapper.get(entity);

            for (String scriptLocation : scriptComponent.scriptLocations) {
                ScriptHandlerLua script = new ScriptHandlerLua(scriptLocation, ScriptSystem.this.entityEngine);
                scriptComponent.scripts.add(script);
                script.callInit(entity);
            }

            for (ScriptHandlerLua script : scriptComponent.scripts) {
                script.call("entityAdded");
            }
        }

        public void entityRemoved(Entity entity) {
            ScriptComponent scriptComponent = ScriptSystem.this.scriptComponentMapper.get(entity);

            for (ScriptHandlerLua script : scriptComponent.scripts) {
                script.call("entityRemoved");
            }
        }
    }

    private class ScriptContactListener implements ContactListener {
        ScriptContactListener() {
            super();
        }

        @Override
        public void beginContact(Contact contact) {
            Family family = Family.all(ScriptComponent.class, PhysicsBodyComponent.class).get();

            for (Entity entity : entityEngine.getEntitiesFor(family)) {
                ScriptComponent scriptComponent = ScriptSystem.this.scriptComponentMapper.get(entity);
                for (ScriptHandlerLua script : scriptComponent.scripts) {
                    if (entity == contact.getFixtureA().getBody().getUserData()) {
                        script.call("beginContact", new LuaValue[]{
                            CoerceJavaToLua.coerce(new ScriptHandlerLua.EntityContext((Entity) contact.getFixtureB().getBody().getUserData(), ScriptSystem.this.entityEngine)),
                            CoerceJavaToLua.coerce(contact)
                        });
                    } else if (entity == contact.getFixtureB().getBody().getUserData()) {
                        script.call("beginContact", new LuaValue[]{
                            CoerceJavaToLua.coerce(new ScriptHandlerLua.EntityContext((Entity) contact.getFixtureA().getBody().getUserData(), ScriptSystem.this.entityEngine)),
                            CoerceJavaToLua.coerce(contact)
                        });
                    }
                }
            }
        }

        @Override
        public void endContact(Contact contact) {
            Family family = Family.all(ScriptComponent.class, PhysicsBodyComponent.class).get();

            for (Entity entity : entityEngine.getEntitiesFor(family)) {
                ScriptComponent scriptComponent = ScriptSystem.this.scriptComponentMapper.get(entity);
                for (ScriptHandlerLua script : scriptComponent.scripts) {
                    if (entity == contact.getFixtureA().getBody().getUserData()) {
                        script.call("endContact", new LuaValue[]{
                            CoerceJavaToLua.coerce(new ScriptHandlerLua.EntityContext((Entity) contact.getFixtureB().getBody().getUserData(), ScriptSystem.this.entityEngine)),
                            CoerceJavaToLua.coerce(contact)
                        });
                    } else if (entity == contact.getFixtureB().getBody().getUserData()) {
                        script.call("endContact", new LuaValue[]{
                            CoerceJavaToLua.coerce(new ScriptHandlerLua.EntityContext((Entity) contact.getFixtureA().getBody().getUserData(), ScriptSystem.this.entityEngine)),
                            CoerceJavaToLua.coerce(contact)
                        });
                    }
                }
            }
        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {
            Family family = Family.all(ScriptComponent.class, PhysicsBodyComponent.class).get();

            for (Entity entity : entityEngine.getEntitiesFor(family)) {
                ScriptComponent scriptComponent = ScriptSystem.this.scriptComponentMapper.get(entity);
                for (ScriptHandlerLua script : scriptComponent.scripts) {
                    if (entity == contact.getFixtureA().getBody().getUserData()) {
                        script.call("preSolve", new LuaValue[]{
                            CoerceJavaToLua.coerce(new ScriptHandlerLua.EntityContext((Entity) contact.getFixtureB().getBody().getUserData(), ScriptSystem.this.entityEngine)),
                            CoerceJavaToLua.coerce(contact),
                            CoerceJavaToLua.coerce(oldManifold)
                        });
                    } else if (entity == contact.getFixtureB().getBody().getUserData()) {
                        script.call("preSolve", new LuaValue[]{
                            CoerceJavaToLua.coerce(new ScriptHandlerLua.EntityContext((Entity) contact.getFixtureA().getBody().getUserData(), ScriptSystem.this.entityEngine)),
                            CoerceJavaToLua.coerce(contact),
                            CoerceJavaToLua.coerce(oldManifold)
                        });
                    }
                }
            }
        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {
            Family family = Family.all(ScriptComponent.class, PhysicsBodyComponent.class).get();

            for (Entity entity : entityEngine.getEntitiesFor(family)) {
                ScriptComponent scriptComponent = ScriptSystem.this.scriptComponentMapper.get(entity);
                for (ScriptHandlerLua script : scriptComponent.scripts) {
                    if (entity == contact.getFixtureA().getBody().getUserData()) {
                        script.call("postSolve", new LuaValue[]{
                            CoerceJavaToLua.coerce(new ScriptHandlerLua.EntityContext((Entity) contact.getFixtureB().getBody().getUserData(), ScriptSystem.this.entityEngine)),
                            CoerceJavaToLua.coerce(contact),
                            CoerceJavaToLua.coerce(impulse)
                        });
                    } else if (entity == contact.getFixtureB().getBody().getUserData()) {
                        script.call("postSolve", new LuaValue[]{
                            CoerceJavaToLua.coerce(new ScriptHandlerLua.EntityContext((Entity) contact.getFixtureA().getBody().getUserData(), ScriptSystem.this.entityEngine)),
                            CoerceJavaToLua.coerce(contact),
                            CoerceJavaToLua.coerce(impulse)
                        });
                    }
                }
            }
        }

    }
}
