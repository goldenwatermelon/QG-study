package operation;

import entity.Courses;
import entity.StudentCourses;
import entity.Students;
import entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class CoursesOperation {

    //1、查看可选课程方法
    public boolean selectableCourses(User user, Connection conn) {
        ArrayList<Courses> coursesList = new ArrayList<>();
        String sql = "select course_id,course_name,points from courses where course_id not in (select courses_id from student_courses where stu_num = (select num from students where user_id = ?))";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user.id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Courses courses = new Courses();
                    courses.setCourseId(rs.getInt("course_id"));
                    courses.setCourseName(rs.getString("course_name"));
                    courses.setPoints(rs.getFloat("points"));
                    coursesList.add(courses);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (coursesList.size() >= 2) {
            System.out.println("可选课程有:");
            System.out.println(coursesList);
            return true;
        } else {
            System.out.println("选择课程已达上限，已无可选课程");
            return false;
        }
    }


    //2、选择课程方法
    public void selectCourses(User user, Connection conn) throws SQLException {

        Scanner sc = new Scanner(System.in);
        int command = 0;
        while (command != -1) {
            if (selectableCourses(user, conn)) {
                System.out.println("请输入要选择的课程id（如输入1则选择语文,输入-1返回）：");
                while (true) {
                    try {
                        command = sc.nextInt();
                        break;
                    } catch (Exception e) {
                        System.out.println("输入无效");
                    }
                    sc.next();
                }

                if (command == -1){
                    break;
                }
                if (command <= 6 && command >= 1) {
                    try {
                        conn.setAutoCommit(false);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    String sql = "insert into student_courses(stu_num,courses_id) select ?,? where not exists (select 1 from student_courses where stu_num = ? and courses_id = ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        Students studentInform = new Students();
                        StudentCourses studentCourses = new StudentCourses();
                        PublicOperation publicOperation = new PublicOperation();
                        studentInform = publicOperation.inform(user, conn);
                        studentCourses.stu_num = studentInform.num;
                        studentCourses.course_id = command;
                        pstmt.setInt(1, studentCourses.stu_num);
                        pstmt.setInt(2, studentCourses.course_id);
                        pstmt.setInt(3, studentCourses.stu_num);
                        pstmt.setInt(4, studentCourses.course_id);
                        int rows = pstmt.executeUpdate();
                        if (rows > 0) {
                            conn.commit();
                            System.out.println("选课成功！");
                        } else {
                            System.out.println("不能选择重复课程");
                            conn.rollback();
                        }
                    } catch (SQLException e) {
                        System.out.println("数据库错误" + e.getMessage());
                    }
                } else {
                    System.out.println("输入有误捏");
                }
            } else {
                break;
            }
        }
    }

    //3、退课方法
    public void dropCourses(User user,Connection conn) throws SQLException {
        ArrayList<Courses> courses = selectedCourses(user,conn);
        if (courses != null) {
            Scanner sc = new Scanner(System.in);
            int command = 0;
            while (command != -1) {
                System.out.println("请输入要退选的课程id（如输入1则选择语文,输入-1返回）：");
                while (true) {
                    try {
                        command = sc.nextInt();
                        break;
                    } catch (Exception e) {
                        System.out.println("输入无效");
                    }
                    sc.next();
                }
                if (command == -1) {
                    break;
                }
                if (command <= 6 && command >= 1) {
                    try {
                        conn.setAutoCommit(false);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    String sql = "delete from student_courses where stu_num = ? and courses_id = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        Students studentInform = new Students();
                        StudentCourses studentCourses = new StudentCourses();
                        PublicOperation publicOperation = new PublicOperation();
                        studentInform = publicOperation.inform(user, conn);
                        studentCourses.stu_num = studentInform.num;
                        studentCourses.course_id = command;
                        pstmt.setInt(1, studentCourses.stu_num);
                        pstmt.setInt(2, studentCourses.course_id);
                        int rows = pstmt.executeUpdate();
                        if (rows > 0) {
                            conn.commit();
                            System.out.println("退课成功！");
                            selectedCourses(user, conn);
                        } else {
                            System.out.println("未选择此课程，无法退课");
                        }
                    } catch (SQLException e) {
                        conn.rollback();
                        System.out.println("数据库错误" + e.getMessage());
                    }
                } else {
                    System.out.println("输入有误捏");
                }
            }
        }
    }

    //4、查看已选课程方法
    public ArrayList<Courses> selectedCourses(User user, Connection conn) {
        ArrayList<Courses> coursesList = new ArrayList<>();
        String sql = "select course_id,course_name,points from courses where course_id in (select courses_id from student_courses where stu_num = (select num from students where user_id = ?))";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user.id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Courses courses = new Courses();
                    courses.setCourseId(rs.getInt("course_id"));
                    courses.setCourseName(rs.getString("course_name"));
                    courses.setPoints(rs.getFloat("points"));
                    coursesList.add(courses);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (coursesList.size() > 0) {
            System.out.println("已选课程有:");
            System.out.println(coursesList);

        } else {
            System.out.println("无已选课程");
        }
        return coursesList;
    }
}