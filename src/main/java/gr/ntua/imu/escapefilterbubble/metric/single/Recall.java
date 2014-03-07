/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.imu.escapefilterbubble.metric.single;

/**
 * @author kostas
 */

public class Recall<R> extends CountMetric<R> {


    public Recall() {
    }

    public Double calculate() {

        count();

        return new Double(getTruePositive()) / new Double(getTruePositive() + getFalseNegative());

    }


}
