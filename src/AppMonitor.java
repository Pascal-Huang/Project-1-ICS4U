import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class AppMonitor {
    // Instance variables
   private ArrayList<String> blackList;
   private boolean deepWorkMode;
   ArrayList<String> ignoreTitles = new ArrayList<>(); //List of keywords to ignore background processes


    public AppMonitor(ArrayList<String> list, boolean deepWork){
        this.blackList = list;
        this.deepWorkMode = deepWork;

        ignoreTitles.add("n/a");
        ignoreTitles.add("olemainthreadwndname");   
        ignoreTitles.add("input trap");
        ignoreTitles.add("notification");
        ignoreTitles.add("updater");
    }

    /*Method that uses the command prompt to get a list of the running applications, ensures it is not a background process ,
    and then compares it to the black listed apps untill a detection is made or none at all*/ 
    public boolean isFocused(List<String> blackList){
        String line;
        try{
            Process p = Runtime.getRuntime().exec("tasklist /v /fo csv /nh"); //Command to get lid of all running apps
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream())); // Reads the output of command
            while ((line = input.readLine()) != null) {
                String lowerLine = line.toLowerCase();
                for (int i = 0; i < blackList.size(); i++){
                    if ((lowerLine.contains(blackList.get(i)) && isRealWindow(lowerLine))){ //Compare to blacklist
                        System.out.println("Detected " + lowerLine);
                        if (deepWorkMode){
                            closeApp(blackList.get(i)); // Closes app if in deep work mode
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
    public boolean isRealWindow(String app){ // Checks if it is not just a background process (prevents false detections)
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
    public String getTitle(String CSVLine){
        String title = CSVLine.split("\",\"")[0].replaceAll("\"", "") + CSVLine.split("\",\"")[8].replaceAll("\"", "");
        return title;
    }
    public ArrayList<String> getBlackList() {
        return blackList;
    }
    public boolean getDeepWork(){
        return deepWorkMode;
    }   
    public void setDeepWork(boolean mode){
        this.deepWorkMode = mode;
    }
    public void closeApp(String CSVLine){ // Closes selected app using command prompt
        String appName = CSVLine;
        try{
            Runtime.getRuntime().exec("taskkill /F /IM " + appName + ".exe");
            System.out.println("Closed " + appName);
        }
        catch(Exception e){
            System.out.println("Error closing app: " + e.getMessage());
        }
    }
    public void addToBlackList(String app){
        String program = app.toLowerCase();
        if (!blackList.contains(program)){
            blackList.add(program);
        }
    }
    // Used to prepare the app monitor, to ensure no false detections with background processes occurs
    public void closeBlackList(List<String> blackList){
        for (String app : blackList){
            closeApp(app);
        }
    }
}