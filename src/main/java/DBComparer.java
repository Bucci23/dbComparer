import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;

public class DBComparer {
    public static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String POSTGRESQL_DRIVER = "mssql-jdbc-8.4.1.jre8.jar";
    public static final String ORACLE_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String SQLSERVER_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    public String MssqlUrlBuilder(String serverName, String portNumber, String databaseName, String userName, String password) {
        return "jdbc:sqlserver://" + serverName + ":" + portNumber + ";databaseName=" + databaseName + ";" + "user=" + userName + ";password=" + password;
    }

    public boolean compareResultSets(ResultSet resultSet1, ResultSet resultSet2) throws SQLException {
        while (resultSet1.next()) {
            resultSet2.next();
            ResultSetMetaData resultSetMetaData = resultSet1.getMetaData();
            int count = resultSetMetaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                if (!resultSet1.getObject(i).equals(resultSet2.getObject(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public void printResultSet(ResultSet rs) throws SQLException {
        int colNumber = rs.getMetaData().getColumnCount();
        System.out.println("Number of columns: " + colNumber);
        for (int i = 1; i <= colNumber; ++i)
            System.out.print(rs.getMetaData().getColumnLabel(i) + ", TYPE: " + rs.getMetaData().getColumnTypeName(i) + "; ");
        System.out.println("VALUES:");
        while (rs.next()) {
            for (int i = 1; i <= colNumber; ++i)
                System.out.print(rs.getString(i) + ";  ");
            System.out.println("");
        }
    }

    public ResultSet getResultSet(String url, String query) {
        System.out.println("Connecting to url: " + url);

        try {
            DBManager.setConnection(SQLSERVER_DRIVER, url);
            Connection c = DBManager.getConnection();
            System.out.println(c.getMetaData());
            Statement statement = c.createStatement();
            ResultSet rs = statement.executeQuery(query);
            System.out.println("Query Executed");
            printResultSet(rs);
            return rs;
        } catch (SQLException e) {
            System.out.println("Something is wrong: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        DBComparer dbComparer = new DBComparer();
        ResultSet rs1 = dbComparer.getResultSet(dbComparer.MssqlUrlBuilder("localhost", "1433", "CALCIO", "user", "123"), "select * from calciatore");
        ResultSet rs2 = dbComparer.getResultSet(dbComparer.MssqlUrlBuilder("localhost", "1433", "DB_CONCORSO_FOTOGRAFICO", "user", "123"),"select * from fotografo");

        try {
            System.out.println(dbComparer.compareResultSets(rs1, rs2));

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
