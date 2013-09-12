package oyyt.DroneDownloader.Data;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

import org.sqlite.JDBC;

public class SQLiteConnection {
    private String url;
    private String filePath;
    private String fileName;
    static String driver;
    static String connUrl;

    public SQLiteConnection() {
    }

    public SQLiteConnection(String url, String filePath, String fileName) {
        this.url = url;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public void init() {
        try { 
            Properties props = new Properties();
            props.load(new FileInputStream("E:/Workspace/DroneDownloader/sqlite/sql.ini"));
            driver = props.getProperty("driver");
            connUrl = props.getProperty("url");
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(connUrl);
            Statement stat = conn.createStatement();
            stat.executeUpdate("create table if not exists user ( fileName varchar(225), fileSize varchar(255), speed varchar(255), proportion varchar(255), state varchar(225))");     
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertData() {
        String insertSql = "insert into user(fileName,fileSize,state) values (?,?,?)";
        try{
            Connection conn = DriverManager.getConnection(connUrl);
            PreparedStatement preStat =conn.prepareStatement(insertSql);
            preStat.setString(1, this.fileName);
            preStat.setString(2, "0");
            preStat.setString(3, "Downloading");
            preStat.execute();
        }
         catch (Exception e){
             e.printStackTrace();
         }
    }
    
    public ResultSet queryData(){
        ResultSet result = null;
        try {
        Connection conn = DriverManager.getConnection(connUrl);
        Statement stat = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY
                , ResultSet.CONCUR_READ_ONLY);
        result = stat.executeQuery("select * from user");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
        
    }

}
