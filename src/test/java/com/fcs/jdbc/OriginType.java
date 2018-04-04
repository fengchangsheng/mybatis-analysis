package com.fcs.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 从这几个基本的步骤去看Mybatis对应的操作
 */
public class OriginType {

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/design", "root", "fengcs");

            String sql = "select * from tb_user";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.executeQuery();

            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                System.out.println("===================>"+ id);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
