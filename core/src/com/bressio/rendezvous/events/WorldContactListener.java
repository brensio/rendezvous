package com.bressio.rendezvous.events;

import com.badlogic.gdx.physics.box2d.*;
import com.bressio.rendezvous.entities.Enemy;
import com.bressio.rendezvous.entities.projectiles.Bullet;
import com.bressio.rendezvous.entities.tiles.InteractiveTile;
import com.bressio.rendezvous.scenes.Match;

import static com.bressio.rendezvous.scheme.PhysicsAdapter.*;

public class WorldContactListener implements ContactListener {

    private Match match;

    public WorldContactListener(Match match) {
        this.match = match;
    }

    private void sendContactMessage(Contact contact, boolean isEndingContact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        int combination = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch (combination){
            case PLAYER_TAG | BUILDING_TAG:
            case PLAYER_TAG | LOOT_TAG:
                if(fixtureA.getFilterData().categoryBits == PLAYER_TAG) {
                    if (!isEndingContact) {
                        ((InteractiveTile) fixtureB.getUserData()).onPlayerEnter();
                    } else {
                        ((InteractiveTile) fixtureB.getUserData()).onPlayerLeave();
                    }
                } else {
                    if (!isEndingContact) {
                        ((InteractiveTile) fixtureA.getUserData()).onPlayerEnter();
                    } else {
                        ((InteractiveTile) fixtureA.getUserData()).onPlayerLeave();
                    }
                }
                break;
            case BULLET_TAG | DEFAULT_TAG:
            case BULLET_TAG | LOOT_TAG:
                if(fixtureA.getFilterData().categoryBits == BULLET_TAG) {
                    ((Bullet) fixtureA.getUserData()).destroy();
                } else {
                    ((Bullet) fixtureB.getUserData()).destroy();
                }
                break;
            case ENEMY_TAG | LOOT_TAG:
                if(fixtureA.getFilterData().categoryBits == ENEMY_TAG) {
                    if (!isEndingContact) {
                        ((InteractiveTile) fixtureB.getUserData()).onEnemyEnter((Enemy)fixtureA.getUserData());
                    } else {
                        ((InteractiveTile) fixtureB.getUserData()).onEnemyLeave((Enemy)fixtureA.getUserData());
                    }
                } else {
                    if (!isEndingContact) {
                        ((InteractiveTile) fixtureA.getUserData()).onEnemyEnter((Enemy)fixtureB.getUserData());
                    } else {
                        ((InteractiveTile) fixtureA.getUserData()).onEnemyLeave((Enemy)fixtureB.getUserData());
                    }
                }
                break;
            case ENEMY_TAG | BULLET_TAG:
                if(fixtureA.getFilterData().categoryBits == ENEMY_TAG) {
                    if (!((Enemy) fixtureA.getUserData()).isDead() && !((Bullet) fixtureB.getUserData()).isDestroyed()) {
                        if (((Bullet) fixtureB.getUserData()).getWeapon().getLastSoldierToShoot() != fixtureA.getUserData()) {
                            ((Enemy) fixtureA.getUserData()).getShot((Bullet)fixtureB.getUserData());
                            ((Bullet) fixtureB.getUserData()).destroy();
                        }
                    }
                } else if (!((Enemy) fixtureB.getUserData()).isDead() && !((Bullet) fixtureA.getUserData()).isDestroyed()) {
                    if (((Bullet) fixtureA.getUserData()).getWeapon().getLastSoldierToShoot() != fixtureB.getUserData()) {
                        ((Enemy) fixtureB.getUserData()).getShot((Bullet)fixtureA.getUserData());
                        ((Bullet) fixtureA.getUserData()).destroy();
                    }
                }
                break;
            case PLAYER_TAG | BULLET_TAG:
                if(fixtureA.getFilterData().categoryBits == PLAYER_TAG) {
                    if (!match.getPlayer().isDead() && !((Bullet) fixtureB.getUserData()).isDestroyed()) {
                        if (((Bullet) fixtureB.getUserData()).getWeapon().getLastSoldierToShoot() != match.getPlayer()) {
                            match.getPlayer().getShot((Bullet)fixtureB.getUserData());
                            ((Bullet) fixtureB.getUserData()).destroy();
                        }
                    }
                } else if (!match.getPlayer().isDead() && !((Bullet) fixtureA.getUserData()).isDestroyed()) {
                    if (((Bullet) fixtureA.getUserData()).getWeapon().getLastSoldierToShoot() != match.getPlayer()) {
                        match.getPlayer().getShot((Bullet)fixtureA.getUserData());
                        ((Bullet) fixtureA.getUserData()).destroy();
                    }
                }
                break;
            case ENEMY_TAG | PLAYER_TAG:
                if(fixtureA.getFilterData().categoryBits == PLAYER_TAG) {
                    if (!isEndingContact) {
                        ((Enemy) fixtureB.getUserData()).onPlayerEnter();
                    } else {
                        ((Enemy) fixtureB.getUserData()).onPlayerLeave();
                    }
                } else {
                    if (!isEndingContact) {
                        ((Enemy) fixtureA.getUserData()).onPlayerEnter();
                    } else {
                        ((Enemy) fixtureA.getUserData()).onPlayerLeave();
                    }
                }
                break;
            case ENEMY_TAG:
                if(fixtureA.getFilterData().categoryBits == ENEMY_TAG) {
                    if (!isEndingContact) {
                        ((Enemy) fixtureB.getUserData()).onEnemyEnter((Enemy)fixtureA.getUserData());
                    } else {
                        ((Enemy) fixtureB.getUserData()).onEnemyLeave((Enemy)fixtureA.getUserData());
                    }
                } else {
                    if (!isEndingContact) {
                        ((Enemy) fixtureA.getUserData()).onEnemyEnter((Enemy)fixtureB.getUserData());
                    } else {
                        ((Enemy) fixtureA.getUserData()).onEnemyLeave((Enemy)fixtureB.getUserData());
                    }
                }
                break;
        }
    }

    @Override
    public void beginContact(Contact contact) {
        sendContactMessage(contact, false);
    }

    @Override
    public void endContact(Contact contact) {
        sendContactMessage(contact, true);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
