package gr.ntua.imu.escapefilterbubble.topics.data;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 * @author Kostas Christidis
 */
public class DefaultFileSource implements FileSource, Serializable {

    private static FileSource instance;
    private String filePath;
    private HashSet<String> documents;
    private HashSet<String> labels;

    static Logger log = Logger.getLogger(FileSource.class.toString());

    @Override
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public static FileSource getInstance() {
        if (instance == null) {
            instance = new DefaultFileSource();
        }
        return instance;
    }

    @Override
    public void readDocuments() throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));

        String line;
        documents = new HashSet<String>();
        labels = new HashSet<String>();

        br.readLine();
        log.fine("Reading File Input");
        while ((line = br.readLine()) != null) {
            String[] parts = line.split("::");
            documents.add(parts[2]);
            labels.add(parts[0]);
        }
    }


    public HashSet<String> getDocuments() {
        return documents;
    }

    public void setDocuments(HashSet<String> documents) {
        this.documents = documents;
    }

    public HashSet<String> getLabels() {
        return labels;
    }

    public void setLabels(HashSet<String> labels) {
        this.labels = labels;
    }
}
