package com.bressio.rendezvous.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

class KeyFrameIndexer {

    private Animation<TextureRegion> movingAnimation;
    private AnimationRegion animationRegion;
    private Texture texture;

    KeyFrameIndexer(Texture texture, AnimationRegion animationRegion) {
        this.texture = texture;
        this.animationRegion = animationRegion;
        init();
    }

    private void init() {
        Array<TextureRegion> frames = new Array<>();
        for (int i = animationRegion.getStartColumn(); i <= animationRegion.getAmountFrames(); i++) {
            frames.add(new TextureRegion(texture, i * animationRegion.getFrameWidth(), animationRegion.getStartRow(),
                    animationRegion.getFrameWidth(), animationRegion.getFrameHeight()));
        }
        movingAnimation = new Animation<>(animationRegion.getFrameDuration(), frames);
        frames.clear();
    }

    Animation<TextureRegion> getMovingAnimation() {
        return movingAnimation;
    }
}
