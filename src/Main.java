/*
This program is a gamified productivity tool that incentivizes studying by linking user focus to the 
health and levelof a virtual pet. The application monitors active windows in real-time, 
rewarding the user with XP for staying on task while penalizing them if they open 
distracting applications that they set themselves.
 */
import java.util.ArrayList;
import java.util.List;

public class Main{
    public static void main(String[] args) throws Exception {
        //Initialization of objects, default blacklist and GUI
        ArrayList<String> blackList = new ArrayList<>(List.of("discord", "steam", "instagram", "valorant", "minecraft", "gemini")); //Starting list of a new profile

        Pet pet = new Pet(100, 0, 0, true);
        AppMonitor monitor = new AppMonitor(blackList, false);
        DataManager dataManager = new DataManager(pet, monitor);

        new TestGUI(pet, monitor, dataManager); 
    }
}

