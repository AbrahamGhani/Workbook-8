package com.dreamcar.dao;

public class DatabaseConnection {

    private static final String url = "jdbc:sqlserver://skills4it.database.windows.net:1433;" +
                "database=Courses;" +
                "user=user416@skills4it;" +
                "password=YearupSecure2025!;" +
                "encrypt=true;" +
                "trustServerCertificate=false;" +
                "loginTimeout=30;";

 public String getUrl(){
     return url;
 }


}
