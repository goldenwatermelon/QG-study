package entity;

public class Courses {
    public Integer courseId;
    public String courseName;
    public Float points;
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Float getPoints() {
        return points;
    }

    public void setPoints(Float points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", points=" + points + '\n'
                ;
    }
}
