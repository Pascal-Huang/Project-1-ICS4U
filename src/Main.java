import java.util.ArrayList;
import java.util.List;

public class Main{
    public static void main(String[] args) throws Exception {
        ArrayList<String> blackList = new ArrayList<>(List.of("Discord.exe", "steam.exe", "Spotify.exe", "Notepad.exe", "ONENOTE.EXE"));
        AppMonitor monitor = new AppMonitor(blackList, true);
        pet pet = new pet(100, 0, 0, true);

        closeBlackList(monitor, blackList);
        // monitor.isAppRunningTest(blackList);
    }

    // Used to prepare the app monitor, to ensure no false detections with background processes occurs
    public static void closeBlackList(AppMonitor monitor, List<String> blackList){
        for (String app : blackList){
            monitor.closeApp(app);
        }
    }
}

