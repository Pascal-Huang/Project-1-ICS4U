import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Which profile");
        int profileNumber = scanner.nextInt();
        scanner.close();

        ArrayList<String> blackList = new ArrayList<>(List.of("discord", "steam", "instagram", "valorant", "minecraft", "gemini"));
        // ArrayList<String> blackListedSites = new ArrayList<>(List.of("youtube", "gemini"));

        Pet pet = new Pet(100, 0, 0, true);
        AppMonitor monitor = new AppMonitor(blackList, false);
        DataManager dataManager = new DataManager(pet, profileNumber, monitor);
        new TestGUI(pet, monitor, dataManager);

       
    }
}

