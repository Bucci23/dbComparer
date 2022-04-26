
public class ConnectionProperties {
    String name;
    String database_type;
    int port;
    String username;
    String password;
    String host;
    String database;

    public ConnectionProperties(String name, String database_type, int port, String username, String password, String host, String database) {
        this.name = name;
        this.database_type = database_type;
        this.port = port;
        this.username = username;
        this.password = password;
        this.host = host;
        this.database = database;
    }

    @Override
    public String toString() {
        return "ConnectionProperties{" +
                "name='" + name + '\'' +
                ", database_type='" + database_type + '\'' +
                ", port=" + port +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", host='" + host + '\'' +
                ", database='" + database + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatabase_type() {
        return database_type;
    }

    public void setDatabase_type(String database_type) {
        this.database_type = database_type;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}
