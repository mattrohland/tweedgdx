package com.tweedgdx.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.tweedgdx.components.AliasComponent;
import com.tweedgdx.components.PhysicsBodyComponent;
import com.tweedgdx.components.PositionComponent;
import com.tweedgdx.helpers.BaseShapesFactory;


public class PhysicsSystem extends IteratingSystem{

    public World world;

    private ComponentMapper<AliasComponent> aliasComponentMapper = ComponentMapper.getFor(AliasComponent.class);
    private ComponentMapper<PositionComponent> positionComponentMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<PhysicsBodyComponent> physicsBodyComponentMapper = ComponentMapper.getFor(PhysicsBodyComponent.class);

    private PhysicsEntityListener physicsEntityListener;
    private static final float worldTimeStep = 1 / 30f;
    private float worldAccumulator = 0f;

    public PhysicsSystem(){
        super(Family.all(PhysicsBodyComponent.class).get());
    }

    @Override
    public void addedToEngine(Engine entityEngine){
        super.addedToEngine(entityEngine);

        this.world = new World(new Vector2(0, 0), true);
        this.physicsEntityListener = new PhysicsEntityListener();

        // Start Listening
        entityEngine.addEntityListener(100, this.physicsEntityListener);
    }

    @Override
    public void removedFromEngine(Engine entityEngine){
        super.removedFromEngine(entityEngine);

        entityEngine.removeEntityListener(this.physicsEntityListener);
        this.world.dispose();
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);

        // Stabalize world steps
        this.worldAccumulator += deltaTime;

        while(this.worldAccumulator >= deltaTime){
            this.world.step(this.worldTimeStep, 6, 2);
            this.worldAccumulator -= this.worldTimeStep;
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime){
        AliasComponent aliasComponent = this.aliasComponentMapper.get(entity);
        PhysicsBodyComponent physicsBodyComponent = this.physicsBodyComponentMapper.get(entity);
        PositionComponent positionComponent = this.positionComponentMapper.get(entity);

        // Update positions
        if(positionComponent != null){
            positionComponent.x = physicsBodyComponent.body.getPosition().x;
            positionComponent.y = physicsBodyComponent.body.getPosition().y;
            positionComponent.yaw = physicsBodyComponent.body.getAngle() * MathUtils.radiansToDegrees;
        }
    }

    /*
    * Private Methods
    */

    private final class BodyTypes{
        public static final String DYNAMIC = "DYNAMIC";
        public static final String KINEMATIC = "KINEMATIC";
        public static final String STATIC = "STATIC";

        private BodyTypes(){}
    }

    private final class Shapes{
        public static final String QUADRILATERAL = "QUADRILATERAL";

        private Shapes(){}
    }

    private Body createBodyForPhysicalBodyComponent(PhysicsBodyComponent physicsBodyComponent, PositionComponent positionComponent){
        BodyDef bodyDef = new BodyDef();

        if(physicsBodyComponent.type.equals(BodyTypes.DYNAMIC)){
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        }else if(physicsBodyComponent.type.equals(BodyTypes.KINEMATIC)){
            bodyDef.type = BodyDef.BodyType.StaticBody;
        }else if(physicsBodyComponent.type.equals(BodyTypes.STATIC)){
            bodyDef.type = BodyDef.BodyType.StaticBody;
        }

        // Set Body Position
        bodyDef.position.set(new Vector2(positionComponent.x, positionComponent.y));

        physicsBodyComponent.body = world.createBody(bodyDef);
        createFixtureForPhysicalBodyComponent(physicsBodyComponent);

        return physicsBodyComponent.body;
    }

    private Fixture createFixtureForPhysicalBodyComponent(PhysicsBodyComponent physicsBodyComponent){
        FixtureDef fixtureDef = new FixtureDef();

        Shape shape = this.createShapeForPhysicalBodyComponent(physicsBodyComponent);

        fixtureDef.shape = shape;
        fixtureDef.density = physicsBodyComponent.density;
        fixtureDef.friction = physicsBodyComponent.friction;
        fixtureDef.restitution = physicsBodyComponent.restitution;

        Fixture fixture = physicsBodyComponent.body.createFixture(fixtureDef);

        shape.dispose();

        return fixture;
    }

    private Shape createShapeForPhysicalBodyComponent(PhysicsBodyComponent physicsBodyComponent){
        Shape shape = null;

        if(physicsBodyComponent.shape.equals(Shapes.QUADRILATERAL)){
            shape = BaseShapesFactory.createQuadrilateral(physicsBodyComponent.width, physicsBodyComponent.height);
        }

        return shape;
    }

    private class PhysicsEntityListener implements EntityListener{
        public void entityAdded(Entity entity) {
            PositionComponent positionComponent;
            PhysicsBodyComponent physicsBodyComponent;

            if(positionComponentMapper.has(entity) && physicsBodyComponentMapper.has(entity)){
                positionComponent = positionComponentMapper.get(entity);

                physicsBodyComponent = physicsBodyComponentMapper.get(entity);
                createBodyForPhysicalBodyComponent(physicsBodyComponent, positionComponent);
            }
        }

        public void entityRemoved(Entity entity){}
    }
}
