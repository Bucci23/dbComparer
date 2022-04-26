public class Query {
    String name;
    String query;

    @Override
    public String toString() {
        return "Query{" +
                "name='" + name + '\'' +
                ", query='" + query + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Query(String name, String query) {
        this.name = name;
        this.query = query;
    }
}
