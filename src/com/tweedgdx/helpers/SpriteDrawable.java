package com.tweedgdx.helpers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import com.tweedgdx.components.PositionComponent;


public class SpriteDrawable implements DrawableInterface {
    private Sprite sprite;
    private JsonValue instructions;

    public SpriteDrawable(JsonValue instructions){
        this.instructions = instructions;

        Texture texture = new Texture(Gdx.files.internal(this.instructions.getString("textureImageFile")));
        TextureRegion textureRegion = new TextureRegion(
            texture,
            this.instructions.getInt("textureX", 0),
            this.instructions.getInt("textureY", 0),
            this.instructions.getInt("textureWidth", 0),
            this.instructions.getInt("textureHeight", 0)
        );

        this.sprite = new Sprite(textureRegion);
    }

    @Override
    public void update(RenderBlockInterface renderBlock, Entity entity, float deltaTime){
        PositionComponent positionComponent = ((SpriteRenderBlock) renderBlock).positionComponentMapper.get(entity);

        // Set position and size.
        this.sprite.setBounds(
            (positionComponent.x - (this.instructions.getFloat("renderWidth", 0) / 2) + this.instructions.getFloat("renderOffsetX", 0)),
            (positionComponent.y - (this.instructions.getFloat("renderHeight", 0) / 2) + this.instructions.getFloat("renderOffsetY", 0)),
            this.instructions.getFloat("renderWidth", 0),
            this.instructions.getFloat("renderHeight", 0)
        );

        // Set rotation.
        this.sprite.setOriginCenter();
        this.sprite.setRotation(positionComponent.yaw);
    }

    @Override
    public void draw(RenderBlockInterface renderBlock) {
        this.sprite.draw(((SpriteRenderBlock) renderBlock).batch);
    }

}
