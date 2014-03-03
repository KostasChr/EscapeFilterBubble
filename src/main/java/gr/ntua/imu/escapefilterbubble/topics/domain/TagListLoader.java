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
public class TagListLoader {

    private String tagListFilePath;
    private HashMap<Integer, String> tagMap;

    public TagListLoader(String tagListFilePath) {
        this.tagListFilePath = tagListFilePath;
    }

    public HashMap<Integer, String> createHashMap() throws FileNotFoundException, IOException {
        tagMap = new HashMap<Integer, String>();
        BufferedReader reader;
        File inputFile = new File(tagListFilePath);
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"), 8192);

            reader.readLine();
            String line = reader.readLine();
            while (line != null) {
                String[] fields = line.split(",");
                tagMap.put(Integer.parseInt(fields[0]), fields[1]);
                line = reader.readLine();
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TagListLoader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return tagMap;
    }

    public String getTagName(Integer id) {
        return tagMap.get(id);
    }
}
