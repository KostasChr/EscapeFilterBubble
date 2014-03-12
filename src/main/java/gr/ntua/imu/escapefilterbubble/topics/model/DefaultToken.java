package gr.ntua.imu.escapefilterbubble.topics.model;

/**
 * @author KostasChr
 */
public class DefaultToken implements Token, Comparable<Token> {

    private Double probability;
    private String token;

    public DefaultToken(String word1, Double v) {
        this.probability = v;
        this.token = word1;

    }

    public Double getProbability() {
        return probability;
    }


    public String getToken() {
        return token;
    }


    public void setProbability(Double probability) {
        this.probability = probability;

    }

    public void setToken(String token) {
        this.token = token;
    }


    @Override
    public int compareTo(Token token) {
        return Double.compare(token.getProbability(), this.getProbability());
    }
}
