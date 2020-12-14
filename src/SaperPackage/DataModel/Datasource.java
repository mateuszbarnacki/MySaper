package SaperPackage.DataModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {

    public static final String DB_NAME = "saper_winners.db";

    public static final String CONNECTION_STRING = "jdbc:sqlite:D:\\Programming\\SQL\\Databases\\" + DB_NAME;

    public static final String TABLE_EASY = "easy";
    public static final String TABLE_EASY_NAME = "name";
    public static final String TABLE_EASY_TIME = "time";
    public static final int INDEX_EASY_NAME = 1;
    public static final int INDEX_EASY_TIME = 2;

    public static final String TABLE_MEDIUM = "medium";
    public static final String TABLE_MEDIUM_NAME = "name";
    public static final String TABLE_MEDIUM_TIME = "time";
    public static final int INDEX_MEDIUM_NAME = 1;
    public static final int INDEX_MEDIUM_TIME = 2;

    public static final String TABLE_HARD = "hard";
    public static final String TABLE_HARD_NAME = "name";
    public static final String TABLE_HARD_TIME = "time";
    public static final int INDEX_HARD_NAME = 1;
    public static final int INDEX_HARD_TIME = 2;

    public static final String TABLE_CUSTOM = "custom";
    public static final String TABLE_CUSTOM_NAME = "name";
    public static final String TABLE_CUSTOM_TIME = "time";
    public static final String TABLE_CUSTOM_TYPE = "type";
    public static final int INDEX_CUSTOM_NAME = 1;
    public static final int INDEX_CUSTOM_TIME = 2;
    public static final int INDEX_CUSTOM_TYPE = 3;

    public static final String CREATE_EASY_WINNERS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_EASY +
                    "(" + TABLE_EASY_NAME + " text, " + TABLE_EASY_TIME + " text)";

    public static final String CREATE_MEDIUM_WINNERS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_MEDIUM +
                    "(" + TABLE_MEDIUM_NAME + " text, " + TABLE_MEDIUM_TIME + " text)";

    public static final String CREATE_HARD_WINNERS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_HARD +
                    "(" + TABLE_HARD_NAME + " text, " + TABLE_HARD_TIME + " text)";

    public static final String CREATE_CUSTOM_WINNERS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_CUSTOM +
                    "(" + TABLE_CUSTOM_NAME + " text, " + TABLE_CUSTOM_TIME + " text, " + TABLE_CUSTOM_TYPE + " text)";

    public static final String INSERT_EASY_WINNER =
            "INSERT INTO " + TABLE_EASY + " (" + TABLE_EASY_NAME + ", " + TABLE_EASY_TIME + ") VALUES(?, ?)";

    public static final String QUERY_EASY_WINNERS =
            "SELECT * FROM " + TABLE_EASY + " ORDER BY " + TABLE_EASY_TIME;

    public static final String INSERT_MEDIUM_WINNER =
            "INSERT INTO " + TABLE_MEDIUM + " (" + TABLE_EASY_NAME + ", " + TABLE_MEDIUM_TIME + ") VALUES(?, ?)";

    public static final String QUERY_MEDIUM_WINNERS =
            "SELECT * FROM " + TABLE_MEDIUM + " ORDER BY " + TABLE_MEDIUM + "." + TABLE_MEDIUM_TIME;

    public static final String INSERT_HARD_WINNER =
            "INSERT INTO " + TABLE_HARD + " (" + TABLE_HARD_NAME + ", " + TABLE_HARD_TIME + ") VALUES(?, ?)";

    public static final String QUERY_HARD_WINNERS =
            "SELECT * FROM " + TABLE_HARD + " ORDER BY " + TABLE_HARD + "." + TABLE_HARD_TIME;

    public static final String INSERT_CUSTOM_WINNER =
            "INSERT INTO " + TABLE_CUSTOM + " (" + TABLE_CUSTOM_NAME + ", " + TABLE_CUSTOM_TIME + ", " + TABLE_CUSTOM_TYPE + ") VALUES(?, ?, ?)";

    public static final String QUERY_CUSTOM_WINNERS =
            "SELECT " + TABLE_CUSTOM_NAME + ", " + TABLE_CUSTOM_TIME + " FROM " + TABLE_CUSTOM +
                    " WHERE " + TABLE_CUSTOM_TYPE + " = ? ORDER BY " + TABLE_CUSTOM_TIME;

    private PreparedStatement queryInsertEasyWinner;
    private PreparedStatement queryInsertMediumWinner;
    private PreparedStatement queryInsertHardWinner;
    private PreparedStatement queryInsertCustomWinner;
    private PreparedStatement queryCustomWinners;

    private Connection connection;
    private final static Datasource instance = new Datasource();

    private Datasource(){}

    public static Datasource getInstance(){
        return instance;
    }

    public boolean open(){
        try{
            connection = DriverManager.getConnection(CONNECTION_STRING);

            createEasyWinnersTable();
            createMediumWinnersTable();
            createHardWinnersTable();
            createCustomWinnersTable();

            queryInsertEasyWinner = connection.prepareStatement(INSERT_EASY_WINNER);
            queryInsertMediumWinner = connection.prepareStatement(INSERT_MEDIUM_WINNER);
            queryInsertHardWinner = connection.prepareStatement(INSERT_HARD_WINNER);
            queryInsertCustomWinner = connection.prepareStatement(INSERT_CUSTOM_WINNER);
            queryCustomWinners = connection.prepareStatement(QUERY_CUSTOM_WINNERS);

            return true;
        }catch (SQLException e){
            System.out.println("Couldn't connect: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean close(){
        try{
            if(queryInsertEasyWinner != null){
                queryInsertEasyWinner.close();
            }
            if(queryInsertMediumWinner != null){
                queryInsertMediumWinner.close();
            }
            if(queryInsertHardWinner != null){
                queryInsertHardWinner.close();
            }
            if(queryInsertCustomWinner != null){
                queryInsertCustomWinner.close();
            }
            if(queryCustomWinners != null){
                queryCustomWinners.close();
            }
            if(connection != null){
                connection.close();
            }
            return true;
        }catch (SQLException e){
            System.out.println("Couldn't close the connection: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void createEasyWinnersTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(CREATE_EASY_WINNERS_TABLE);
    }

    private void createMediumWinnersTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(CREATE_MEDIUM_WINNERS_TABLE);
    }

    private void createHardWinnersTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(CREATE_HARD_WINNERS_TABLE);
    }

    private void createCustomWinnersTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(CREATE_CUSTOM_WINNERS_TABLE);
    }

    public List<TimeResult> queryEasyWinners(){
        List<TimeResult> easyWinners = new ArrayList<>();

        try(Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(QUERY_EASY_WINNERS)){

            while(results.next()){
                TimeResult timeResult = new TimeResult();
                timeResult.setName(results.getString(1));
                timeResult.setTime(results.getString(2));
                easyWinners.add(timeResult);
            }

            return easyWinners;
        }catch (SQLException e){
            System.out.println("Couldn't query easy winners: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<TimeResult> queryMediumWinners(){
        List<TimeResult> mediumWinners = new ArrayList<>();

        try(Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(QUERY_MEDIUM_WINNERS)){

            while(results.next()){
                TimeResult timeResult = new TimeResult();
                timeResult.setName(results.getString(1));
                timeResult.setTime(results.getString(2));
                mediumWinners.add(timeResult);
            }

            return mediumWinners;
        }catch (SQLException e){
            System.out.println("Couldn't query medium winners: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<TimeResult> queryHardWinners(){
        List<TimeResult> hardWinners = new ArrayList<>();

        try(Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(QUERY_HARD_WINNERS)){

            while(results.next()){
                TimeResult timeResult = new TimeResult();
                timeResult.setName(results.getString(1));
                timeResult.setTime(results.getString(2));
                hardWinners.add(timeResult);
            }

            return hardWinners;
        }catch (SQLException e){
            System.out.println("Couldn't query hard winners: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<TimeResult> queryCustomWinners(String type){
        List<TimeResult> customWinners = new ArrayList<>();

        try{
            queryCustomWinners.setString(1, type);
            ResultSet results = queryCustomWinners.executeQuery();

            while(results.next()){
                TimeResult timeResult = new TimeResult();
                timeResult.setName(results.getString(1));
                timeResult.setTime(results.getString(2));
                customWinners.add(timeResult);
            }

            results.close();
            return customWinners;
        }catch (SQLException e){
            System.out.println("Couldn't query custom winners: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean insertEasyWinner(String name, String time){
        try {
            queryInsertEasyWinner.setString(1, name);
            queryInsertEasyWinner.setString(2, time);
            int affectedRows = queryInsertEasyWinner.executeUpdate();

            return affectedRows == 1;
        }catch (SQLException e){
            System.out.println("Couldn't add easy winner: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertMediumWinner(String name, String time){
        try {
            queryInsertMediumWinner.setString(1, name);
            queryInsertMediumWinner.setString(2, time);
            int affectedRows = queryInsertMediumWinner.executeUpdate();

            return affectedRows == 1;
        }catch (SQLException e){
            System.out.println("Couldn't add medium winner: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertHardWinner(String name, String time){
        try {
            queryInsertHardWinner.setString(1, name);
            queryInsertHardWinner.setString(2, time);
            int affectedRows = queryInsertHardWinner.executeUpdate();

            return affectedRows == 1;
        }catch (SQLException e){
            System.out.println("Couldn't add hard winner: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertCustomWinner(String name, String time, String type){
        try {
            queryInsertCustomWinner.setString(1, name);
            queryInsertCustomWinner.setString(2, time);
            queryInsertCustomWinner.setString(3, type);
            int affectedRows = queryInsertCustomWinner.executeUpdate();

            return affectedRows == 1;
        }catch (SQLException e){
            System.out.println("Couldn't add custom winner: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}