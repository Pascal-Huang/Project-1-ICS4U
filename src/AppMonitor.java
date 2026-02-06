import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class AppMonitor {
   private ArrayList<String> blackList;
   ArrayList<String> ignoreTitles = new ArrayList<>();


    public AppMonitor(ArrayList<String> list){
        this.blackList = list;

        ignoreTitles.add("N/A");
        ignoreTitles.add("OleMainThreadWndName");   
        ignoreTitles.add("Input Trap");
        ignoreTitles.add("Notification");
        ignoreTitles.add("Updater");
    }
    
    public void addToBlackList(String app){
        blackList.add(app);
    }

    public void isAppRunningTest(List<String> blackList){
        String line;
        try{
            Process p = Runtime.getRuntime().exec("tasklist /v /fo csv /nh");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));


            while ((line = input.readLine()) != null) {
                for (int i = 0; i < blackList.size(); i++){
                    if (line.contains(blackList.get(i))){
                        if (isRealWindow(line)){
                            System.out.println(line);
                        }
                    }
                }
            }
            input.close();
        }
        catch(Exception e){
            System.out.println("Error Message");
        }
    }
    public boolean isRealWindow(String app){
        for (int i = 0; i < ignoreTitles.size(); i++){
            if (app.contains(ignoreTitles.get(i))){
                return false;
            }
        }
        return true;

    }
}



