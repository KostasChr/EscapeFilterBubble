package gr.ntua.imu.escapefilterbubble.topics.analyzer;

import gr.ntua.imu.escapefilterbubble.topics.data.FileSource;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;

import static org.mockito.Mockito.when;

/**
 * @author KostasChr
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class AnalyzerTest {


    private static HashSet<String> mockDocumentSet;
    private static HashSet<String> mockLabelSet;


    @BeforeClass
    public static void initializeTestVariables() {
        mockDocumentSet = new HashSet<String>();
        mockDocumentSet.add("test test test test test2 ");
        mockDocumentSet.add("test2 sadf test test ");
        mockLabelSet = new HashSet<String>();
        mockLabelSet.add("test");
        mockLabelSet.add("test2");
    }

    @Autowired
    private DefaultAnalyzer analyzer;

    @Test
    public void testEstimation() {
        FileSource mockSource = Mockito.mock(FileSource.class);
        when(mockSource.getDocuments()).thenReturn(mockDocumentSet);
        when(mockSource.getLabels()).thenReturn(mockLabelSet);
        analyzer.setSource(mockSource);
        analyzer.loadTrainSet();
        analyzer.estimate();
        Assert.assertNotNull(analyzer.getWordsForTopic(0));
    }

}
