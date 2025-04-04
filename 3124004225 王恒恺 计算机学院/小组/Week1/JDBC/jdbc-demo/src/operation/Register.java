package operation;

import com.QG.jdbc.DatabaseUtil;
import entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Register {
    //完成注册操作
    public void registrationMenu() {
        ArrayList<User> User = new ArrayList<>();//
        User user = new User();
        Scanner sc = new Scanner(System.in);
        System.out.println("===== 用户注册 =====");
        System.out.println("请输入用户名：");
        System.out.println("> ");
        String name = sc.next();

        while (true) {
            System.out.println("请输入密码：\n> ");
            String passwordStr = sc.next();
            System.out.println("请确认密码：\n> ");
            String checkPasswordStr = sc.next();
            if (checkPasswordStr.equals(passwordStr)) {
                user.setPassword(passwordStr);
                break;
            } else {
                System.out.println("您输入的2次密码不一致，请重新输入\n");
            }
        }
        while (true) {
            System.out.println("\n请选择角色（输入 1 代表学生，2 代表管理员）：\n");
            if (sc.hasNextInt()) {
                int role = sc.nextInt();
                if (role == 1 || role == 2) {
                    user.setName(name);
                    user.setRole(role);
                    break;
                } else {
                    System.out.println("只有两种角色类型捏\n");
                }
            } else {
                System.out.println("只能输入1或2噢\n");
                sc.nextLine();
            }
        }
        //存入账户集合
        User.add(user);
        try (Connection conn = DatabaseUtil.getConnection()){
            if (conn != null) {
                conn.setAutoCommit(false);
            }
            String sql = "insert into users(password,role,name) values(?,?,?)";
            try (PreparedStatement pstmt1 = conn.prepareStatement(sql)){
                pstmt1.setString(1,user.getPassword());
                pstmt1.setInt(2,user.getRole());
                pstmt1.setString(3,user.getName());
                int rows = pstmt1.executeUpdate();
                if (rows > 0){
                    conn.commit();
                    System.out.println("注册成功！请返回主界面登录。\n");
                    DatabaseUtil.close(conn);
                }
            }catch (SQLException e){
                conn.rollback();
                System.out.println("数据库错误" + e.getMessage());
            }
        }catch (SQLException e){
            System.out.println("连接数据库失败: " + e.getMessage());
        }

        try (Connection conn = DatabaseUtil.getConnection()){
            String sql = "select id from users where name = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setString(1, user.getName());
                try (ResultSet rs = pstmt.executeQuery()){
                    while (rs.next()){
                        user.setId(rs.getInt("id"));
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            System.out.println("连接数据库失败: " + e.getMessage());
        }

        if (user.getRole() == 1) {
            try (Connection conn = DatabaseUtil.getConnection()){
                if (conn != null) {
                    conn.setAutoCommit(false);
                }
                String sql = "insert into students(user_id) values(?)";
                try (PreparedStatement pstmt2 = conn.prepareStatement(sql)){
                    pstmt2.setInt(1,user.getId());
                    int rows2 = pstmt2.executeUpdate();
                    if (rows2 > 0) {
                        conn.commit();
                        DatabaseUtil.close(conn);
                    }
                } catch (SQLException e) {
                    conn.rollback();
                    throw new RuntimeException(e);
                }
            }catch (SQLException e){
                System.out.println("连接数据库失败: " + e.getMessage());
            }
        }
    }
}
