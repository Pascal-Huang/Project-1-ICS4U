import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Which profile");
        int profileNumber = scanner.nextInt();
        scanner.close();

        ArrayList<String> blackList = new ArrayList<>(List.of("discord.exe", "steam.exe", "spotify.exe", "notepad.exe", "onenote.exe"));
        ArrayList<String> blackListedSites = new ArrayList<>(List.of("youtube", "gemini"));

        Pet pet = new Pet(100, 0, 0, true);
        AppMonitor monitor = new AppMonitor(blackList, blackListedSites, false);
        DataManager dataManager = new DataManager(pet, profileNumber);
        new GUI(pet, monitor, dataManager);

        boolean activeStudying = true; 
        boolean focused = true;

        // closeBlackList(monitor, blackList);
        // dataManager.loadPetData();

    //     System.out.println(pet.toString());
    //     while (activeStudying){
    //         // long startTime = System.currentTimeMillis();

    //         focused = monitor.isFocused(blackList);
    //         if (focused){
    //             pet.changeXp(1000);
    //         }
    //         else{
    //             pet.changeHealth(-5);
    //             pet.changeXp(-200);
    //         }
    //         dataManager.savePetData();
    //     }
    }

    // Used to prepare the app monitor, to ensure no false detections with background processes occurs
    public static void closeBlackList(AppMonitor monitor, List<String> blackList){
        for (String app : blackList){
            monitor.closeApp(app);
        }
    }
}

