import java.io.*;

public class DataManager {
    private final String profile1 = "profile1.txt";
    private final String profile2 = "profile2.txt";
    private final String filePath;
    private int profileNumber;
    private Pet pet;

    public DataManager(Pet pet, int profile){
        this.pet = pet;
        this.profileNumber = profile;
        if (profile == 1){
            filePath = profile1;
        }
        else if (profile == 2){
            filePath = profile2;
        }
        else{
            throw new IllegalArgumentException("Invalid profile number. Must be 1 or 2.");
        }
    }
    
    public void savePetData(){
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(filePath));
            writer.println(pet.getHealth() + "," + pet.getXp() + "," + pet.getLevel() + "," + pet.isAlive());
            writer.close();
        }
        catch(IOException e){
            System.out.println("Error saving data " + e.getMessage());
            System.out.println("Hello");
        }
    }   
    public void loadPetData(){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();

            String[] data = line.split(",");
            pet.setHealth(Integer.parseInt(data[0]));
            pet.setXp(Long.parseLong(data[1]));
            pet.setLevel(Integer.parseInt(data[2]));
            pet.setAlive(Boolean.parseBoolean(data[3]));

            System.out.println("Data loaded successfully.");

            reader.close();
        }
        catch(IOException e){
            System.out.println("Error loading data " + e.getMessage());
        }
    }
}
