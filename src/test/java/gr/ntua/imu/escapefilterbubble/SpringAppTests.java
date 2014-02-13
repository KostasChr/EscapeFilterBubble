package gr.ntua.imu.escapefilterbubble;

import gr.ntua.imu.escapefilterbubble.topics.analyzer.AnalyzerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AnalyzerTest.class,
        MvcTest.class
})
public class SpringAppTests {
}
