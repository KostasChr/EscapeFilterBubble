package gr.ntua.imu.escapefilterbubble.metric.single;

import java.util.Collection;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * @author KostasChr
 */

public abstract class CountMetric<R> implements Metric<R> {
    private Queue<R> recQueue;
    private Collection<R> actualQueue;
    protected int truePositive = 0;
    protected int falsePositive = 0;
    protected int trueNegative = 0;
    protected int falseNegative = 0;

    public abstract Double calculate();

    protected void count() {
        Set<R> tSet = new HashSet<R>(getActualQueue());
        setFalseNegative(tSet.size());
        while (getRecQueue().size() > 0) {
            R r = getRecQueue().poll();
            if (tSet.contains(r)) {
                setTruePositive(getTruePositive() + 1);
                setFalseNegative(getFalseNegative() - 1);
            } else {
                setFalsePositive(getFalsePositive() + 1);
            }
        }
    }

    public Queue<R> getRecQueue() {
        return recQueue;
    }

    public void setRecQueue(Queue<R> recQueue) {
        this.recQueue = recQueue;
    }

    public Collection<R> getActualQueue() {
        return actualQueue;
    }

    public void setActualQueue(Collection<R> actualQueue) {
        this.actualQueue = actualQueue;
    }

    public int getTruePositive() {
        return truePositive;
    }

    public void setTruePositive(int truePositive) {
        this.truePositive = truePositive;
    }

    public int getFalsePositive() {
        return falsePositive;
    }

    public void setFalsePositive(int falsePositive) {
        this.falsePositive = falsePositive;
    }

    public int getTrueNegative() {
        return trueNegative;
    }

    public void setTrueNegative(int trueNegative) {
        this.trueNegative = trueNegative;
    }

    public int getFalseNegative() {
        return falseNegative;
    }

    public void setFalseNegative(int falseNegative) {
        this.falseNegative = falseNegative;
    }
}
