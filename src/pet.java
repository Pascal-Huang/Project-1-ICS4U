public class Pet {
    //instance variables
    private String name;
    private int health;
    private long xp;
    private int level;
    private boolean isAlive;
    private long xpIncrease = 50;
    private long requirementIncrease = 0;

    public Pet(int h, int x, int lvl, boolean isA){
        this.name = "Pet"; //Feature not included yet   
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
    public long getRequiredXp(int lvl) { //Math to calculate required xp for a given level (increases)
        long base = 100;
        xpIncrease = 50;

        return base + ((long)(lvl - 1) * (xpIncrease + requirementIncrease));
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
    //Handles changes in xp, including level up and down and health decrease on xp loss
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

    public void setHealth(int health) {
        this.health = health;
    }
    public void setXp(long xp) {
        this.xp = xp;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public void setAlive(boolean alive) {
        isAlive = alive;
    }
    public void setXpIncrease(int xp){
        xpIncrease = xp;
    }
    public void setRequirementIncrease(int xp){
        requirementIncrease = xp;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "Pet: Health=" + this.health + ", XP=" + this.xp + "/" + getRequiredXp(this.level + 1) + ", Level=" + this.level;
    }
}
