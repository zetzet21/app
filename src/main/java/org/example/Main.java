package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {

    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "1234";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/maDatabase";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

        while (true) {
            System.out.println("1. Показать список всех задач");
            System.out.println("2. Выполнить задачу");
            System.out.println("3. Создать задачу");
            System.out.println("4. Выйти");

            int command = scanner.nextInt();

            if (command == 1) {
                Statement statement = connection.createStatement();
                String SQL_SELECT_TASKS = "select * from task order by id desc";
                ResultSet result = statement.executeQuery(SQL_SELECT_TASKS);

                while (result.next()) {
                    System.out.println(result.getInt("id") + " " + result.getString("task")
                            + " " + result.getString(("state")));
                }
            } else if (command == 2) {
                String SQL_UPDATE_TASKS = "update task set state = 'DONE' where id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_TASKS);
                System.out.println("Введите id задачи: ");
                int taskId = scanner.nextInt();
                preparedStatement.setInt(1, taskId);
                preparedStatement.executeUpdate();
            } else if (command == 3) {
                String sql = "insert into task(task, state) values (?, ?);";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                System.out.println("Введите название задачи: ");
                scanner.nextLine();
                String taskName = scanner.nextLine();

                System.out.println("Введите статус задачи: ");
                String taskState = scanner.nextLine();

                preparedStatement.setString(1, taskName);
                preparedStatement.setString(2, taskState);
                preparedStatement.executeUpdate();
            } else if (command == 4) {
                System.exit(0);
            } else {
                System.err.println("Команда не распознана");
            }
        }
    }
}