/*
 * created by Alsu Saifetdinova
 * created: 11/17/2018
 * description:
 * this is sql connector, connects sql tables with java application
 * please see login and password in tables.sql
 */
package utils;

import sample.IHaveLog;
import sample.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLSystem
{
    private final IHaveLog log;
    private Statement sql;
    private String host = "localhost";
    private int port = 3060;
    private String db_name;
    private String username;
    private String password;
    private String db_file;

    public EnumSQL sqlType = EnumSQL.SQLite;

    public enum EnumSQL
    {
        SQLite, MySQL;
    }

    public SQLSystem(final IHaveLog log, EnumSQL type)
    {
        this.log = log;
        this.sqlType = type;
    }

    public void setParams(String host, int port, String db_name, String username) {
        this.setParams(host, port, db_name, username, null);
    }

    public void setParams(String host, int port, String db_name, String username, String password) {
        this.host = host;
        this.port = port;
        this.db_name = db_name;
        this.username = username;
        this.password = password;
    }

    public boolean connectSQL()
    {
        Connection conn = null;
        try
        {
            showMessage( "connectSQL", "initialize" );
            if( sqlType == EnumSQL.SQLite )
            {
                conn = DriverManager.getConnection("jdbc:sqlite:" + db_file );
                sql = conn.createStatement();
                if( sql != null )
                    return true;
            }
            else
            {
                //Class.forName("org.gjt.mm.mysql.Driver");
                if(password != null && (password != null && !password.isEmpty()))
                conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db_name, username, password );
                sql = conn.createStatement();
                if( sql != null )
                    return true;
            }
        } catch (SQLException e) {
            errorMessage( "connectSQL->SQLException", e.getMessage() );
            e.printStackTrace();
        } catch (Exception e) {
            errorMessage( "connectSQL->Exception", e.getMessage() );
            e.printStackTrace();
        }
        return false;
    }

    private void errorMessage(String moduleName, String msg) {
        log.errorMessage(this.getClass().getSimpleName(), moduleName, msg);
    }

    private void showMessage(String moduleName, String msg) {
        log.showMessage( this.getClass().getSimpleName(), moduleName, msg );
    }

    public ResultSet query( String query )
    {
        ResultSet rs = null;

        if( sql == null )
        {
            if( !this.connectSQL() ) return rs;
        }

        try
        {
            //showMessage( "SQLSystem->query", query );
            return sql.executeQuery( query );
        } catch (SQLException e) {
            errorMessage( "SQLSystem->query", e.getMessage() + "\r\n" + query );
            e.printStackTrace();
        }
        return rs;
    }

    public boolean update( String query )
    {
        if( sql == null )
        {
            if( !this.connectSQL() ) return false;
        }

        try
        {
            //showMessage( "SQLSystem->update", query );
            sql.executeUpdate( query );
            return true;
        } catch (SQLException e) {
            errorMessage( "SQLSystem->query", e.getMessage() + "\r\n" + query );
            e.printStackTrace();
        }
        return false;
    }

    public void close() {
        try {
            sql.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

