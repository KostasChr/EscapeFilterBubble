package gr.ntua.imu.escapefilterbubble.topics.similarities;

/**
 * Service for calculating the similarity between items based on the visits they
 * receive
 *
 * @author Kostas Christidis
 */
public class SimilarityCalculationService implements SimilarityServiceInterface {

   /* private Integer n = 100000;
    private Double minSimilarity = 0.0;
    private TagListLoader tagListLoaderService;
    private ArtistListLoader artistListLoaderService;

    public void setArtistListLoaderService(ArtistListLoader artistListLoaderService) {
        this.artistListLoaderService = artistListLoaderService;
    }

    ItemSimilarity itemSimilarity;
    ItemSimilarity tanimotoItemSimilarity;
    ItemSimilarity neighborhoodSimilarity;
    UserSimilarity tanimotoUserSimilarity;
    DataModel model;
    DataModel model1;
    NearestNItemsNeighborhood neighborhood;
    private ParallelTopicModel topicModel;
    LinkedHashMap<Integer, double[]> usersAndTopicProbabilities = new LinkedHashMap();//holds the topic probability distribution per user

    public SimilarityCalculationService(String userPreferenceFilePath,
                                        String inversePreferenceFilePath,
                                        TagListLoader tagListLoaderService, int method) throws TasteException {
        try {
            this.tagListLoaderService = tagListLoaderService;
            model = new FileDataModel(new File(userPreferenceFilePath));
            model1 = new FileDataModel(new File(inversePreferenceFilePath));
            switch (method) {
                case 0: {
                    neighborhoodSimilarity = new EuclideanDistanceSimilarity(model);
                    itemSimilarity = new UncenteredCosineSimilarity(model);
                    break;
                }
                case 1: {
                    tanimotoItemSimilarity = new TanimotoCoefficientSimilarity(model);
                    break;

                }
                case 2: {
                    tanimotoItemSimilarity = new TanimotoCoefficientSimilarity(model);
                    break;
                }


                case 3: {
                    tanimotoItemSimilarity = new TanimotoCoefficientSimilarity(model);
                    break;
                }
                case 4: {
                    tanimotoUserSimilarity = new TanimotoCoefficientSimilarity(model1);
                    tanimotoItemSimilarity = new TanimotoCoefficientSimilarity(model);
                    break;
                }

                default: {
                    neighborhoodSimilarity = new EuclideanDistanceSimilarity(model);
                    itemSimilarity = new UncenteredCosineSimilarity(model);
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(SimilarityCalculationService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public SimilarityCalculationService(ParallelTopicModel topicModel) throws TasteException {
        this.topicModel = topicModel;
        ArrayList<TopicAssignment> list = topicModel.getData();
        int iterator = 0;
        for (TopicAssignment topicAssignment : list) {
            usersAndTopicProbabilities.put((Integer) Integer.parseInt((String) topicAssignment.instance.getName()), topicModel.getTopicProbabilities(iterator));
            iterator++;
        }
    }

    public LinkedHashMap<Integer, double[]> getUsersAndTopicProbabilities() {
        return this.usersAndTopicProbabilities;
    }

    @Override
    public Double getSimilarity(Integer artistId, Integer artist2id) throws TasteException {
        Double itemSimilarityDbl = itemSimilarity.itemSimilarity(artistId, artist2id);
        if (itemSimilarityDbl.isNaN()) {
            return 0.0;
        } else {
            return (itemSimilarityDbl + 1.0) / 2.0;
        }
    }

    public Double getUserModelSimilarity(Integer artistId, Integer artist2id) throws Exception {
        Double userSimilarity = Utils.cosineDistance(usersAndTopicProbabilities.get(artistId), usersAndTopicProbabilities.get(artist2id));
        if (userSimilarity.isNaN()) {
            return 0.0;
        } else {
            return userSimilarity;
        }
    }

    public Double getUserTanimotoSimilarity(Integer userId, Integer user2id) throws TasteException {
        Double userSimilarityDbl = tanimotoUserSimilarity.userSimilarity(userId, user2id);
        if (userSimilarityDbl.isNaN()) {
            return 0.0;
        } else {
            //return (userSimilarityDbl + 1.0) / 2.0;
            return userSimilarityDbl;
        }
    }

    public Double getTanimotoSimilarity(Integer artistId, Integer artist2id) throws TasteException {

        Double itemSimilarityDbl = tanimotoItemSimilarity.itemSimilarity(artistId,
                artist2id);//itemSimilarity.itemSimilarity(artistId, artist2id);
        //System.out.println("itemSimilarityDbl: " + itemSimilarityDbl);
        //Double halfTanimotoSimilarity = 0.8 * tanimotoItemSimilarity.itemSimilarity(artistId, artist2id);
        if (itemSimilarityDbl.isNaN()) {
            return 0.0;
        } else {
            return 1 - itemSimilarityDbl;//(itemSimilarityDbl + 1.0) / 2.0;
        }

    }

    public long[] getAllSimilarItems(Integer artistId) {
        try {
            return tanimotoItemSimilarity.allSimilarItemIDs(artistId);
        } catch (TasteException ex) {
            Logger.getLogger(SimilarityCalculationService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public LinkedHashMap<Integer, Double> getAllSimilarModelUsers(Integer userId) {
        try {
            LinkedHashMap<Integer, Double> similarNodes = new LinkedHashMap();
            for (Integer user : usersAndTopicProbabilities.keySet()) {
                if (user == userId) {
                    continue;
                } else {
                    similarNodes.put(user, Utils.cosineDistance(usersAndTopicProbabilities.get(user), usersAndTopicProbabilities.get(userId)));
                }
            }
            return similarNodes;
        } catch (Exception ex) {
            Logger.getLogger(SimilarityCalculationService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String getTagsForArtist(Integer artistId) throws TasteException {
        PreferenceArray preferencesForItem = model.getPreferencesForItem(artistId);
        String tagsToReturn = "";
        for (Preference preference : preferencesForItem) {
            tagsToReturn += " " + tagListLoaderService.getTagName((int) preference.getUserID()) + " " + preference.getValue();
        }
        return tagsToReturn;

    }

    public String getTagsForArtistAsString(Integer artistId) throws TasteException {
        PreferenceArray preferencesForItem = model.getPreferencesForItem(artistId);
        String tagsToReturn = "";
        for (Preference preference : preferencesForItem) {
            for (int i = 0; i < preference.getValue(); i++) {
                tagsToReturn += " " + tagListLoaderService.getTagName((int) preference.getUserID());
            }
        }
        return tagsToReturn;
    }

    public Integer getNextNode(Integer fromItem, Integer toItem, Integer stepsLeft, Integer totalSteps, Double minSimilarity, Double previousSimilarityAchieved) {

        try {
            //Here Items are artists, songs are users
            LongPrimitiveIterator itemIDs = model.getItemIDs();

            // Map with possible next item and probability to jump to it
            HashMap<Integer, Double> jumpProbabilitiesMap = new HashMap<Integer, Double>();

            //Calculate similarity and distance
            Double similaritySourceToTarget = getSimilarity(fromItem, toItem); // similarity from source to target
            if (similaritySourceToTarget == 1.0) {
                return toItem;
            }

            while (itemIDs.hasNext()) {

                Integer recommendedItemId = itemIDs.next().intValue();
                if ((recommendedItemId.equals(toItem)) || (recommendedItemId.equals(fromItem))) {
                    continue;
                }
                //Similarities to Source and Target
                Double similarityToSource = getSimilarity(recommendedItemId, fromItem);
                Double similarityToTarget = getSimilarity(recommendedItemId, toItem);
                if (similarityToTarget.equals(Double.NaN)) {
                    similarityToTarget = 0.0;
                }
                if (similarityToSource.equals(Double.NaN)) {
                    similarityToSource = 0.0;
                }

                if ((similarityToSource > minSimilarity) && (similarityToTarget > similaritySourceToTarget) && (similarityToTarget > previousSimilarityAchieved)) {

                    if (((stepsLeft == 1) && (similarityToTarget > minSimilarity)) || (stepsLeft > 1)) {
//                        System.out.println("Return " + recommendedItemId + " with " + similarityToSource);
                        Double probability = similarityToTarget + similarityToSource;
                        jumpProbabilitiesMap.put(recommendedItemId, probability);
                    }
                }
            }
            if (jumpProbabilitiesMap.isEmpty()) {
                return null;
            } else {
                return calculateNextNode(jumpProbabilitiesMap);
            }

        } catch (Exception e) {
            Logger.getLogger(SimilarityCalculationService.class.getName()).log(Level.SEVERE, e.getLocalizedMessage());
            return null;
        }
    }

    private Integer calculateNextNode(HashMap<Integer, Double> jumpProbabilitiesMap) throws TasteException {
        Double probabilitiesSum = 0.0;
        for (Integer ri : jumpProbabilitiesMap.keySet()) {
            probabilitiesSum += jumpProbabilitiesMap.get(ri);
        }
        Random r = new Random();
        Double choice = r.nextDouble();
        Double choiceChecker = 0.0;
        for (Integer ri : jumpProbabilitiesMap.keySet()) {
            choiceChecker += jumpProbabilitiesMap.get(ri) / probabilitiesSum;
            if (choiceChecker > choice) {

                return ri;
            }
        }
        return 0;

    }

    public void printData() throws TasteException {
        LongPrimitiveIterator itemIDs = model.getItemIDs();
        while (itemIDs.hasNext()) {
            Integer recommendedItemId = itemIDs.next().intValue();
            PreferenceArray preferenceArray = model.getPreferencesForItem(recommendedItemId);
            System.out.println("Item " + recommendedItemId + " " + artistListLoaderService.getArtistName(recommendedItemId));
            for (Preference preference : preferenceArray) {
                System.out.print(" " + preference.getUserID() + " " + tagListLoaderService.getTagName((int) preference.getUserID()));
            }
            System.out.println();
        }
    }

    public PreferenceArray getUserPreferences(int userId) {
        try {
            return model.getPreferencesFromUser(userId);
        } catch (TasteException ex) {
            Logger.getLogger(SimilarityCalculationService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public FastIDSet getUserItemIDs(int userId) {
        try {
            return model.getItemIDsFromUser(userId);
        } catch (TasteException ex) {
            Logger.getLogger(SimilarityCalculationService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }*/
}
