package com.jdl.localizacion;

import android.os.StrictMode;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    //Atributos de la clase
    public Connection conn = null;
    public String driver = "com.mysql.jdbc.Driver";
    public String localhost = "192.168.1.38";
    //public String localhost = "192.168.1.58";
    public String port = "3306";
    public String dbName = "dbgps";
    public String userName = "root";
    public String password = "admon";

    //Metodo que obtiene la conexion a la base de datos
    public Connection connect(){

        try {
            // Creamos la conexión

            //Cargamos el driver con el conector jdbc
            StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName(driver).newInstance();
            String url = "jdbc:mysql://" + localhost + ":" + port + "/" + dbName;
            conn = DriverManager.getConnection(url, userName, password);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return conn;
    }

    //Metodo que cierra una conexion a la base de datos
    public void disconnect(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //Metodos gets y sets
    public String getHost() {
        return localhost;
    }

    public void setHost(String host) {
        this.localhost = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}

