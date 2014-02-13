package gr.ntua.imu.escapefilterbubble.topics.data;

import java.io.IOException;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Kostas
 * Date: 9/1/2014
 * Time: 10:55 μμ
 * To change this template use File | Settings | File Templates.
 */
public interface Source {
    Set<String> getDocuments();

    Set<String> getLabels();

    void readDocuments() throws IOException;
}
