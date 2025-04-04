package role;

import entity.Courses;
import entity.StudentCourses;
import entity.Students;
import entity.User;
import operation.AdminOperation;
import operation.CoursesOperation;
import operation.PublicOperation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class AdminMenu {
    public void adminMenu (User user, Connection conn) throws SQLException {
        Scanner sc = new Scanner(System.in);
        int command = 0;

        while (command != 9) {
            System.out.println("===== 管理员菜单 =====");
            System.out.println("1. 查询所有学生");
            System.out.println("2. 查看某学生信息或修改其手机号");
            System.out.println("3. 查询所有课程");
            System.out.println("4. 修改课程学分");
            System.out.println("5. 查询某课程的学生名单");
            System.out.println("6. 查询某学生的选课情况");
            System.out.println("7. 修改密码");
            System.out.println("8. 查看个人信息");
            System.out.println("9. 退出");
            System.out.println("请选择操作（输入 1-9）：");

            while (true) {
                try {
                    command = sc.nextInt();
                    break;
                } catch (Exception e) {
                    System.out.println("输入无效");
                }
                sc.next();
            }
            PublicOperation publicOperation = new PublicOperation();
            AdminOperation adminOperation = new AdminOperation();
            Students students = new Students();
            StudentCourses studentCourses = new StudentCourses();
            Courses courses = new Courses();
            CoursesOperation coursesOperation = new CoursesOperation();

            switch (command) {
                case 1:
                    //查询所有学生
                    ArrayList<User> allStudents = new ArrayList<>();
                    allStudents = adminOperation.selectStudent(conn);
                    break;
                case 2:
                    //查看某学生信息或修改其手机号
                    adminOperation.updateStuTel(conn);
                    break;
                case 3:
                    //查询所有课程
                    adminOperation.allCourses(conn);
                    break;
                case 4:
                    //修改课程学分
                    adminOperation.updatePoints(conn);
                    break;
                case 5:
                    //查询某课程的学生名单
                    adminOperation.stuListOfCourse(conn);
                    break;
                case 6:
                    //查询某学生的选课情况
                    adminOperation.coursesOfStudent(conn);
                    break;
                case 7:
                    //修改密码
                    publicOperation.updatePassword(user,conn);
                    command = 9;
                    break;
                case 8:
                    //查看个人信息
                    System.out.println(user);
                    break;
                case 9:
                    //退出
                    conn.close();
                    break;
                default:
                    System.out.println("只能输入1-9噢");
            }
        }
    }
}
