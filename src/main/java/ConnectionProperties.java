import java.sql.*;

public class ConnectionProperties {
    String name;
    String database_type;
    int port;
    String username;
    String password;
    String host;
    String database;
    String schema;

    public ConnectionProperties(String name, String database_type, int port, String username, String password, String host, String database) {
        this.name = name;
        this.database_type = database_type;
        this.port = port;
        this.username = username;
        this.password = password;
        this.host = host;
        this.database = database;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getUrl() {
        if (database_type.equals("mysql"))
            return "jdbc:mysql://" + username + ":" + password + "@" + host + ":" + port + "/" + database;
        if (database_type.equals("mssql"))
            return "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + database + ";" + "user=" + username + ";password=" + password;
        if (database_type.equals("postgresql"))
            return "jdbc:postgresql://" + host + ":" + port + "/" + database + "?user=" + username + "&password=" + password + "&currentSchema=" + schema;
        if(database_type.equals("oracle"))
            return "jdbc:oracle:thin:" + username + "/" + password + "@" + host +":"+ port + "/" + database;
        return null;
    }

    public String getDriver(){
        if (database_type.equals("mysql"))
            return "com.mysql.cj.jdbc.Driver";
        if (database_type.equals("postgresql"))
            return "org.postgresql.Driver ";
        if (database_type.equals("oracle"))
            return "oracle.jdbc.driver.OracleDriver";
        if (database_type.equals("mssql"))
            return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        else{
            System.out.println("DATABASE TYPE NOT SUPPORTED");
            return null;
        }
    }

    public void extraActions(Connection c) throws SQLException{
        if(database_type.equals("oracle")) {
            Statement s = c.createStatement();
            s.executeQuery("ALTER SESSION SET CURRENT_SCHEMA = \"" + schema + "\"");
        }
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
