package role;

import com.QG.jdbc.DatabaseUtil;
import entity.Courses;
import entity.StudentCourses;
import entity.Students;
import entity.User;
import operation.CoursesOperation;
import operation.PublicOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class StudentMenu {
    public void studentMenu(User user,Connection conn) throws SQLException {
        Scanner sc = new Scanner(System.in);
        int command = 0;
        while (command != 8) {
            System.out.println("===== 学生菜单 =====");
            System.out.println("1. 查看可选课程");
            System.out.println("2. 选择课程");
            System.out.println("3. 退选课程");
            System.out.println("4. 查看已选课程");
            System.out.println("5. 修改手机号");
            System.out.println("6. 查看个人信息");
            System.out.println("7. 修改密码");
            System.out.println("8. 退出");
            System.out.println("请选择操作（输入 1-8）：");

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
            Students students = new Students();
            StudentCourses studentCourses = new StudentCourses();
            Courses courses = new Courses();
            CoursesOperation coursesOperation = new CoursesOperation();
            switch (command) {
                case 1:
                    //查询courses表和student_courses表
                    //查看可选课程
                    coursesOperation.selectableCourses(user,conn);
                    break;
                case 2:
                    //写入student_courses表
                    coursesOperation.selectCourses(user,conn);
                    break;
                case 3:
                    //删除student_courses表中的对应内容
                    coursesOperation.dropCourses(user,conn);
                    break;
                case 4:
                    //查询student_courses表
                    ArrayList<Courses> selectedCourses = coursesOperation.selectedCourses(user,conn);
                    break;
                case 5:
                    //查询students表并修改手机号tel
                    students = publicOperation.inform(user, conn);
                    publicOperation.updateTel(students,conn);
                    break;
                case 6:
                    //查询users表和students表的对应内容
                    students = publicOperation.inform(user, conn);
                    System.out.println(user);
                    System.out.println(students);
                    break;
                case 7:
                    //修改users表中的password
                    if (publicOperation.updatePassword(user, conn)) {
                        command = 8;
                    }
                    break;
                case 8:
                    //退出
                    conn.close();
                    break;
                default:
                    System.out.println("只能输入1-8噢");
            }
        }
    }
}
