package gr.ntua.imu.escapefilterbubble.topics.model;

import java.util.PriorityQueue;

/**
 * @author KostasChr
 */

public interface Topic {
    Integer getId();

    PriorityQueue<Token> getTokens();

    Double getProbability();

    public void setId(Integer id);

    public void setTokens(PriorityQueue<Token> tokens);

    public void setProbability(Double probability);


}
