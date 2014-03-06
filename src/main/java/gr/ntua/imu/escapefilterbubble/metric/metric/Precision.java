/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.imu.escapefilterbubble.metric.metric;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * @author kostas
 */

public class Precision<R> implements Metric {

    private Queue<R> recQueue;
    private Queue<R> actualQueue;
    private int truePositive = 0;
    private int falsePositive = 0;
    private int trueNegative = 0;
    private int falseNegative = 0;


    public Precision(Queue<R> recQueue, Queue<R> actualQueue) {
        this.setRecQueue(recQueue);
        this.setActualQueue(actualQueue);
    }

    public Precision() {
    }

    public Double calculate() {
        Set<R> tSet = new HashSet<R>(getActualQueue());
        falseNegative = tSet.size();
        while (getRecQueue().size() > 0) {
            R r = getRecQueue().poll();
            if (tSet.contains(r)) {
                truePositive++;
                falseNegative--;
            } else {
                falsePositive++;
            }
        }
        return new Double(truePositive) / new Double(truePositive + falsePositive);
    }

    public Queue<R> getRecQueue() {
        return recQueue;
    }

    public void setRecQueue(Queue<R> recQueue) {
        this.recQueue = recQueue;
    }

    public Queue<R> getActualQueue() {
        return actualQueue;
    }

    public void setActualQueue(Queue<R> actualQueue) {
        this.actualQueue = actualQueue;
    }
}
