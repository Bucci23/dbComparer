import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBComparer {
    DBManager manager;
    JsonHandler jsonHandler;
    Operations o;
    ConnectionProperties golden;
    String inputFilename;
    String outputFilename;

    public DBComparer(String inputFilename, String outputFilename) {
        jsonHandler = new JsonHandler();
        this.inputFilename = inputFilename;
        this.outputFilename = outputFilename;
        o = jsonHandler.jsonToConnnection(inputFilename);
        golden = o.getGoldenDB();
    }

    public String compareResultSets(ResultSet resultSet1, ResultSet resultSet2) throws SQLException {
        StringBuilder msg = new StringBuilder("");
        boolean headerFlag = false;
        boolean valuesFlag = false;
        resultSet1.first();
        resultSet2.first();
        if (resultSet1.getMetaData().getColumnCount() != resultSet2.getMetaData().getColumnCount()) {
            return "not ok: different number of columns: Reference: " + getRSHeader(resultSet1) + "\nvs: " + getRSHeader(resultSet2);
        }
        for(int i = 1; i<=resultSet1.getMetaData().getColumnCount(); ++i){
            if(!(resultSet1.getMetaData().getColumnLabel(i).equals(resultSet2.getMetaData().getColumnLabel(i)))){
                headerFlag = true;
                msg.append("Different column name! column ").append(i).append(": reference ").append(resultSet1.getMetaData().getColumnLabel(i)).append(" != ").append(resultSet2.getMetaData().getColumnLabel(i)).append("\n");
            }
            if(resultSet1.getMetaData().getColumnType(i) != resultSet2.getMetaData().getColumnType(i)){
                headerFlag = true;
                msg.append("Different column type! column ").append(i).append(": reference ").append(resultSet1.getMetaData().getColumnTypeName(i)).append(" != ").append(resultSet2.getMetaData().getColumnTypeName(i)).append("\n");

            }
        }


        int counter = 0;
        do {
            counter++;

            int count = resultSet1.getMetaData().getColumnCount();
            for (int i = 1; i <= count; i++) {
                //System.out.println("analyzing row " + counter + " column: " + i + "  " + resultSet1.getString(i) + "  " + resultSet2.getString(i));
                if (resultSet1.getString(i) != null && resultSet2.getString(i) != null) {
                    if (!resultSet1.getObject(i).equals(resultSet2.getObject(i))) {
                        if(!resultSet1.getObject(i).toString().equals(resultSet2.getObject(i).toString())) {
                            valuesFlag = true;
                            msg.append("row ").append(counter).append(" is different: ").append(resultSet1.getMetaData().getColumnLabel(i)).append(": ").append(resultSet1.getString(i)).append(" != ").append(resultSet2.getString(i)).append("\n");
                        }
                    }
                } else if ((resultSet1.getString(i) == null && resultSet2.getString(i) != null) || (resultSet1.getString(i) != null && resultSet2.getString(i) == null)) {
                    valuesFlag = true;
                    msg.append("row ").append(counter).append(" is different: ").append(resultSet1.getMetaData().getColumnLabel(i)).append(": ").append(resultSet1.getString(i)).append(" != ").append(resultSet2.getString(i)).append("\n");
                }
            }
            resultSet1.next();
            resultSet2.next();
        } while (!(resultSet1.isAfterLast()) && !(resultSet2.isAfterLast()));
        if((resultSet1.isAfterLast() && !resultSet2.isAfterLast()) || (!resultSet1.isAfterLast() && resultSet2.isAfterLast())){
            resultSet1.last();
            resultSet2.last();
            msg.append("different number of rows! Reference has ").append(resultSet1.getRow()).append(" vs ").append(resultSet2.getRow());
            valuesFlag = true;
        }
        if(headerFlag && !valuesFlag)
            return msg.append("Values: ok.").toString();
        else if(!headerFlag && valuesFlag){
            return "Columns: ok. Values: \n" + msg.toString();
        }
        else if(headerFlag){
            return msg.toString();
        }
        else return "Everything Ok";
    }

    public String getRSHeader(ResultSet rs) throws SQLException {
        int colNumber = rs.getMetaData().getColumnCount();
        StringBuilder msg = new StringBuilder("Number of columns: " + colNumber);
        for (int i = 1; i <= colNumber; ++i)
            msg.append(rs.getMetaData().getColumnLabel(i)).append(", TYPE: ").append(rs.getMetaData().getColumnTypeName(i)).append("; ");
        msg.append("\n");
        return msg.toString();

    }

    public String getRSRow(ResultSet rs, int colNumber) throws SQLException {
        StringBuilder msg = new StringBuilder();
        for (int i = 1; i <= colNumber; ++i)
            msg.append(rs.getString(i)).append(";  ");
        return msg.toString();
    }


    public String printResultSet(ResultSet rs) {
        try {
            int colNumber = rs.getMetaData().getColumnCount();
            StringBuilder msg = new StringBuilder(getRSHeader(rs));
            while (rs.next()) {
                msg.append("\n").append(getRSRow(rs, colNumber));
            }
            return msg.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getResultSet(ConnectionProperties cProp, String query) {
        //System.out.println("Connecting to url: " + cProp.getUrl());
        manager = new DBManager();

        try {
            manager.setConnection(cProp.getDriver(), cProp.getUrl());
            Connection c = manager.getConnection();
            Statement statement = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            cProp.extraActions(c);
            //System.out.println("Query Executed");
            //printResultSet(rs);
            return statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Something is wrong: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    public void printResults(String msg){
        try {
            FileWriter fw = new FileWriter(outputFilename);
            fw.append("OUTPUT: \n").append(msg);
            fw.flush();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void run() {
        List<ResultSet> resultSets = new ArrayList<>();
        List<ResultSet> goldenResultSets = new ArrayList<>();
        StringBuilder result=new StringBuilder();
        for (Query i : o.getQueries())
            goldenResultSets.add(getResultSet(golden, i.getQuery()));
        int k = 0;
        try {
            for (ConnectionProperties i : o.getConnections()) {
                int h = 0;
                for (Query j : o.getQueries()) {
                    if(!golden.name.equals(i.name)) {
                        result.append("Comparing ").append(golden.name).append(" and ").append(i.name).append(" using query: ").append(j.getName()).append("\n").append(compareResultSets(goldenResultSets.get(h), getResultSet(i, j.getQuery())));
                    }
                    h++;
                    k++;
                }
                printResults(result.toString());
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {

        DBComparer dbComparer = new DBComparer(args[0], args[1]);
        dbComparer.run();
    }
}
