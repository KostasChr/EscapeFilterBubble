package gr.ntua.imu.escapefilterbubble.topics.domain;

/**
 * Service for calculating the similarity between items based on the visits they
 * receive
 *
 * @author Kostas Christidis
 */
public class UserArtistLoader {

//    private TagListLoader tagListLoaderService;
//    private ArtistTagLoader artistTagLoader;
//    ItemSimilarity itemSimilarity;
//    ItemSimilarity tanimotoItemSimilarity;
//    ItemSimilarity neighborhoodSimilarity;
//    DataModel model;
//    NearestNItemsNeighborhood neighborhood;
//
//    public UserArtistLoader(String userPreferenceFilePath, TagListLoader tagListLoaderService, ArtistTagLoader artistTagLoader) throws TasteException {
//        try {
//            this.tagListLoaderService = tagListLoaderService;
//            this.artistTagLoader = artistTagLoader;
//            model = new FileDataModel(new File(userPreferenceFilePath));
//
//        } catch (IOException ex) {
//            Logger.getLogger(SimilarityCalculationService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public String getTagsForArtist(Integer artistId) throws TasteException {
//        PreferenceArray preferencesForUser = model.getPreferencesFromUser(artistId);
//        String tagsToReturn = "";
//        for (Preference preference : preferencesForUser) {
//            ArrayList<Integer> tagsArray = artistTagLoader.getTagsForArtist((int) preference.getUserID());
//            for (Integer tag : tagsArray) {
//                tagsToReturn += " " + tagListLoaderService.getTagName(tag) + " ";
//            }
//
//        }
//        return tagsToReturn;
//    }
//
//    public String getTagsForArtistAsString(Integer artistId) throws TasteException {
//        return getTagsForArtist(artistId);
//    }
//
//    public LongPrimitiveIterator getUserIds() {
//        try {
//            return model.getUserIDs();
//        } catch (TasteException ex) {
//            Logger.getLogger(UserArtistLoader.class.getName()).log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }
//
//    public PreferenceArray getUserPreferences(int userId) {
//        try {
//            return model.getPreferencesFromUser(userId);
//        } catch (TasteException ex) {
//            Logger.getLogger(UserArtistLoader.class.getName()).log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }
//
//    public double getRecommendation(int userId, int itemId) {
//        try {
//            ItemUserAverageRecommender itemUserAverageRecommender = new ItemUserAverageRecommender(model);
//            return itemUserAverageRecommender.estimatePreference(userId, itemId);
//        } catch (TasteException ex) {
//            Logger.getLogger(SimilarityCalculationService.class.getName()).log(Level.SEVERE, null, ex);
//            return 0;
//        }
//    }
}
