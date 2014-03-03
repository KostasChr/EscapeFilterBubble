package gr.ntua.imu.escapefilterbubble.topics.path;

import gr.ntua.imu.escapefilterbubble.topics.domain.ArtistListLoader;
import gr.ntua.imu.escapefilterbubble.topics.domain.TagListLoader;
import gr.ntua.imu.escapefilterbubble.topics.similarities.SimilarityCalculationService;

import java.util.*;

/**
 * Service for calculating the similarity between items based on the visits they
 * receive
 *
 * @author Kostas Christidis
 */
public class DijkstraService {

    private SimilarityCalculationService similarityCalculationService;
    private Integer sourceArtistId;
    private Integer stepsLeft;
    private Integer iterationsCount;
    private HashMap votesSet[];

    private ArtistListLoader artistListLoaderService;
    private TagListLoader tagListLoaderService;

    private Set<Integer> settledNodes;
    private Set<Integer> unSettledNodes;
    private Map<Integer, Integer> predecessors;
    private Map<Integer, Double> distance;
    private HashMap<Integer, String> artists;

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

    public DijkstraService(SimilarityCalculationService similarityCalculationService) {
        this.similarityCalculationService = similarityCalculationService;
    }

    public HashMap<Integer, String> getArtists() {
        return artists;
    }

    public void setArtists(HashMap<Integer, String> artists) {
        this.artists = artists;
    }

    public void runDijkstra() {
        settledNodes = new HashSet<Integer>();
        unSettledNodes = new HashSet<Integer>();
        distance = new HashMap<Integer, Double>();
        predecessors = new HashMap<Integer, Integer>();

        distance.put(sourceArtistId, 0.0);
        unSettledNodes.add(sourceArtistId);
        while (unSettledNodes.size() > 0) {
            int node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            try {
                findMinimalDistances(node);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //System.out.println("unSettledNodes size: " + unSettledNodes.size());
        }
    }

    public void runModelBasedDijkstra() {
        settledNodes = new HashSet<Integer>();
        unSettledNodes = new HashSet<Integer>();
        distance = new HashMap<Integer, Double>();
        predecessors = new HashMap<Integer, Integer>();

        distance.put(sourceArtistId, 0.0);
        unSettledNodes.add(sourceArtistId);
        while (unSettledNodes.size() > 0) {
            int node = getModelBasedMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            try {
                findModelBasedMinimalDistances(node);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //System.out.println("unSettledNodes size: " + unSettledNodes.size());
        }
    }

    private double getShortestDistance(int destination) {
        Double d = distance.get(destination);
        if (d == null) {
            return Double.MAX_VALUE;
        } else {
            return d;
        }
    }

    private double getModelBasedShortestDistance(int destination) {
        Double d = distance.get(destination);
        if (d == null) {
            return Double.MAX_VALUE;
        } else {
            return d;
        }
    }

    private void findMinimalDistances(int node) {
        List<Integer> adjacentNodes = getNeighbors(node);
        for (Integer target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node)
                    + similarityCalculationService.getUserTanimotoSimilarity(node, target)) {
                //similarityCalculationService.getTanimotoSimilarity(node, target)) {
                distance.put(target, getShortestDistance(node)
                        + similarityCalculationService.getUserTanimotoSimilarity(node, target));
                //similarityCalculationService.getTanimotoSimilarity(node, target));
                predecessors.put(target, node);
                if (settledNodes.size() < 1700) {
                    unSettledNodes.add(target);
                }
            }
        }
    }

    private void findModelBasedMinimalDistances(int node) throws Exception {
        List<Integer> adjacentNodes = getModelBasedNeighbors(node);
        for (Integer target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node)
                    + similarityCalculationService.getUserModelSimilarity(node, target)) {
                //similarityCalculationService.getTanimotoSimilarity(node, target)) {
                distance.put(target, getModelBasedShortestDistance(node)
                        + similarityCalculationService.getUserModelSimilarity(node, target));
                //similarityCalculationService.getTanimotoSimilarity(node, target));
                predecessors.put(target, node);
                if (settledNodes.size() < 1700) {
                    unSettledNodes.add(target);
                }
            }
        }
    }

    private int getMinimum(Set<Integer> vertexes) {
        int minimum = -1;
        for (Integer vertex : vertexes) {
            if (minimum == -1) {
                minimum = vertex;
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }

    private int getModelBasedMinimum(Set<Integer> vertexes) {
        int minimum = -1;
        for (Integer vertex : vertexes) {
            if (minimum == -1) {
                minimum = vertex;
            } else {
                if (getModelBasedShortestDistance(vertex) < getModelBasedShortestDistance(minimum)) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }

    private List<Integer> getNeighbors(Integer node) {
        long[] neighboorsList = similarityCalculationService.getAllSimilarItems(node);
        List<Integer> neighbors = new ArrayList();
        double maxSimilarity = 0;
        double minSimilarity = 100;
        LinkedHashMap<Integer, Double> similarities = new LinkedHashMap<Integer, Double>();
        for (Long neighboor : neighboorsList) {
            try {
                double similarity = similarityCalculationService.getUserTanimotoSimilarity(node, neighboor.intValue());
                //similarityCalculationService.getTanimotoSimilarity(node, neighboor.intValue());
                if (maxSimilarity < similarity) maxSimilarity = similarity;
                if (minSimilarity > similarity) minSimilarity = similarity;
                similarities.put(neighboor.intValue(), similarity);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("could not find neighboor for node: " + node + " and target: " + neighboor.intValue());
            }
        }
//        System.out.println("for node: " + node + " neighboors size: " + neighboorsList.length +  
//                " max similarity: " + maxSimilarity + " min similarity: " + minSimilarity);
        //double threshold = 0.8; it seems that this threshold provides some nice results
        double threshold = 1.0;
        //0.8;
        //0.76;
        //0.61;

        List<Map.Entry<Integer, Double>> entries =
                new ArrayList<Map.Entry<Integer, Double>>(similarities.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<Integer, Double>>() {
            public int compare(Map.Entry<Integer, Double> a, Map.Entry<Integer, Double> b) {
                return b.getValue().compareTo(a.getValue());
            }
        });
        Map<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();
        for (Map.Entry<Integer, Double> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

//        if (Collections.max(similarities) < threshold){
//            threshold = Collections.max(similarities);
//        }
        int iterations = 0;
        for (Map.Entry<Integer, Double> entry : sortedMap.entrySet()) {

            //adjust the value in order to influence the length of the path maybe?
            if (iterations > 30) break;

            double similarity;

            try {

                similarity = entry.getValue();
                //similarityCalculationService.getTanimotoSimilarity(node, neighboor.intValue());
                //System.out.println("Found similarity: " + similarity);
                if (//similarity <= threshold &&
                        !isSettled(entry.getKey())) {
                    //System.out.println("for node: " + node + ", " + artists.get(node) + " ***** and target *****: " + entry.getKey() + ", " + artists.get(entry.getKey()) + " similarity: " + entry.getValue());
                    neighbors.add(entry.getKey());
                    iterations++;
                }
            } catch (Exception ex) {
                //Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }

        }
        return neighbors;
    }

    private List<Integer> getModelBasedNeighbors(Integer node) {
        LinkedHashMap<Integer, Double> neighboorsList = similarityCalculationService.getAllSimilarModelUsers(node);
        List<Integer> neighbors = new ArrayList();
        double maxSimilarity = 0;
        double minSimilarity = 100;
        LinkedHashMap<Integer, Double> similarities = new LinkedHashMap<Integer, Double>();
        for (Map.Entry<Integer, Double> neighboor : neighboorsList.entrySet()) {
            try {
                double similarity = neighboor.getValue();
                //similarityCalculationService.getTanimotoSimilarity(node, neighboor.intValue());
                if (maxSimilarity < similarity) maxSimilarity = similarity;
                if (minSimilarity > similarity) minSimilarity = similarity;
                similarities.put(neighboor.getKey(), similarity);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("could not find neighboor for node: " + node + " and target: " + neighboor.getKey());
            }
        }
//        System.out.println("for node: " + node + " neighboors size: " + neighboorsList.length +  
//                " max similarity: " + maxSimilarity + " min similarity: " + minSimilarity);
        //double threshold = 0.8; it seems that this threshold provides some nice results
        double threshold = 1.0;
        //0.8;
        //0.76;
        //0.61;

        List<Map.Entry<Integer, Double>> entries =
                new ArrayList<Map.Entry<Integer, Double>>(similarities.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<Integer, Double>>() {
            public int compare(Map.Entry<Integer, Double> a, Map.Entry<Integer, Double> b) {
                return b.getValue().compareTo(a.getValue());
            }
        });
        Map<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();
        for (Map.Entry<Integer, Double> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

//        if (Collections.max(similarities) < threshold){
//            threshold = Collections.max(similarities);
//        }
        int iterations = 0;
        for (Map.Entry<Integer, Double> entry : sortedMap.entrySet()) {

            //adjust the value in order to influence the length of the path maybe?
            if (iterations > 60) break;

            double similarity;

            try {

                similarity = entry.getValue();
                //similarityCalculationService.getTanimotoSimilarity(node, neighboor.intValue());
                //System.out.println("Found similarity: " + similarity);
                if (//similarity <= threshold &&
                        !isSettled(entry.getKey())) {
                    //System.out.println("for node: " + node + ", " + artists.get(node) + " ***** and target *****: " + entry.getKey() + ", " + artists.get(entry.getKey()) + " similarity: " + entry.getValue());
                    neighbors.add(entry.getKey());
                    iterations++;
                }
            } catch (Exception ex) {
                //Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }

        }
        return neighbors;
    }

    private boolean isSettled(Integer vertex) {
        return settledNodes.contains(vertex);
    }

    public LinkedList<Integer> getPath(Integer target) {
        LinkedList<Integer> path = new LinkedList<Integer>();
        Integer step = target;
        // Check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
    }
}
