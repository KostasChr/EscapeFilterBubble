package gr.ntua.imu.escapefilterbubble.topics.data;

import java.io.IOException;
import java.util.Set;

/**
 * @author KostasChr
 */
public interface FileSource {
    Set<String> getDocuments();

    Set<String> getLabels();

    void readDocuments() throws IOException;

    void setFilePath(String filePath);
}
