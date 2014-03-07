/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.imu.escapefilterbubble.metric.multi;

import gr.ntua.imu.escapefilterbubble.metric.single.Metric;

import java.util.Queue;

/**
 * @author kostas
 */
public class KendalsTau<R, T> implements Metric {

    private Queue<R> rQueue;
    private Queue<T> tQueue;

    public KendalsTau(Queue<R> rQueue, Queue<T> tQueue) {
        this.rQueue = rQueue;
        this.tQueue = tQueue;
    }

    public Double calculate() {
        return null;

    }

}
