package com.QG.jdbc;

import operation.Login;
import operation.Register;

import java.util.Scanner;

public class StartMenu {

    //启动系统展示欢迎页面
    public void start() {
        Scanner sc = new Scanner(System.in);
        int command = 0;
        while (command != 3) {
            System.out.println("===========================");
            System.out.println("学生选课管理系统");
            System.out.println("===========================");
            System.out.println("1. 登录");
            System.out.println("2. 注册");
            System.out.println("3. 退出");
            System.out.println("请选择操作（输入 1-3）：");
            while (true) {
                try {
                    command = sc.nextInt();
                    break;
                } catch (Exception e) {
                    System.out.println("输入无效");
                }
                sc.next();
            }
            switch (command) {
                case 1:
                    //登录
                    Login login = new Login();
                    login.login();
                    break;
                case 2:
                    //register
                    Register register = new Register();
                    register.registrationMenu();
                    break;
                case 3:
                    //exit
                    sc.close();
                    break;
                default:
                    System.out.println("没有该操作捏");
            }
        }
    }
}
