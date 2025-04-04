package operation;

import entity.Courses;
import entity.Students;
import entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class AdminOperation {
    //1、查找所有学生
    PublicOperation publicOperation = new PublicOperation();

    public ArrayList<User> selectStudent(Connection conn) {
        ArrayList<User> usersList = new ArrayList<>();
        ArrayList<Students> studentsList = new ArrayList<>();

        String sql = "select id,name,password from users where role = 1";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                    user.setId(rs.getInt("id"));
                    user.setRole(1);
                    usersList.add(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!usersList.isEmpty()) {
            int i;
            System.out.println("学生列表：");
            for (i = 0;i < usersList.size();i++) {
                PublicOperation publicOperation = new PublicOperation();
                Students students = publicOperation.inform(usersList.get(i),conn);
                studentsList.add(students);
                System.out.println(usersList.get(i).getId() + ", " + usersList.get(i).getName() + ", " + studentsList.get(i).getNum());
            }
        } else {
            System.out.println("列表中无学生");
        }
        return usersList;
    }

    //2、修改学生手机号
    public void updateStuTel (Connection conn) {
        ArrayList<User> studentsList = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        studentsList = selectStudent(conn);

        System.out.println("请输入需要查找的学生id");
        int id;
        while (true) {
            try {
                id = sc.nextInt();
                break;
            } catch (Exception e) {
                System.out.println("输入无效");
            }
            sc.next();
        }
        int i;
        for (i = 0;i < studentsList.size();i++) {
            if (studentsList.get(i).getId() == id) {
                break;
            }
        }

        Students students = new Students();
        students = publicOperation.inform(studentsList.get(i),conn);

        System.out.println(studentsList.get(i) + ", " + students);
        System.out.println("如需修改学生手机号，输入1，否则输入0：");
        int command = -1;
        while (command != 0) {
            while (true) {
                try {
                    command = sc.nextInt();
                    break;
                } catch (Exception e) {
                    System.out.println("输入无效");
                }
                sc.next();
            }
            if (command == 1) {
                publicOperation.updateTel(students,conn);
                break;
            }else if (command == 0){
                break;
            }else {
                System.out.println("请输入0或1");
            }
        }
    }

    //3、查询所有课程方法
    public ArrayList<Courses> allCourses(Connection conn) {
        ArrayList<Courses> coursesList = new ArrayList<>();
        String sql = "select course_id,course_name,points from courses";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
        System.out.println(coursesList);
        return coursesList;
    }


    //4、修改课程学分
    public void updatePoints (Connection conn) {
        ArrayList<Courses> courses = allCourses(conn);
        Scanner sc = new Scanner(System.in);
        int id = 0;
        while (id != -1) {
            System.out.println("请输入要选择的课程id（如选择语文则输入1,返回输入-1）：");
            while (true) {
                try {
                    id = sc.nextInt();
                    break;
                } catch (Exception e) {
                    System.out.println("输入无效");
                }
                sc.next();
            }
            if (id == -1){
                break;
            }
            if (id >= 1 && id <= 6) {
                float points;
                System.out.println("请输入修改后的学分：");
                while (true) {
                    try {
                        points = sc.nextFloat();
                        break;
                    } catch (Exception e) {
                        System.out.println("输入无效");
                    }
                    sc.next();
                }
                String sql = "update courses set points = ? where course_id = ?";
                try {
                    conn.setAutoCommit(false);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try (PreparedStatement pstmt = conn.prepareStatement(sql)){
                    pstmt.setFloat(1,points);
                    pstmt.setInt(2,id);
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
            }else {
                System.out.println("输入无效");
                break;
            }
        }
    }

    //5、查询某课程的学生名单
    public void stuListOfCourse (Connection conn) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Courses> Courses = allCourses(conn);
        int id = 0;
        while (id != -1) {
            ArrayList<Students> stuList1 = new ArrayList<>();
            ArrayList<User> stuList2 = new ArrayList<>();
            System.out.println("请输入查询的课程id（如选择语文则输入1,返回输入-1）：");
            while (true) {
                try {
                    id = sc.nextInt();
                    break;
                } catch (Exception e) {
                    System.out.println("输入无效");
                }
                sc.next();
            }
            if (id == -1) {
                break;
            }
            if (id >= 1 && id <= 6 ) {
                String sql = "select name,num from users,students where " +
                        "id = (select user_id from students where num = (select stu_num from student_courses where courses_id = ?)) " +
                        "and user_id = (select user_id from students where num = (select stu_num from student_courses where courses_id = ?))";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)){
                    pstmt.setInt(1,id);
                    pstmt.setInt(2,id);
                    try (ResultSet rs = pstmt.executeQuery()){
                        while (rs.next()) {
                            Students students = new Students();
                            User user = new User();
                            students.setNum(rs.getInt("num"));
                            user.setName(rs.getString("name"));
                            stuList1.add(students);
                            stuList2.add(user);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int i;
                for (i = 0;i < Courses.size();i++) {
                    if (Courses.get(i).getCourseId() == id) {
                        break;
                    }
                }
                System.out.println("选择" + Courses.get(i).getCourseName() + "的学生有：");
                int j;
                for (j = 0;j < stuList1.size();j++) {
                    System.out.println(stuList1.get(j).getNum() + ", " + stuList2.get(j).getName());
                }
            }else {
                System.out.println("输入无效");
            }
        }
    }

    //6、查询某学生的选课情况
    public void coursesOfStudent (Connection conn) {
        ArrayList<User> allStudents = selectStudent(conn);//调用本类方法1
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入学生id，返回输入-1）：");
        int id = 0;
        while (true) {
            try {
                id = sc.nextInt();
                break;
            } catch (Exception e) {
                System.out.println("输入无效");
            }
            sc.next();
        }
        while (id != -1) {
            if (id > 0) {
                int i;
                for (i = 0;i < allStudents.size();i++) {
                    if (allStudents.get(i).id == id){
                        break;
                    }
                }
                ArrayList<Courses> stuCourses = new ArrayList<>();
                CoursesOperation coursesOperation = new CoursesOperation();
                stuCourses = coursesOperation.selectedCourses(allStudents.get(i),conn);
                System.out.println(allStudents.get(i).getName() + "的已选课程：");
                int j;
                for (j = 0;j < stuCourses.size();j++) {
                    System.out.println(stuCourses.get(j));
                }
            }
        }
    }
}
