package gr.ntua.imu.escapefilterbubble.topics.path;

import gr.ntua.imu.escapefilterbubble.topics.domain.ArtistListLoader;
import gr.ntua.imu.escapefilterbubble.topics.domain.ArtistTagLoader;
import gr.ntua.imu.escapefilterbubble.topics.domain.TagListLoader;
import gr.ntua.imu.escapefilterbubble.topics.domain.UserArtistLoader;
import gr.ntua.imu.escapefilterbubble.topics.similarities.SimilarityServiceInterface;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * Service for calculating the similarity between items based on the visits they
 * receive
 *
 * @author Kostas Christidis
 */
public class RandomWalkService {

    private SimilarityServiceInterface similarityCalculationService;
    private Integer sourceArtistId;
    private Integer stepsLeft;
    private Integer iterationsCount;
    private HashMap votesSet[];
    public static final double SIMILARITY_STEP = 0.01;
    public static final int MINIMUM_OPTIONS = 4;
    private ArtistTagLoader artistTagLoader;
    private UserArtistLoader userArtistLoader;

    public UserArtistLoader getUserArtistLoader() {
        return userArtistLoader;
    }

    public void setUserArtistLoader(UserArtistLoader userArtistLoader) {
        this.userArtistLoader = userArtistLoader;
    }

    public ArtistTagLoader getArtistTagLoader() {
        return artistTagLoader;
    }

    public void setArtistTagLoader(ArtistTagLoader artistTagLoader) {
        this.artistTagLoader = artistTagLoader;
    }

    public void setIterationsCount(Integer iterationsCount) {
        this.iterationsCount = iterationsCount;
    }

    private ArtistListLoader artistListLoaderService;
    private TagListLoader tagListLoaderService;

    public ArtistListLoader getArtistListLoaderService() {
        return artistListLoaderService;
    }

    public void setArtistListLoaderService(ArtistListLoader artistListLoaderService) {
        this.artistListLoaderService = artistListLoaderService;
    }

    public TagListLoader getTagListLoaderService() {
        return tagListLoaderService;
    }

    public void setTagListLoaderService(TagListLoader tagListLoaderService) {
        this.tagListLoaderService = tagListLoaderService;
    }

    public Integer getStepsLeft() {
        return stepsLeft;
    }

    public void setStepsLeft(Integer stepsLeft) {
        this.stepsLeft = stepsLeft;
    }

    public Integer getSourceArtistId() {
        return sourceArtistId;
    }

    public void setSourceArtistId(Integer sourceArtistId) {
        this.sourceArtistId = sourceArtistId;
    }

    public Integer getTargetArtistId() {
        return targetArtistId;
    }

    public void setTargetArtistId(Integer targetArtistId) {
        this.targetArtistId = targetArtistId;
    }

    private Integer targetArtistId;

    public RandomWalkService(SimilarityServiceInterface similarityCalculationService) {
        this.similarityCalculationService = similarityCalculationService;
    }

    public void doRandomWalk() {
        votesSet = new HashMap[stepsLeft];

        System.out.println("Source artist: " + sourceArtistId);
        Double maxSimilarity = 0.2;
        Integer timesPathUnfinished = 0;
        for (int j = 0; j < iterationsCount; j++) {
            Integer sourceArtistIdTemp = sourceArtistId;
            Integer chosenArtists[] = new Integer[stepsLeft];
            Boolean pathFinished = true;
            Integer recommendedItemId;
            Double previousSimilarityAchieved = 0.0;

            for (int i = 0; i < stepsLeft; i++) {
                recommendedItemId = similarityCalculationService.getNextNode(sourceArtistIdTemp, targetArtistId, stepsLeft - i, stepsLeft, maxSimilarity, previousSimilarityAchieved);
                if ((recommendedItemId == null) || (similarityCalculationService.getSimilarity(recommendedItemId, sourceArtistIdTemp).equals(0.0))) {
                    System.out.println("Could not find appropriate next node!");
                    pathFinished = false;

                } else {
                    sourceArtistIdTemp = recommendedItemId;
                    chosenArtists[i] = sourceArtistIdTemp;
                    previousSimilarityAchieved = similarityCalculationService.getSimilarity(recommendedItemId, targetArtistId);
                }
            }
            if (pathFinished) {
                System.out.println("*********New path proposed ");
                for (int i = 0; i < stepsLeft; i++) {

                    if (i == 0) {
                        System.out.println("Added step" + i + " " + chosenArtists[i] + " with " + similarityCalculationService.getSimilarity(chosenArtists[i], sourceArtistId));
                    } else {
                        System.out.println("Added step" + i + " " + chosenArtists[i] + " with " + similarityCalculationService.getSimilarity(chosenArtists[i], chosenArtists[i - 1]));
                    }

                    if (votesSet[i] == null) {
                        votesSet[i] = new HashMap<Integer, Integer>();
                    }
                    if (votesSet[i].containsKey(chosenArtists[i])) {
                        //remove it and add it with +1
                        Integer currentCount = (Integer) votesSet[i].get(chosenArtists[i]);
                        votesSet[i].remove(chosenArtists[i]);
                        votesSet[i].put(chosenArtists[i], currentCount + 1);
                    } else {
                        //add it with 1
                        votesSet[i].put(chosenArtists[i], 1);
                    }
                }
                if (smallNumberOfCandidates(votesSet, j)) {
                    timesPathUnfinished += 1;
                    if (timesPathUnfinished > 10) {
                        timesPathUnfinished = 0;
                        maxSimilarity -= SIMILARITY_STEP;
                        if (maxSimilarity < 0) {
                            System.out.println("NOTHING FOUND!!!");

                        }
                        System.out.println("Reducing similarity to " + maxSimilarity);
                    }
                } else {
                    timesPathUnfinished = 0;

                }

            } else {
                timesPathUnfinished += 1;
                System.out.println("Could not finish graph one more time");
                if (timesPathUnfinished > 5) {
                    timesPathUnfinished = 0;
                    maxSimilarity -= SIMILARITY_STEP;
                    if (maxSimilarity < 0) {
                        System.out.println("NOTHING FOUND!!!");

                    }
                    System.out.println("Reducing similarity to " + maxSimilarity);
                }
            }
        }
        HashSet<Integer> notThis = new HashSet<Integer>();
        notThis.add(sourceArtistId);
        notThis.add(targetArtistId);
        System.out.println("starting from " + sourceArtistId + " with similarity " + similarityCalculationService.getSimilarity(sourceArtistId, targetArtistId) + " with " + userArtistLoader.getTagsForArtist(sourceArtistId));
        for (int i = 0; i < stepsLeft; i++) {
            Integer proposedItem = getHighestCountId(votesSet[i], notThis);
            System.out.println("Step " + (i + 1) + " : " + proposedItem + " with " + similarityCalculationService.getSimilarity(proposedItem, targetArtistId) + " to target, and " + similarityCalculationService.getSimilarity(proposedItem, sourceArtistId) + " to source " + " with " + userArtistLoader.getTagsForArtist(proposedItem));
            notThis.add(proposedItem);
        }
        System.out.println("Finally " + targetArtistId + " " + userArtistLoader.getTagsForArtist(sourceArtistId));
    }

    private Integer getHighestCountId(HashMap<Integer, Integer> map, Set<Integer> notThis) {
        Iterator itr = map.keySet().iterator();
        Integer maxId = 0;
        Integer maxVotes = 0;
        while (itr.hasNext()) {
            Integer currentId = (Integer) itr.next();
            if ((map.get(currentId) > maxVotes) && (!notThis.contains(currentId))) {
                maxVotes = map.get(currentId);
                maxId = currentId;
            }
        }
//        System.out.println("a winner with " + maxVotes + " votes" );
        return maxId;
    }

    private boolean smallNumberOfCandidates(HashMap[] votesSet, Integer j) {

        if (j < 10) {
            return false;
        }

        for (int i = 0; i < votesSet.length; i++) {
            if (votesSet[i].size() < MINIMUM_OPTIONS) {
                return true;
            }
        }
        return false;
    }
}
