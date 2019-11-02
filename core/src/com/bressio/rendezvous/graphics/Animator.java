package com.bressio.rendezvous.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bressio.rendezvous.entities.Entity;
import com.bressio.rendezvous.entities.Soldier;
import com.bressio.rendezvous.entities.objects.Empty;
import com.bressio.rendezvous.entities.objects.Medkit;
import com.bressio.rendezvous.entities.objects.equipment.armor.MilitaryVest;
import com.bressio.rendezvous.entities.objects.equipment.armor.SoftVest;
import com.bressio.rendezvous.entities.objects.equipment.helmets.CombatHelmet;
import com.bressio.rendezvous.entities.objects.equipment.helmets.HalfHelmet;
import com.bressio.rendezvous.entities.objects.weapons.ars.STAR;
import com.bressio.rendezvous.entities.objects.weapons.ars.W16A;
import com.bressio.rendezvous.entities.objects.weapons.pistols.G21;
import com.bressio.rendezvous.entities.objects.weapons.pistols.P26;
import com.bressio.rendezvous.entities.objects.weapons.srs.AW3;
import com.bressio.rendezvous.entities.objects.weapons.srs.M20;

public class Animator {

    private enum State {
        IDLE, MOVING
    }

    private Entity entity;
    private float stateTimer;
    private State currentState;
    private State previousState;
    private TextureRegion idleTexture;
    private KeyFrameIndexer indexer;
    private AnimationRegion animationRegion;

    public Animator(Entity entity, AnimationRegion animationRegion) {
        this.entity = entity;
        this.animationRegion = animationRegion;
        init();
        setupIdleTexture();
    }

    private void init() {
        currentState = State.IDLE;
        previousState = State.IDLE;
        stateTimer = 0;
        indexer = new KeyFrameIndexer(entity.getTexture(), animationRegion);
    }

    private void setupIdleTexture() {
        idleTexture = new TextureRegion(entity.getTexture(),
                animationRegion.getIdleTextureX(),
                animationRegion.getIdleTextureY(),
                animationRegion.getIdleTextureWidth(),
                animationRegion.getIdleTextureHeight());
        idleTexture.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public TextureRegion getIdleTexture() {
        return idleTexture;
    }

    public TextureRegion getFrame(float delta, float velocityThreshold) {
        currentState = getState(velocityThreshold);
        TextureRegion region;
        stateTimer = currentState == previousState ? stateTimer + delta : 0;
        switch (currentState) {
            case MOVING:
                region = indexer.getMovingAnimation().getKeyFrame(stateTimer, true);
                break;
            default:
                region = idleTexture;
                break;
        }
        previousState = currentState;
        return region;
    }

    private State getState(float velocityThreshold) {
        if (entity.getBody().getLinearVelocity().x > velocityThreshold ||
                entity.getBody().getLinearVelocity().x < -velocityThreshold ||
                entity.getBody().getLinearVelocity().y > velocityThreshold ||
                entity.getBody().getLinearVelocity().y < -velocityThreshold) {
            return State.MOVING;
        } else {
            return State.IDLE;
        }
    }

    public void verify(Object selectedAmorClass, Object selectedHelmetClass, Object selectedObjectClass) {
        if (selectedAmorClass == Empty.class && selectedHelmetClass == Empty.class) {
            defineNewAnimation(selectedObjectClass,
                    AnimationRegion.SOLDIER,
                    ResourceHandler.TextureAtlasPath.SOLDIER_ATLAS,
                    AnimationRegion.SOLDIER_MEDKIT,
                    ResourceHandler.TextureAtlasPath.SOLDIER_MEDKIT_ATLAS,
                    AnimationRegion.SOLDIER_STAR,
                    ResourceHandler.TextureAtlasPath.SOLDIER_STAR_ATLAS,
                    AnimationRegion.SOLDIER_W16A,
                    ResourceHandler.TextureAtlasPath.SOLDIER_W16A_ATLAS,
                    AnimationRegion.SOLDIER_G21,
                    ResourceHandler.TextureAtlasPath.SOLDIER_G21_ATLAS,
                    AnimationRegion.SOLDIER_P26,
                    ResourceHandler.TextureAtlasPath.SOLDIER_P26_ATLAS,
                    AnimationRegion.SOLDIER_AW3,
                    ResourceHandler.TextureAtlasPath.SOLDIER_AW3_ATLAS,
                    AnimationRegion.SOLDIER_M20,
                    ResourceHandler.TextureAtlasPath.SOLDIER_M20_ATLAS);
        } else if (selectedHelmetClass == Empty.class && selectedAmorClass == MilitaryVest.class) {
            defineNewAnimation(selectedObjectClass,
                    AnimationRegion.SOLDIER_MILITARY_VEST,
                    ResourceHandler.TextureAtlasPath.SOLDIER_MILITARY_VEST_ATLAS,
                    AnimationRegion.SOLDIER_MV_MEDKIT,
                    ResourceHandler.TextureAtlasPath.SOLDIER_MV_MEDKIT_ATLAS,
                    AnimationRegion.SOLDIER_MV_STAR,
                    ResourceHandler.TextureAtlasPath.SOLDIER_MV_STAR_ATLAS,
                    AnimationRegion.SOLDIER_MV_W16A,
                    ResourceHandler.TextureAtlasPath.SOLDIER_MV_W16A_ATLAS,
                    AnimationRegion.SOLDIER_MV_G21,
                    ResourceHandler.TextureAtlasPath.SOLDIER_MV_G21_ATLAS,
                    AnimationRegion.SOLDIER_MV_P26,
                    ResourceHandler.TextureAtlasPath.SOLDIER_MV_P26_ATLAS,
                    AnimationRegion.SOLDIER_MV_AW3,
                    ResourceHandler.TextureAtlasPath.SOLDIER_MV_AW3_ATLAS,
                    AnimationRegion.SOLDIER_MV_M20,
                    ResourceHandler.TextureAtlasPath.SOLDIER_MV_M20_ATLAS);
        } else if (selectedHelmetClass == Empty.class && selectedAmorClass == SoftVest.class) {
            defineNewAnimation(selectedObjectClass,
                    AnimationRegion.SOLDIER_SOFT_VEST,
                    ResourceHandler.TextureAtlasPath.SOLDIER_SOFT_VEST_ATLAS,
                    AnimationRegion.SOLDIER_SV_MEDKIT,
                    ResourceHandler.TextureAtlasPath.SOLDIER_SV_MEDKIT_ATLAS,
                    AnimationRegion.SOLDIER_SV_STAR,
                    ResourceHandler.TextureAtlasPath.SOLDIER_SV_STAR_ATLAS,
                    AnimationRegion.SOLDIER_SV_W16A,
                    ResourceHandler.TextureAtlasPath.SOLDIER_SV_W16A_ATLAS,
                    AnimationRegion.SOLDIER_SV_G21,
                    ResourceHandler.TextureAtlasPath.SOLDIER_SV_G21_ATLAS,
                    AnimationRegion.SOLDIER_SV_P26,
                    ResourceHandler.TextureAtlasPath.SOLDIER_SV_P26_ATLAS,
                    AnimationRegion.SOLDIER_SV_AW3,
                    ResourceHandler.TextureAtlasPath.SOLDIER_SV_AW3_ATLAS,
                    AnimationRegion.SOLDIER_SV_M20,
                    ResourceHandler.TextureAtlasPath.SOLDIER_SV_M20_ATLAS);
        } else if (selectedHelmetClass == CombatHelmet.class && selectedAmorClass == Empty.class) {
            defineNewAnimation(selectedObjectClass,
                    AnimationRegion.SOLDIER_COMBAT_HELMET,
                    ResourceHandler.TextureAtlasPath.SOLDIER_COMBAT_HELMET_ATLAS,
                    AnimationRegion.SOLDIER_CT_MEDKIT,
                    ResourceHandler.TextureAtlasPath.SOLDIER_CT_MEDKIT_ATLAS,
                    AnimationRegion.SOLDIER_CT_STAR,
                    ResourceHandler.TextureAtlasPath.SOLDIER_CT_STAR_ATLAS,
                    AnimationRegion.SOLDIER_CT_W16A,
                    ResourceHandler.TextureAtlasPath.SOLDIER_CT_W16A_ATLAS,
                    AnimationRegion.SOLDIER_CT_G21,
                    ResourceHandler.TextureAtlasPath.SOLDIER_CT_G21_ATLAS,
                    AnimationRegion.SOLDIER_CT_P26,
                    ResourceHandler.TextureAtlasPath.SOLDIER_CT_P26_ATLAS,
                    AnimationRegion.SOLDIER_CT_AW3,
                    ResourceHandler.TextureAtlasPath.SOLDIER_CT_AW3_ATLAS,
                    AnimationRegion.SOLDIER_CT_M20,
                    ResourceHandler.TextureAtlasPath.SOLDIER_CT_M20_ATLAS);
        } else if (selectedHelmetClass == HalfHelmet.class && selectedAmorClass == Empty.class) {
            defineNewAnimation(selectedObjectClass,
                    AnimationRegion.SOLDIER_HALF_HELMET,
                    ResourceHandler.TextureAtlasPath.SOLDIER_HALF_HELMET_ATLAS,
                    AnimationRegion.SOLDIER_HT_MEDKIT,
                    ResourceHandler.TextureAtlasPath.SOLDIER_HT_MEDKIT_ATLAS,
                    AnimationRegion.SOLDIER_HT_STAR,
                    ResourceHandler.TextureAtlasPath.SOLDIER_HT_STAR_ATLAS,
                    AnimationRegion.SOLDIER_HT_W16A,
                    ResourceHandler.TextureAtlasPath.SOLDIER_HT_W16A_ATLAS,
                    AnimationRegion.SOLDIER_HT_G21,
                    ResourceHandler.TextureAtlasPath.SOLDIER_HT_G21_ATLAS,
                    AnimationRegion.SOLDIER_HT_P26,
                    ResourceHandler.TextureAtlasPath.SOLDIER_HT_P26_ATLAS,
                    AnimationRegion.SOLDIER_HT_AW3,
                    ResourceHandler.TextureAtlasPath.SOLDIER_HT_AW3_ATLAS,
                    AnimationRegion.SOLDIER_HT_M20,
                    ResourceHandler.TextureAtlasPath.SOLDIER_HT_M20_ATLAS);
        } else if (selectedHelmetClass == CombatHelmet.class && selectedAmorClass == MilitaryVest.class) {
            defineNewAnimation(selectedObjectClass,
                    AnimationRegion.SOLDIER_MV_COMBAT_HELMET,
                    ResourceHandler.TextureAtlasPath.SOLDIER_MV_COMBAT_HELMET_ATLAS,
                    AnimationRegion.SOLDIER_MV_CT_MEDKIT,
                    ResourceHandler.TextureAtlasPath.SOLDIER_MV_CT_MEDKIT_ATLAS,
                    AnimationRegion.SOLDIER_MV_CT_STAR,
                    ResourceHandler.TextureAtlasPath.SOLDIER_MV_CT_STAR_ATLAS,
                    AnimationRegion.SOLDIER_MV_CT_W16A,
                    ResourceHandler.TextureAtlasPath.SOLDIER_MV_CT_W16A_ATLAS,
                    AnimationRegion.SOLDIER_MV_CT_G21,
                    ResourceHandler.TextureAtlasPath.SOLDIER_MV_CT_G21_ATLAS,
                    AnimationRegion.SOLDIER_MV_CT_P26,
                    ResourceHandler.TextureAtlasPath.SOLDIER_MV_CT_P26_ATLAS,
                    AnimationRegion.SOLDIER_MV_CT_AW3,
                    ResourceHandler.TextureAtlasPath.SOLDIER_MV_CT_AW3_ATLAS,
                    AnimationRegion.SOLDIER_MV_CT_M20,
                    ResourceHandler.TextureAtlasPath.SOLDIER_MV_CT_M20_ATLAS);
        } else if (selectedHelmetClass == HalfHelmet.class && selectedAmorClass == MilitaryVest.class) {
            defineNewAnimation(selectedObjectClass,
                    AnimationRegion.SOLDIER_MV_HALF_HELMET,
                    ResourceHandler.TextureAtlasPath.SOLDIER_MV_HALF_HELMET_ATLAS,
                    AnimationRegion.SOLDIER_MV_HT_MEDKIT,
                    ResourceHandler.TextureAtlasPath.SOLDIER_MV_HT_MEDKIT_ATLAS,
                    AnimationRegion.SOLDIER_MV_HT_STAR,
                    ResourceHandler.TextureAtlasPath.SOLDIER_MV_HT_STAR_ATLAS,
                    AnimationRegion.SOLDIER_MV_HT_W16A,
                    ResourceHandler.TextureAtlasPath.SOLDIER_MV_HT_W16A_ATLAS,
                    AnimationRegion.SOLDIER_MV_HT_G21,
                    ResourceHandler.TextureAtlasPath.SOLDIER_MV_HT_G21_ATLAS,
                    AnimationRegion.SOLDIER_MV_HT_P26,
                    ResourceHandler.TextureAtlasPath.SOLDIER_MV_HT_P26_ATLAS,
                    AnimationRegion.SOLDIER_MV_HT_AW3,
                    ResourceHandler.TextureAtlasPath.SOLDIER_MV_HT_AW3_ATLAS,
                    AnimationRegion.SOLDIER_MV_HT_M20,
                    ResourceHandler.TextureAtlasPath.SOLDIER_MV_HT_M20_ATLAS);
        } else if (selectedHelmetClass == CombatHelmet.class && selectedAmorClass == SoftVest.class) {
            defineNewAnimation(selectedObjectClass,
                    AnimationRegion.SOLDIER_SV_COMBAT_HELMET,
                    ResourceHandler.TextureAtlasPath.SOLDIER_SV_COMBAT_HELMET_ATLAS,
                    AnimationRegion.SOLDIER_SV_CT_MEDKIT,
                    ResourceHandler.TextureAtlasPath.SOLDIER_SV_CT_MEDKIT_ATLAS,
                    AnimationRegion.SOLDIER_SV_CT_STAR,
                    ResourceHandler.TextureAtlasPath.SOLDIER_SV_CT_STAR_ATLAS,
                    AnimationRegion.SOLDIER_SV_CT_W16A,
                    ResourceHandler.TextureAtlasPath.SOLDIER_SV_CT_W16A_ATLAS,
                    AnimationRegion.SOLDIER_SV_CT_G21,
                    ResourceHandler.TextureAtlasPath.SOLDIER_SV_CT_G21_ATLAS,
                    AnimationRegion.SOLDIER_SV_CT_P26,
                    ResourceHandler.TextureAtlasPath.SOLDIER_SV_CT_P26_ATLAS,
                    AnimationRegion.SOLDIER_SV_CT_AW3,
                    ResourceHandler.TextureAtlasPath.SOLDIER_SV_CT_AW3_ATLAS,
                    AnimationRegion.SOLDIER_SV_CT_M20,
                    ResourceHandler.TextureAtlasPath.SOLDIER_SV_CT_M20_ATLAS);
        } else if (selectedHelmetClass == HalfHelmet.class && selectedAmorClass == SoftVest.class) {
            defineNewAnimation(selectedObjectClass,
                    AnimationRegion.SOLDIER_SV_HALF_HELMET,
                    ResourceHandler.TextureAtlasPath.SOLDIER_SV_HALF_HELMET_ATLAS,
                    AnimationRegion.SOLDIER_SV_HT_MEDKIT,
                    ResourceHandler.TextureAtlasPath.SOLDIER_SV_HT_MEDKIT_ATLAS,
                    AnimationRegion.SOLDIER_SV_HT_STAR,
                    ResourceHandler.TextureAtlasPath.SOLDIER_SV_HT_STAR_ATLAS,
                    AnimationRegion.SOLDIER_SV_HT_W16A,
                    ResourceHandler.TextureAtlasPath.SOLDIER_SV_HT_W16A_ATLAS,
                    AnimationRegion.SOLDIER_SV_HT_G21,
                    ResourceHandler.TextureAtlasPath.SOLDIER_SV_HT_G21_ATLAS,
                    AnimationRegion.SOLDIER_SV_HT_P26,
                    ResourceHandler.TextureAtlasPath.SOLDIER_SV_HT_P26_ATLAS,
                    AnimationRegion.SOLDIER_SV_HT_AW3,
                    ResourceHandler.TextureAtlasPath.SOLDIER_SV_HT_AW3_ATLAS,
                    AnimationRegion.SOLDIER_SV_HT_M20,
                    ResourceHandler.TextureAtlasPath.SOLDIER_SV_HT_M20_ATLAS);
        }
    }

    private void defineNewAnimation(Object selectedObjectClass,
                                    AnimationRegion baseAnim, ResourceHandler.TextureAtlasPath base,
                                    AnimationRegion medkitAnim, ResourceHandler.TextureAtlasPath medkit,
                                    AnimationRegion starAnim, ResourceHandler.TextureAtlasPath star,
                                    AnimationRegion w16aAnim, ResourceHandler.TextureAtlasPath w16a,
                                    AnimationRegion g21Anim, ResourceHandler.TextureAtlasPath g21,
                                    AnimationRegion p26Anim, ResourceHandler.TextureAtlasPath p26,
                                    AnimationRegion aw3Anim, ResourceHandler.TextureAtlasPath aw3,
                                    AnimationRegion m20Anim, ResourceHandler.TextureAtlasPath m20) {
        if (selectedObjectClass == Empty.class) {
            ((Soldier)entity).switchAnimation(baseAnim, base);
        } else if (selectedObjectClass == Medkit.class) {
            ((Soldier)entity).switchAnimation(medkitAnim, medkit);
        } else if (selectedObjectClass == STAR.class) {
            ((Soldier)entity).switchAnimation(starAnim, star);
        } else if (selectedObjectClass == W16A.class) {
            ((Soldier)entity).switchAnimation(w16aAnim, w16a);
        } else if (selectedObjectClass == G21.class) {
            ((Soldier)entity).switchAnimation(g21Anim, g21);
        } else if (selectedObjectClass == P26.class) {
            ((Soldier)entity).switchAnimation(p26Anim, p26);
        } else if (selectedObjectClass == AW3.class) {
            ((Soldier)entity).switchAnimation(aw3Anim, aw3);
        } else if (selectedObjectClass == M20.class) {
            ((Soldier)entity).switchAnimation(m20Anim, m20);
        } else {
            ((Soldier)entity).switchAnimation(baseAnim, base);
        }
    }
}
