package gr.ntua.imu.escapefilterbubble;

import gr.ntua.imu.escapefilterbubble.metric.MetricsTest;
import gr.ntua.imu.escapefilterbubble.topics.analyzer.AnalyzerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AnalyzerTest.class,
        MvcTest.class,
        MetricsTest.class
})
public class SpringAppTests {
}
