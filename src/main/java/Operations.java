import java.util.List;

public class Operations {
    List<ConnectionProperties> connections;
    List<Query> queries;

    @Override
    public String toString() {
        return "Operations{" +
                "connections=" + connections +
                ", queries=" + queries +
                '}';
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

    public Operations(List<ConnectionProperties> connections, List<Query> queries) {
        this.connections = connections;
        this.queries = queries;
    }
}
