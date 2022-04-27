import java.util.List;

public class Operations {
    List<ConnectionProperties> connections;
    List<Query> queries;
    String golden;

    @Override
    public String toString() {
        return "Operations{" +
                "connections=" + connections +
                ", queries=" + queries +
                ", golden='" + golden + '\'' +
                '}';
    }

    public String getGolden() {
        return golden;
    }

    public void setGolden(String golden) {
        this.golden = golden;
    }

    public List<ConnectionProperties> getConnections() {
        return connections;
    }

    public void setConnections(List<ConnectionProperties> connections) {
        this.connections = connections;
    }

    public List<Query> getQueries() {
        return queries;
    }

    public void setQueries(List<Query> queries) {
        this.queries = queries;
    }

    public Operations(List<ConnectionProperties> connections, List<Query> queries, String golden) {
        this.connections = connections;
        this.queries = queries;
        this.golden = golden;
    }
}
