import java.util.ArrayList;
import java.util.List;

public class Main{
    public static void main(String[] args) throws Exception {
        ArrayList<String> blackList = new ArrayList<>(List.of("Discord.exe", "steam.exe", "Spotify.exe", "Notepad.exe", "ONENOTE.EXE"));
        AppMonitor monitor = new AppMonitor(blackList, false);
        Pet pet = new Pet(100, 0, 0, true);
        boolean activeStudying = true; 
        boolean focused = true;

        System.out.println(pet.toString());
        closeBlackList(monitor, blackList);

        while (activeStudying){
            long startTime = System.currentTimeMillis();

            focused = monitor.isAppRunning(blackList);
            if (focused){
                pet.changeXp(1000);
            }
            else{
                pet.changeHealth(-5);
                pet.changeXp(-200);
            }
            System.out.println(pet.toString());
        }
    }

    // Used to prepare the app monitor, to ensure no false detections with background processes occurs
    public static void closeBlackList(AppMonitor monitor, List<String> blackList){
        for (String app : blackList){
            monitor.closeApp(app);
        }
    }
}

