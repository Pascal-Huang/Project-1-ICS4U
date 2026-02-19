import java.io.*;
import java.util.Arrays;

public class DataManager {
    // Textfiles for data storage
    private final String profile1 = "profile1.txt";
    private final String profile2 = "profile2.txt";
    private String filePath;
    private int profileNumber;

    // References to main objects
    private Pet pet;
    private AppMonitor monitor;

    public DataManager(Pet pet, AppMonitor monitor){
        this.pet = pet;
        this.monitor = monitor;

    }

    // Saves pet and blacklist data to a text file (line 1 = pet data, line 2 = blacklisted apps)
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
    // Takes data from text files and updates pet and blacklist data objects
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
    // Sets pet data to default values for when pet dies / user wants to reset
    public void loadDefaultData(){
        pet.setHealth(100);
        pet.setXp(0);
        pet.setLevel(0);
        pet.setAlive(true);
        saveData();
    }
    public void setFilePath(int profile){
        if (profile == 1){
            this.filePath = profile1;
        }
        else if (profile == 2){
            this.filePath = profile2;
        }
    }
}
