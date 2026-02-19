import java.util.ArrayList;
import java.util.List;

public class Main{
    public static void main(String[] args) throws Exception {
        ArrayList<String> blackList = new ArrayList<>(List.of("discord", "steam", "instagram", "valorant", "minecraft", "gemini")); //Starting list of a new profile

        Pet pet = new Pet(100, 0, 0, true);
        AppMonitor monitor = new AppMonitor(blackList, false);
        DataManager dataManager = new DataManager(pet, monitor);

        new TestGUI(pet, monitor, dataManager); 
    }
}

