package com.bressio.rendezvous.entities.objects.weapons;

import com.badlogic.gdx.utils.Timer;
import com.bressio.rendezvous.entities.Soldier;
import com.bressio.rendezvous.entities.objects.EntityObject;
import com.bressio.rendezvous.entities.projectiles.Bullet;
import com.bressio.rendezvous.scenes.Match;

public abstract class Weapon extends EntityObject {

    private Soldier lastSoldierToShoot;
    private int damage;
    private float rateOfFire;
    private int reloadTime;
    private int magCapacity;
    private int accuracy;
    private int bullets;

    private float bulletTimeCount;
    private boolean isblocked;
    private boolean isUnblocking;

    public Weapon(Match match) {
        super(match);
    }

    public boolean transformSoldier(Soldier soldier) {
        soldier.getInventory().transferAmmo(bullets);
        return true;
    }

    public void shoot(Soldier soldier) {
        if (bullets > 0) {
            if (!isblocked) {
                lastSoldierToShoot = soldier;
                getMatch().addBullet(new Bullet(getMatch(), this));
                bullets--;
                bulletTimeCount = 0;
                isblocked = true;
                soldier.setFiring(true);
            }
            if (!isUnblocking){
                isUnblocking = true;

                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        soldier.setFiring(false);
                    }
                }, .01f);

                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        isblocked = false;
                        isUnblocking = false;
                    }
                }, rateOfFire);
            }
        }
    }

    public abstract Object getAmmoType();

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setRateOfFire(float rateOfFire) {
        this.rateOfFire = rateOfFire;
    }

    public void setReloadTime(int reloadTime) {
        this.reloadTime = reloadTime;
    }

    public void setMagCapacity(int magCapacity) {
        this.magCapacity = magCapacity;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public void setBullets(int bullets) {
        this.bullets = bullets;
    }

    public abstract float getTimeToTransform();

    public int getBullets() {
        return bullets;
    }

    public int getMagCapacity() {
        return magCapacity;
    }

    public int getDamage() {
        return damage;
    }

    public Soldier getLastSoldierToShoot() {
        return lastSoldierToShoot;
    }
}
