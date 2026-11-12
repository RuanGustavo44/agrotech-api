
package com.agrotech;

import com.agrotech.database.DatabaseConnection;
import com.agrotech.server.ApiServer;

public class Main {

    public static void main(String[] args) {

        System.out.println("=== AGROTECH API ===");

        DatabaseConnection.initializeDatabase();

        ApiServer.start();
    }
}
