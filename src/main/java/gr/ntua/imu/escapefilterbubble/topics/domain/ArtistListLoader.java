/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.imu.escapefilterbubble.topics.domain;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kostas
 */
public class ArtistListLoader {
    private String ArtistListFilePath;
    private HashMap<Integer, String> artistMap;
    //private HashMap<Integer,String> usersMap;

    public ArtistListLoader(String ArtistListFilePath) {
        this.ArtistListFilePath = ArtistListFilePath;
    }

    public HashMap<Integer, String> createHashMap() throws FileNotFoundException, IOException {
        artistMap = new HashMap<Integer, String>();
        BufferedReader reader;
        File inputFile = new File(ArtistListFilePath);
        int lineNum = 0;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"), 8192);

            reader.readLine();
            String line = reader.readLine();
            while (line != null) {
                String[] fields = line.split(",");
                artistMap.put(Integer.parseInt(fields[0]), fields[1]);
                line = reader.readLine();
                lineNum++;
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TagListLoader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception e) {
            System.out.println("line number: " + lineNum);
            e.printStackTrace();
            throw new IOException("problem");
        }
        return artistMap;
    }

//    public HashMap<Integer, String> createUserHashMap(HashMap<Integer, String> tags, String usertagspath) throws FileNotFoundException, IOException {
//        usersMap = new HashMap<Integer, String>();
//        BufferedReader reader;
//        File inputFile = new File(usertagspath);
//        try {
//            reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"), 8192);
//
//            reader.readLine();
//            String line = reader.readLine();
//            while (line != null) {
//                String[] fields = line.split(",");
//                
//                    if (usersMap.containsKey(Integer.parseInt(fields[1]))){
//                        String value = usersMap.get(Integer.parseInt(fields[1]));
//                        value = value + ", " + tags.get(Integer.parseInt(fields[2]));
//                        usersMap.put(Integer.parseInt(fields[1]), value);
//                    }
//                    else{
//                        usersMap.put(Integer.parseInt(fields[1]), tags.get(Integer.parseInt(fields[2])));
//                    }
//                
//                line = reader.readLine();
//            }
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(TagListLoader.class.getName()).log(Level.SEVERE, null, ex);
//            return null;
//        }
//        return usersMap;
//    }

    public String getArtistName(Integer id) {
        return artistMap.get(id);
    }

}
