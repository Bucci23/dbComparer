import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;

public class DBComparer {
    DBManager manager;
    JsonHandler jsonHandler;
    public static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String POSTGRESQL_DRIVER = "mssql-jdbc-8.4.1.jre8.jar";
    public static final String ORACLE_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String SQLSERVER_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    public String MssqlUrlBuilder(String serverName, String portNumber, String databaseName, String userName, String password) {
        return "jdbc:sqlserver://" + serverName + ":" + portNumber + ";databaseName=" + databaseName + ";" + "user=" + userName + ";password=" + password;
    }

    public String compareResultSets(ResultSet resultSet1, ResultSet resultSet2) throws SQLException {
        StringBuilder msg = new StringBuilder("ok");
        resultSet1.first();
        resultSet2.first();
        if (resultSet1.getMetaData().getColumnCount() != resultSet2.getMetaData().getColumnCount()) {
            return "not ok: different number of columns " ;

        }
        int counter = 0;
        do{
            counter++;

            int count = resultSet1.getMetaData().getColumnCount();
            for (int i = 1; i <= count; i++) {
                System.out.println("analyzing row " + counter + " column: "+ i + "  " + resultSet1.getString(i) + "  " + resultSet2.getString(i));
                if(resultSet1.getString(i) != null && resultSet2.getString(i) != null) {
                    if (!resultSet1.getString(i).equals(resultSet2.getString(i))) {
                        msg.append("riga ").append(counter).append("sbagliata: ").append(resultSet1.getString(i)).append(" != ").append(resultSet2.getString(i));
                    } else if((resultSet1.getString(i) == null && resultSet2.getString(i) != null) || (resultSet1.getString(i) != null && resultSet2.getString(i) == null)){
                        msg.append("riga ").append(counter).append("sbagliata: ").append(resultSet1.getString(i)).append(" != ").append(resultSet2.getString(i));
                    }
                }
            }
            resultSet2.next();
        }while(resultSet1.next() && resultSet2.next());
        return msg.toString();
    }

    public String getRSHeader(ResultSet rs) throws SQLException {
        int colNumber = rs.getMetaData().getColumnCount();
        StringBuilder msg = new StringBuilder("Number of columns: " + colNumber);
        for (int i = 1; i <= colNumber; ++i)
            msg.append(rs.getMetaData().getColumnLabel(i)).append(", TYPE: ").append(rs.getMetaData().getColumnTypeName(i)).append("; ");
        msg.append("\n");
        return msg.toString();

    }

    public String getRSRow (ResultSet rs, int colNumber) throws SQLException{
        StringBuilder msg = new StringBuilder();
        for (int i = 1; i <= colNumber; ++i)
            msg.append(rs.getString(i)).append(";  ");
        return msg.toString();
    }



    public String printResultSet(ResultSet rs) throws SQLException {
        int colNumber = rs.getMetaData().getColumnCount();
        StringBuilder msg = new StringBuilder(getRSHeader(rs));
        while (rs.next()) {
            msg.append("\n").append(getRSRow(rs, colNumber));
        }
        return msg.toString();
    }

    public ResultSet getResultSet(String url, String query) {
        System.out.println("Connecting to url: " + url);
        manager = new DBManager();

        try {
            manager.setConnection(SQLSERVER_DRIVER, url);
            Connection c = manager.getConnection();
            Statement statement = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = statement.executeQuery(query);
            System.out.println("Query Executed");
            //printResultSet(rs);
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
        ResultSet rs2 = dbComparer.getResultSet(dbComparer.MssqlUrlBuilder("localhost", "1433", "CALCIOdump2", "user", "123"), "select * from calciatore");

        try {
            System.out.println("RESULT SET 1: \n" + dbComparer.printResultSet(rs1));
            System.out.println("RESULT SET 2: \n" + dbComparer.printResultSet(rs2));
            System.out.println(dbComparer.compareResultSets(rs1, rs2));
            dbComparer.jsonHandler = new JsonHandler();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
