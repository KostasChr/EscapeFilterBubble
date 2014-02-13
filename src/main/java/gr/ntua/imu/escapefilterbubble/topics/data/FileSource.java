package gr.ntua.imu.escapefilterbubble.topics.data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 * @author Kostas Christidis
 */
public class FileSource implements Source {

    private static Source instance;
    private String filePath;
    private HashSet<String> documents;
    private HashSet<String> labels;

    static Logger log = Logger.getLogger(FileSource.class.toString());

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public static Source getInstance() {
        if (instance == null) {
            instance = new FileSource();
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
