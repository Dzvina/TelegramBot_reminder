package com.mdo.teleg_bot.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class CityDataInserter {

    private static final String SQL_INSERT = "INSERT INTO CITY (CITY_NAME, LONGITUDE, LATITUDE, COUNTRY) VALUES (?,?,?,?)";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/reminder?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String FILE_PATH = "src\\main\\resources\\worldcities.csv";
    private static final String USER = "root";
    private static final String PASSWORD = "1111";
    private static final String SEPARATOR = ",";

    public static void main(String[] args) {
        parseCsvAndInsertToDB();
    }

    private static void parseCsvAndInsertToDB() {

        try (Scanner scanner = new Scanner(new File(FILE_PATH));
             Connection connection = DriverManager
                     .getConnection(DB_URL, USER, PASSWORD)
        ) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] responseElements = line.split(SEPARATOR);
                preparedStatement.setString(1, responseElements[0]);
                preparedStatement.setString(2, responseElements[2]);
                preparedStatement.setString(3, responseElements[1]);
                preparedStatement.setString(4, responseElements[3]);

                preparedStatement.executeUpdate();
            }
        } catch (FileNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
