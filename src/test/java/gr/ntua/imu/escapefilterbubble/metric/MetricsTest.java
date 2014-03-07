package gr.ntua.imu.escapefilterbubble.metric;

import gr.ntua.imu.escapefilterbubble.metric.single.Precision;
import gr.ntua.imu.escapefilterbubble.metric.single.Recall;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.PriorityQueue;

/**
 * @author KostasChr
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class MetricsTest {
    private static PriorityQueue<String> rQueue;
    private static PriorityQueue<String> tQueue;


    @Before
    public void initializeTestVariables() {
        rQueue = new PriorityQueue<String>();
        rQueue.add("apple");
        rQueue.add("kiwi");
        rQueue.add("pear");
        tQueue = new PriorityQueue<String>();
        tQueue.add("orange");
        tQueue.add("apple");
        tQueue.add("banana");
    }

    @Autowired
    private Precision precision;

    @Autowired
    private Recall recall;

    @Test
    public void testPrecision() {
        precision.setRecQueue(rQueue);
        precision.setActualQueue(tQueue);
        Assert.assertEquals(new Double(1) / new Double(3), precision.calculate());
    }

    @Test
    public void testRecall() {
        recall.setRecQueue(rQueue);
        recall.setActualQueue(tQueue);
        Assert.assertEquals(new Double(1) / new Double(3), recall.calculate());
    }

}
