import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class AppMonitor {
   private ArrayList<String> blackList;
   private boolean deepWorkMode;
   ArrayList<String> ignoreTitles = new ArrayList<>();


    public AppMonitor(ArrayList<String> list, boolean deepWork){
        this.blackList = list;
        this.deepWorkMode = deepWork;

        ignoreTitles.add("N/A");
        ignoreTitles.add("OleMainThreadWndName");   
        ignoreTitles.add("Input Trap");
        ignoreTitles.add("Notification");
        ignoreTitles.add("Updater");
    }
    
    public void addToBlackList(String app){
        blackList.add(app);
    }

    /*Method that uses the command prompt to get a list of the running applications, ensures it is not a background process ,
    and then comapres it to the black listed apps */ 
    public boolean isAppRunning(List<String> blackList){
        String line;
        try{
            Process p = Runtime.getRuntime().exec("tasklist /v /fo csv /nh");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));


            while ((line = input.readLine()) != null) {
                for (int i = 0; i < blackList.size(); i++){
                    if (line.contains(blackList.get(i)) && isRealWindow(line)){
                        System.out.println("Detected " + getFileName(line)  );
                        if (deepWorkMode){
                            closeApp(line);
                        }
                        return false;
                    }
                }
            }
            input.close();
        }
        catch(Exception e){
            System.out.println("Error Message");
        }
        return true;
    }
    
    public void isAppRunningTest(List<String> blackList){
        String line;
        try{
            Process p = Runtime.getRuntime().exec("tasklist /v /fo csv /nh");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

            while ((line = input.readLine()) != null) {
                for (int i = 0; i < blackList.size(); i++){
                    if (line.contains(blackList.get(i)) && isRealWindow(line)){
                        System.out.println("Detected " + line);
                        if (deepWorkMode){
                            closeApp(line);
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
    public String getFileName(String CSVLine){
        String fileName = CSVLine.split("\",\"")[0].replaceAll("\"", "");
        return fileName;
    }
    public void closeApp(String CSVLine){
        String appName = getFileName(CSVLine);
        try{
            Runtime.getRuntime().exec("taskkill /F /IM " + appName);
            System.out.println("Closed " + appName);
        }
        catch(Exception e){
            System.out.println("Error closing app: " + e.getMessage());
        }
    }
}



