package gr.ntua.imu.escapefilterbubble.topics.domain;

import gr.ntua.imu.recsys.filterbubble.neighborhood.NearestNItemsNeighborhood;
import gr.ntua.imu.recsys.fitlerbubble.similarities.*;
import gr.ntua.imu.recsys.fitlerbubble.util.Utils;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service for calculating the similarity between items based on the visits they
 * receive
 *
 * @author Kostas Christidis
 */
public class ArtistTagLoader {

    private TagListLoader tagListLoaderService;
    ItemSimilarity itemSimilarity;
    ItemSimilarity tanimotoItemSimilarity;
    ItemSimilarity neighborhoodSimilarity;
    DataModel model;
    NearestNItemsNeighborhood neighborhood;
    private BufferedReader br;
    private InputStream fis;
    private HashMap<Integer, HashSet<Integer>> artistTagMap;
    private HashMap<Integer, String> strArtistTagMap;

    /**
     * @param userPreferenceFilePath
     * @param tagListLoaderService
     * @throws TasteException
     */
    public ArtistTagLoader(String userPreferenceFilePath, TagListLoader tagListLoaderService) throws TasteException {

        try {
            this.tagListLoaderService = tagListLoaderService;

            artistTagMap = (HashMap<Integer, HashSet<Integer>>) Utils.DeSerializeObject("artistTagMap");
            strArtistTagMap = (HashMap<Integer, String>) Utils.DeSerializeObject("strArtistTagMap");
            if (artistTagMap != null && strArtistTagMap != null) {
                return;
            }

            fis = new FileInputStream(userPreferenceFilePath);
            br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
            String line;

            artistTagMap = new HashMap<Integer, HashSet<Integer>>();
            strArtistTagMap = new HashMap<Integer, String>();
            Integer artistId;
            Integer tagId;

            br.readLine();
            System.out.println("Reading Artist Tag Connection Input");
            while ((line = br.readLine()) != null) {

                String[] columns = line.split(",");
                artistId = Integer.parseInt(columns[1]);
                tagId = Integer.parseInt(columns[2]);

                if (!artistTagMap.keySet().contains(artistId)) {
                    HashSet<Integer> tagSet = new HashSet<Integer>();
                    tagSet.add(tagId);
                    artistTagMap.put(artistId, tagSet);

                } else {
                    HashSet<Integer> tagSet = artistTagMap.get(artistId);
                    tagSet.add(tagId);
                    artistTagMap.remove(artistId);
                    artistTagMap.put(artistId, tagSet);
                }
                if (!strArtistTagMap.keySet().contains(artistId)) {
                    String tagSet = "" + columns[2];
                    strArtistTagMap.put(artistId, tagSet);

                } else {
                    String tagSet = strArtistTagMap.get(artistId);
                    tagSet = tagSet + " " + columns[2];
                    strArtistTagMap.remove(artistId);
                    strArtistTagMap.put(artistId, tagSet);
                }
            }
            Utils.SerializeObject("artistTagMap", artistTagMap);
            Utils.SerializeObject("strArtistTagMap", strArtistTagMap);
        } catch (IOException ex) {
            Logger.getLogger(SimilarityCalculationService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArtistTagLoader(String userPreferenceFilePath, TagListLoader tagListLoaderService, String artistTagMapFile) throws TasteException {

        try {
            this.tagListLoaderService = tagListLoaderService;

            artistTagMap = (HashMap<Integer, HashSet<Integer>>) Utils.DeSerializeObject(artistTagMapFile);
            if (artistTagMap != null) {
                return;
            }

            fis = new FileInputStream(userPreferenceFilePath);
            br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
            String line;

            artistTagMap = new HashMap<Integer, HashSet<Integer>>();
            Integer artistId;
            Integer tagId;

            br.readLine();
            System.out.println("Reading Artist Tag Connection Input");
            while ((line = br.readLine()) != null) {

                String[] columns = line.split("\t");
                artistId = Integer.parseInt(columns[1]);
                tagId = Integer.parseInt(columns[2]);

                if (!artistTagMap.keySet().contains(artistId)) {
                    HashSet<Integer> tagSet = new HashSet<Integer>();
                    tagSet.add(tagId);
                    artistTagMap.put(artistId, tagSet);

                } else {
                    HashSet<Integer> tagSet = artistTagMap.get(artistId);
                    tagSet.add(tagId);
                    artistTagMap.remove(artistId);
                    artistTagMap.put(artistId, tagSet);


                }
            }
            Utils.SerializeObject("artistTagMap", artistTagMap);
        } catch (IOException ex) {
            Logger.getLogger(SimilarityCalculationService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Integer> getTagsForArtist(Integer artistId) throws TasteException {
        ArrayList<Integer> listToReturn = new ArrayList<Integer>();
        HashSet<Integer> tagsForUser = artistTagMap.get(artistId);

        for (Integer tag : tagsForUser) {
            listToReturn.add(tag);
        }
        return listToReturn;
    }

    public String getStrTagsForArtist(Integer artistId) {
        String tagsForArtist = strArtistTagMap.get(artistId);

        return tagsForArtist;
    }

    public String getTagsForArtistAsString(Integer artistId) throws TasteException {
        //System.out.println("tags for artist: " + artistId);
        HashSet<Integer> tagsForArtist = artistTagMap.get(artistId);
        String tagsToReturn = "";
        if (tagsForArtist != null) {
            for (Integer tag : tagsForArtist) {
                tagsToReturn += " " + tag + " ";
            }
        }
        return tagsToReturn;
    }


}
