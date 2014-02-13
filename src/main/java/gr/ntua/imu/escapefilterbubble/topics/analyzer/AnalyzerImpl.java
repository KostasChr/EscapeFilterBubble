package gr.ntua.imu.escapefilterbubble.topics.analyzer;

import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.StringArrayIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.Alphabet;
import cc.mallet.types.IDSorter;
import cc.mallet.types.InstanceList;
import gr.ntua.imu.escapefilterbubble.topics.data.Source;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * User: Kostas Christidis
 */
public class AnalyzerImpl implements Analyzer {

    private Pipe pipe;
    private Integer numberOfTopics;
    private Integer numberOfIterations;
    private Double alpha;
    private Double beta;
    private ParallelTopicModel topicModel;
    private Source source;
    private ArrayList<TreeSet<IDSorter>> topicSortedWords;
    private double[] topicDistribution;
    private Alphabet dataAlphabet;

    @Override
    public void loadTrainSet() {
        try {
            setPipe(buildPipe());
            getSource().readDocuments();
            String[] documents = getSource().getDocuments().toArray(new String[]{});
            StringArrayIterator iterator = new StringArrayIterator(documents);
            InstanceList instances = new InstanceList(this.getPipe());
            instances.addThruPipe(iterator);
            setTopicModel(new ParallelTopicModel(getNumberOfTopics(), getAlpha(), getBeta()));
            getTopicModel().addInstances(instances);
            setDataAlphabet(instances.getDataAlphabet());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    private Pipe buildPipe() {

        ArrayList pipeList = new ArrayList();

        pipeList.add(new Input2CharSequence("UTF-8"));

        Pattern tokenPattern =
                Pattern.compile("[\\p{L}\\p{N}_]{3,}+");
        pipeList.add(new CharSequence2TokenSequence(tokenPattern));
        pipeList.add(new TokenSequenceLowercase());
        pipeList.add(new TokenSequenceRemoveStopwords(false, false));
        pipeList.add(new TokenSequence2FeatureSequence());

        return new SerialPipes(pipeList);
    }

    @Override
    public void estimate() {
        getTopicModel().setTopicDisplay(100, 20);
        getTopicModel().setNumIterations(getNumberOfIterations());
        try {
            getTopicModel().estimate();
            topicSortedWords = getTopicModel().getSortedWords();
            topicDistribution = getTopicModel().getTopicProbabilities(0);


        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public Set<String> getWordsForTopic(Integer topicId) {
        Iterator<IDSorter> iterator = topicSortedWords.get(topicId).iterator();
        HashSet<String> result = new HashSet<String>();
        int rank = 0;
        while (iterator.hasNext() && rank < 5) {
            IDSorter idCountPair = iterator.next();
            result.add((String) getDataAlphabet().lookupObject(idCountPair.getID()));
            rank++;
        }

        return result;
    }

    @Override
    public Integer getSize() {
        return getTopicModel().getNumTopics();
    }

    public Pipe getPipe() {
        return pipe;
    }

    public void setPipe(Pipe pipe) {
        this.pipe = pipe;
    }

    public Integer getNumberOfTopics() {
        return numberOfTopics;
    }

    public void setNumberOfTopics(Integer numberOfTopics) {
        this.numberOfTopics = numberOfTopics;
    }

    public Integer getNumberOfIterations() {
        return numberOfIterations;
    }

    public void setNumberOfIterations(Integer numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

    public Double getAlpha() {
        return alpha;
    }

    public void setAlpha(Double alpha) {
        this.alpha = alpha;
    }

    public Double getBeta() {
        return beta;
    }

    public void setBeta(Double beta) {
        this.beta = beta;
    }

    public ParallelTopicModel getTopicModel() {
        return topicModel;
    }

    public void setTopicModel(ParallelTopicModel topicModel) {
        this.topicModel = topicModel;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public ArrayList<TreeSet<IDSorter>> getTopicSortedWords() {
        return topicSortedWords;
    }

    public void setTopicSortedWords(ArrayList<TreeSet<IDSorter>> topicSortedWords) {
        this.topicSortedWords = topicSortedWords;
    }

    public double[] getTopicDistribution() {
        return topicDistribution;
    }

    public void setTopicDistribution(double[] topicDistribution) {
        this.topicDistribution = topicDistribution;
    }

    public Alphabet getDataAlphabet() {
        return dataAlphabet;
    }

    public void setDataAlphabet(Alphabet dataAlphabet) {
        this.dataAlphabet = dataAlphabet;
    }
}
