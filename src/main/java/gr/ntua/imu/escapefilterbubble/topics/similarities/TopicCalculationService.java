/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.imu.escapefilterbubble.topics.similarities;

import gr.ntua.imu.escapefilterbubble.topics.analyzer.Analyzer;
import gr.ntua.imu.escapefilterbubble.topics.domain.ArtistTagLoader;
import gr.ntua.imu.escapefilterbubble.topics.domain.UserArtistLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kostas
 */
public class TopicCalculationService implements SimilarityServiceInterface {

    private ArtistTagLoader artistTagLoader;

    public UserArtistLoader getUserArtistLoader() {
        return userArtistLoader;
    }

    public void setUserArtistLoader(UserArtistLoader userArtistLoader) {
        this.userArtistLoader = userArtistLoader;
    }

    private UserArtistLoader userArtistLoader;

    public ArtistTagLoader getArtistTagLoader() {
        return artistTagLoader;
    }

    public void setArtistTagLoader(ArtistTagLoader artistTagLoader) {
        this.artistTagLoader = artistTagLoader;
    }

    private static final int BURNIN = 50;
    private static final int THINNING = 10;
    private Analyzer topicModelAnalyser;
    private String[] dataTest;
    private String[] nameTest;
    private int numberOfTopics;
    private int numberOfIterations;
    private double[] sampledDistribution;


    public void calculateTopics(String transactionFilePath) throws FileNotFoundException, IOException, Exception {

        //variables
        numberOfTopics = 30;
        numberOfIterations = 1000;
        Double Alpha = 0.01;
        Double Beta = 0.01;
        Double ProportionAnalysed = 1.0;


        //On source and output of information

//        //Import user/artist preferences
//        FileImport fileImport = FileImport.getInstance();
//        fileImport.setArtistTagLoader(artistTagLoader);
//        //String transactionFilePath = "big_annotations.csv";
//        FileInputStream fis = new FileInputStream(transactionFilePath);
//        fileImport.setFis(fis);
//        fileImport.setUserArtistLoader(userArtistLoader);
//        HashMap<Integer, String> documentMap = fileImport.getItemSets();
//
////        for (Map.Entry<Integer, String> entry : documentMap.entrySet()){
////            System.out.println(""+entry.getKey()+ " " + entry.getValue());
////        }
//
//
//
//        //Initialize the topic model wrapper
//        topicModelAnalyser = TopicModelAnalyser.getInstance();
//        topicModelAnalyser.setAlpha(Alpha);
//        topicModelAnalyser.setBeta(Beta);
//        topicModelAnalyser.setNumberOfIterations(numberOfIterations);
//        topicModelAnalyser.setNumberOfTopics(numberOfTopics);
//        topicModelAnalyser.setDocumentMap(documentMap);
//        topicModelAnalyser.setProportionAnalysed(ProportionAnalysed);
//
//        //estimate the topic model
//        topicModelAnalyser.loadModel("topicModel.file");

    }

    public Double getSimilarity(Integer firstUserId, Integer secondUserId) {
//        return topicModelAnalyser.getSimilarity(firstUserId, secondUserId);
        return null;
    }

    public Integer getNextNode(Integer fromItem, Integer toItem, Integer stepsLeft, Integer totalStepsDouble, Double minSimilarity, Double previousSimilarityAchieved) {

        try {
            // Here we need to get the ids of all users
//            String[] nameTrain = topicModelAnalyser.getNameTrain();

            // Map with possible next item and probability to jump to it
            HashMap<Integer, Double> jumpProbabilitiesMap = new HashMap<Integer, Double>();

            Double similaritySourceToTarget = getSimilarity(fromItem, toItem); // similarity from source to target
            if (similaritySourceToTarget == 1.0) {
                return toItem;
            }


//            for (String recommendedItemIdStr : nameTrain) {
//
//
//            //Calculate similarity and distance
//
//
//
//
//                Integer recommendedItemId = Integer.parseInt(recommendedItemIdStr);
//                if ( (recommendedItemId.equals(toItem)) || (recommendedItemId.equals(fromItem)) ){
//                    continue;
//                }
//                //Similarities to Source and Target
//                Double similarityToSource = getSimilarity(recommendedItemId, fromItem);
//                Double similarityToTarget = getSimilarity(recommendedItemId, toItem);
//                if (similarityToTarget.equals(Double.NaN)) {
//                    similarityToTarget = 0.0;
//                }
//                if (similarityToSource.equals(Double.NaN)) {
//                    similarityToSource = 0.0;
//                }
//
//                if ((similarityToSource > minSimilarity) && (similarityToTarget > similaritySourceToTarget)&& (similarityToTarget>previousSimilarityAchieved)) {
//
//                    if ( ((stepsLeft == 1) && (similarityToTarget > minSimilarity)) || (stepsLeft>1) ) {
////                        System.out.println("Return " + recommendedItemId + " with " + similarityToSource);
//                        Double probability = similarityToTarget + similarityToSource;
//                        jumpProbabilitiesMap.put(recommendedItemId, probability);
//                    }
//                }
//            }
//            if (jumpProbabilitiesMap.isEmpty()) {
//                return null;
//            } else {
//                return calculateNextNode(jumpProbabilitiesMap);
//            }

        } catch (Exception e) {
            Logger.getLogger(SimilarityCalculationService.class.getName()).log(Level.SEVERE, e.getLocalizedMessage());

            e.printStackTrace();
            ;
            return null;
        }
        return null;
    }

    private Integer calculateNextNode(HashMap<Integer, Double> jumpProbabilitiesMap) {
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
}
