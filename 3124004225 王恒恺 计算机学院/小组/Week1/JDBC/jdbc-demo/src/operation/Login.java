package operation;

import com.QG.jdbc.DatabaseUtil;
import entity.User;
import role.AdminMenu;
import role.StudentMenu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Login {

    //实现登录
    public void login(){
        Scanner sc = new Scanner(System.in);
        System.out.println("===== 用户登录 =====");
        System.out.println("请输入用户名：\n>");
        String name = sc.next();
        System.out.println("请输入密码：\n>");
        String password = sc.next();

        //判断是否存在账户对象
        try (Connection conn = DatabaseUtil.getConnection()){
            ArrayList<User> User = new ArrayList<>();
            User user = authentication(conn,name,password);
            if (user != null){
                if (user.role == 1){
                    System.out.println("登录成功！你的角色是：学生");
                    //jump to
                    StudentMenu studentMenu = new StudentMenu();
                    studentMenu.studentMenu(user,conn);
                } else if (user.role == 2){
                    System.out.println("登录成功！你的角色是：管理员");
                    //jump to
                    AdminMenu adminMenu = new AdminMenu();
                    adminMenu.adminMenu(user,conn);
                }
            }else {
                System.out.println("用户名或密码错误");
            }
        }catch (SQLException e){
            System.out.println("数据库错误" + e.getMessage());
        }
    }


    //验证用户名和密码
    public User authentication(Connection conn,String name,String password) {
        String sql = "select id,name,password,role from users where name = ? and password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,name);
            pstmt.setString(2,password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    if (storedPassword.equals(password)) {
                        User user = new User();
                        user.setId(rs.getInt("id"));
                        user.setPassword(rs.getString("password"));
                        user.setName(rs.getString("name"));
                        user.setRole(rs.getInt("role"));
                        return user;
                    }
                }
                return null; // 用户不存在或密码错误
            }
        } catch (SQLException e) {
            System.out.println("查询失败: " + e.getMessage());
            return null;
        }
    }
}
