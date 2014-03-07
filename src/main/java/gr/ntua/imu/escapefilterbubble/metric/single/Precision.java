/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.imu.escapefilterbubble.metric.single;

import java.util.Collection;
import java.util.Queue;

/**
 * @author kostas
 */

public class Precision<R> extends CountMetric<R> {


    public Precision(Queue<R> recQueue, Collection<R> actualQueue) {
        this.setRecQueue(recQueue);
        this.setActualQueue(actualQueue);
    }

    public Precision() {
    }

    public Double calculate() {
        count();

        return new Double(getTruePositive()) / new Double(getTruePositive() + getFalsePositive());
    }


}
