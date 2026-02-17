import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DataManager {
    private final String profile1 = "profile1.txt";
    private final String profile2 = "profile2.txt";
    private final String filePath;
    private int profileNumber;
    private Pet pet;
    private AppMonitor monitor;

    public DataManager(Pet pet, int profile, AppMonitor monitor){
        this.pet = pet;
        this.profileNumber = profile;
        this.monitor = monitor;

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
    
    public void saveData(){
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(filePath));
            writer.println(pet.getHealth() + "," + pet.getXp() + "," + pet.getLevel() + "," + pet.isAlive()); //Line 1 (pet data)
            writer.println(String.join(",", monitor.getBlackList())); //Line 2 (blacklisted apps)

            writer.close();
        }
        catch(IOException e){
            System.out.println("Error saving data " + e.getMessage());
        }
    }   
    public void loadData(){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line1 = reader.readLine();

            String[] data = line1.split(",");
            pet.setHealth(Integer.parseInt(data[0]));
            pet.setXp(Long.parseLong(data[1]));
            pet.setLevel(Integer.parseInt(data[2]));
            pet.setAlive(Boolean.parseBoolean(data[3]));

            String line2 = reader.readLine();
            if (line2 != null && !line2.isEmpty()) {
                String[] blackListData = line2.split(",");
                monitor.getBlackList().clear(); // Clear existing list before loading
                monitor.getBlackList().addAll(Arrays.asList(blackListData));
            }

            System.out.println("Data loaded successfully.");
            reader.close();
        }
        catch(IOException e){
            System.out.println("Error loading data " + e.getMessage());
        }
    }
}
