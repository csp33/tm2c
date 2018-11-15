/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tm2c;

/**
 *
 * @author csp98
 */
public class Course {

    private String start_time;
    private String end_time;
    private String course_name;
    private String course_code;
    private String room_code;
    private String building_code;
    private String building_location;
    private String day;

    public Course(String day, String start, String end, String name, String ccode, String rcode, String location) {
        this.day = day;
        this.start_time = start;
        this.end_time = end;
        this.course_name = name;
        this.course_code = ccode;
        this.room_code = rcode;
        this.building_code = "" + rcode.charAt(0);
        this.building_location = location;
    }

    public String toString() {
        return course_name + " " + course_code + " " + day + " " + start_time + " " + end_time + " "
                + " " + room_code + " " + building_code + " " + building_location + "\n";
    }

    String getBuildingLocation() {
        return building_location;
    }

    String getBuildingCode() {
        return building_code;
    }

    String getRoomCode() {
        return room_code;
    }

    String getCourseCode() {
        return course_code;
    }

    String getCourseName() {
        return course_name;
    }

    String getEndTime() {
        return end_time;
    }

    String getStartTime() {
        return start_time;
    }

    String getDay() {
        return day;
    }
}
