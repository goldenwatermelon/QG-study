package operation;

import entity.Students;
import entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class PublicOperation {
    //查询个人信息方法
    public Students inform(User user, Connection conn) {
        String sql = "select num,tel,user_id from students where user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1,user.getId());
            Students student = new Students();
            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()) {
                    student.setNum(rs.getInt("num"));
                    student.setTel(rs.getString("tel"));
                    student.setUser_id(rs.getInt("user_id"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return student;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //修改手机号方法
    public void updateTel (Students students,Connection conn) {
        Scanner sc = new Scanner(System.in);
        System.out.println("当前手机号为：" + students.getTel());
        System.out.println("请输入修改后的手机号（取消修改输入i）：");
        String newTel = sc.next();
        if(newTel.equals("i")){
            return;
        }
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql = "update students set tel = ? where num = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,newTel);
            pstmt.setInt(2,students.getNum());
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                conn.commit();
                System.out.println("修改成功！");
            } else {
                System.out.println("修改失败，请检查输入");
                conn.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //修改密码方法
    public Boolean updatePassword (User user,Connection conn){
        System.out.println("当前密码为：" + user.getPassword());
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("请输入修改后的密码（取消修改输入i）：");
            String password = sc.next();
            if(password.equals("i")){
                return false;
            }
            System.out.println("请确认修改后的密码：");
            String checkPassword = sc.next();
            if (checkPassword.equals(password)) {
                user.setPassword(password);
                try {
                    conn.setAutoCommit(false);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                String sql = "update users set password = ? where id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)){
                    pstmt.setString(1, user.getPassword());
                    pstmt.setInt(2,user.getId());
                    int rows = pstmt.executeUpdate();
                    if (rows > 0) {
                        conn.commit();
                        System.out.println("修改成功！");
                        System.out.println("修改后的密码为：" + user.getPassword());
                        System.out.println("请重新登录");
                        return true;
                    } else {
                        System.out.println("修改失败，请检查输入");
                        conn.rollback();
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("您输入的2次密码不一致，请重新输入");
            }
        }
    }
}
