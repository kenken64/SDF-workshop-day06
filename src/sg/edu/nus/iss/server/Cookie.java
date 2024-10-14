package sg.edu.nus.iss.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Cookie {
    private static List<String> cookies;
    public static void initAllCookies(String cookieFilepath) throws IOException{
        cookies = getDataFromText(cookieFilepath);
    }

    public static String getRandomCookie() {
        String randomCookie = "";
        if(cookies.size() > 0){
            Random rand = new Random();
            int randVal = rand.nextInt(cookies.size());
            randomCookie = cookies.get(randVal);
            System.out.println("RANDOM COOKIE >> " + randomCookie);
        }else{
            System.err.println("No cookies in the cookie file");
        } 
        
        return randomCookie;
    }

    public static List<String> getDataFromText(String filepath) throws IOException {
        List<String> lists = new LinkedList<>();
        System.out.println(filepath);
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lists.add(line);
            }
        }
        System.out.println(lists);
        return lists;
    }
}