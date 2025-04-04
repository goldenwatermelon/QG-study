package com.QG.jdbc;

import entity.User;
import operation.AdminOperation;
import role.StudentMenu;

import java.sql.Connection;
import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException {
       StartMenu startMenu = new StartMenu();
       startMenu.start();
    }
}


