import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class AppMonitor {
   private ArrayList<String> blackList;
   private boolean deepWorkMode;
   ArrayList<String> ignoreTitles = new ArrayList<>();


    public AppMonitor(ArrayList<String> list,boolean deepWork){
        this.blackList = list;
        this.deepWorkMode = deepWork;

        ignoreTitles.add("n/a");
        ignoreTitles.add("olemainthreadwndname");   
        ignoreTitles.add("input trap");
        ignoreTitles.add("notification");
        ignoreTitles.add("updater");
    }

    /*Method that uses the command prompt to get a list of the running applications, ensures it is not a background process ,
    and then comapres it to the black listed apps */ 
    public boolean isFocused(List<String> blackList){
        String line;
        try{
            Process p = Runtime.getRuntime().exec("tasklist /v /fo csv /nh");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));


            while ((line = input.readLine()) != null) {
                String lowerLine = line.toLowerCase();
                for (int i = 0; i < blackList.size(); i++){
                    if ((lowerLine.contains(blackList.get(i)) && isRealWindow(lowerLine))){
                        System.out.println("Detected " + lowerLine);
                        if (deepWorkMode){
                            closeApp(lowerLine);
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
    public void closeApp(String CSVLine){
        String appName = getFileName(CSVLine);
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



