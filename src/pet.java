public class Pet {
    private int health;
    private long xp;
    private int level;
    private boolean isAlive;

    public Pet(int h, int x, int lvl, boolean isA){
        this.health = h;
        this.xp = x;
        this.level = lvl;
        this.isAlive = isA;
    }

    public int getHealth() {
        return health;
    }
    public long getXp() {
        return xp;
    }
    public long getRequiredXp(int lvl) {
        long base = 100;
        long increase = 50;

        return base + ((long)(lvl - 1) * increase);
    }
    public int getLevel() {
        return level;
    }
    public boolean isAlive() {
        return isAlive;
    }
    
    public void changeHealth(int change){
        this.health += change;
        if (this.health <= 0){
            this.health = 0;
            setAlive(false);
            System.out.println("Your pet has died :(");
        }
    }
    public void changeXp(int change){
        this.xp += change;
        long requiredXp = getRequiredXp(this.level + 1);
        while (xp >= requiredXp){
            levelUp();
            this.xp = this.xp - requiredXp;
            requiredXp = getRequiredXp(this.level + 1);
        }
        if (this.xp < 0){
            if (this.level > 1){
                levelDown();
                this.xp = getRequiredXp(this.level - 1) - 1;
            }
            else{
                this.xp = 0;
            }
            this.changeHealth(-20);
        }
    }
    public void levelUp(){
        this.level += 1;
        this.health = 100;
        System.out.println("Your pet has leveled up to level " + this.level + " (" + this.xp + " / " + getRequiredXp(this.level) + ")");
    }
    public void levelDown(){
        this.level -= 1;
        System.out.println("Your pet has leveled down to level " + this.level);
    }
    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    @Override
    public String toString() {
        return "Pet: Health=" + this.health + ", XP=" + this.xp + "/" + getRequiredXp(this.level + 1) + ", Level=" + this.level;
    }
}
