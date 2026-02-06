import java.util.ArrayList;
import java.util.List;

public class Main{
    public static void main(String[] args) throws Exception {
        ArrayList<String> blackList = new ArrayList<>(List.of("Discord.exe", "chrome.exe", "steam.exe", "Spotify.exe", "Notepad.exe"));
        AppMonitor monitor = new AppMonitor(blackList);

        monitor.isAppRunningTest(blackList);

        // for (int i = 0; i < appList.length; i ++){
        //     if (monitor.isAppRunning(appList[i])){
        //         System.out.println("Detected: " + appList[i]);
        //     }
        // }
    }
}

