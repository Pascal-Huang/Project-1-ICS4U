public class pet {
    private int health;
    private int xp;
    private int level;
    private boolean isAlive;

    public pet(int h, int x, int lvl, boolean isA){
        this.health = h;
        this.xp = x;
        this.level = lvl;
        this.isAlive = isA;
    }

    public int getHealth() {
        return health;
    }
    public int getXp() {
        return xp;
    }
    public int getLevel() {
        return level;
    }
    public boolean isAlive() {
        return isAlive;
    }
}
