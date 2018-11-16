/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tm2c;

import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author csp98
 */
public class DBConnection {

    private static final String DB_URL = Parameters.DB_URL;
    private static final String DB_DRV = Parameters.DB_DRV;
    private static final String DB_USER = Parameters.DB_USER;
    private static final String DB_PASSWORD = Parameters.DB_PASSWORD;
    private Connection connection = null;
    private Statement statement = null;
    private String username = null;

    public DBConnection() {
        try {
            Class.forName(DB_DRV);
            connection = DriverManager.getConnection(
                    DB_URL, DB_USER, DB_PASSWORD);
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex);
        }
    }

    public boolean login(String username, String password) {
        boolean success = isValidUser(username, password);
        if (success) {
            this.username = username;
        }
        return success;
    }

    public void logout() {
        username = null;
    }

    private boolean isValidUser(String username, String password) {
        boolean valid = false;
        ResultSet rs;
        try {
            rs = statement.executeQuery("select * from student where username=\""
                    + username + "\" and password=sha1(md5(\"" + password + "\"));");
            if (rs.next()) {
                valid = true;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return valid;
    }

    public String getLocation(String building) {
        ResultSet query;
        String result = "";
        try {
            query = statement.executeQuery("SELECT location from building where building_code=\"" + building + "\";");
            if (query.next()) {
                result = query.getString(1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return result;
    }

    public boolean createUser(String username, String password, String name, String email) {
        boolean success = false;
        try {
            statement.execute("INSERT INTO student(username,password,name,email) VALUES (\""
                    + username + "\",sha1(md5(\"" + password + "\")),\"" + name + "\",\"" + email + "\");");
            success = true;
        } catch (SQLException e) {
            if (!e.toString().contains("Duplicate")) {
                System.out.println(e);
            }
        }
        this.username = username;
        return success;
    }

    public boolean addCourse(Course c) {
        String start_time = c.getStartTime();
        String end_time = c.getEndTime();
        String course_name = c.getCourseName();
        String course_code = c.getCourseCode();
        String room_code = c.getRoomCode();
        String building_code = c.getBuildingCode();
        String building_location = c.getBuildingLocation();
        String day = c.getDay();
        day = day.toUpperCase();
        boolean success = false;
        if (username != null) {
            try {

                statement.execute("INSERT INTO course"
                        + " SELECT * FROM (SELECT \"" + course_code + "\",\"" + course_name + "\") AS tmp WHERE NOT EXISTS"
                        + "(SELECT course_code FROM course where course_code=\"" + course_code + "\") LIMIT 1;");
                statement.execute("INSERT INTO room"
                        + " SELECT * FROM (SELECT \"" + room_code + "\") AS tmp WHERE NOT EXISTS"
                        + "(SELECT room_code FROM room where room_code=\"" + room_code + "\") LIMIT 1;");

                statement.execute("INSERT INTO is_registered"
                        + " SELECT * FROM (SELECT \"" + username + "\",\"" + course_code + "\") AS tmp WHERE NOT EXISTS"
                        + "(SELECT course_code FROM is_registered where course_code=\"" + course_code + "\") LIMIT 1;");

                statement.execute("INSERT INTO imparted SELECT * FROM(SELECT \"" + course_code
                        + "\",\"" + room_code + "\",\"" + day + "\",\"" + start_time + "\",\"" + end_time
                        + "\") AS tmp WHERE NOT EXISTS (SELECT course_code FROM imparted WHERE course_code=\""
                        + course_code + "\") LIMIT 1;");

                statement.execute("INSERT INTO located" + " SELECT * FROM (SELECT \"" + room_code + "\",\"" + room_code.charAt(0) + "\") AS tmp WHERE NOT EXISTS"
                        + "(SELECT room_code FROM located where room_code=\"" + course_code + "\") LIMIT 1;");

                success = true;
            } catch (SQLException ex) {
                if (!ex.toString().contains("Duplicate")) {
                    System.out.println(ex);
                }
            }
        } else {
            System.err.println("You must log in first.");
        }
        return success;
    }

    public boolean addCourse(String course_code, String course_name, String room,
            String day, String start_hour, String end_hour) {
        day = day.toUpperCase();
        boolean success = false;
        if (username != null) {
            try {

                statement.execute("INSERT INTO course"
                        + " SELECT * FROM (SELECT \"" + course_code + "\",\"" + course_name + "\") AS tmp WHERE NOT EXISTS"
                        + "(SELECT course_code FROM course where course_code=\"" + course_code + "\") LIMIT 1;");
                statement.execute("INSERT INTO room"
                        + " SELECT * FROM (SELECT \"" + room + "\") AS tmp WHERE NOT EXISTS"
                        + "(SELECT room_code FROM room where room_code=\"" + room + "\") LIMIT 1;");

                statement.execute("INSERT INTO is_registered"
                        + " SELECT * FROM (SELECT \"" + username + "\",\"" + course_code + "\") AS tmp WHERE NOT EXISTS"
                        + "(SELECT course_code FROM is_registered where course_code=\"" + course_code + "\") LIMIT 1;");

                statement.execute("INSERT INTO imparted SELECT * FROM(SELECT \"" + course_code
                        + "\",\"" + room + "\",\"" + day + "\",\"" + start_hour + "\",\"" + end_hour
                        + "\") AS tmp WHERE NOT EXISTS (SELECT course_code FROM imparted WHERE course_code=\""
                        + course_code + "\") LIMIT 1;");

                statement.execute("INSERT INTO located" + " SELECT * FROM (SELECT \"" + room + "\",\"" + room.charAt(0) + "\") AS tmp WHERE NOT EXISTS"
                        + "(SELECT room_code FROM located where room_code=\"" + course_code + "\") LIMIT 1;");

                success = true;
            } catch (SQLException ex) {
                if (!ex.toString().contains("Duplicate")) {
                    System.out.println(ex);
                }
            }
        } else {
            System.err.println("You must log in first.");
        }
        return success;
    }

    public ArrayList<Course> getAllCourses() {
        ResultSet query = null;
        ArrayList<Course> result = new ArrayList();
        Course aux;
        if (username != null) {
            try {
                query = statement.executeQuery("SELECT imparted.course_code,building.location,"
                        + "imparted.start_time,imparted.end_time,course.name,imparted.day,building.building_code,imparted.room_code FROM is_registered,"
                        + "building,located,student,course,imparted WHERE student.username=\"" + username + "\"\n"
                        + "AND course.course_code=imparted.course_code AND student.username=is_registered.username AND is_registered.course_code=imparted.course_code \n"
                        + "AND located.room_code=imparted.room_code AND building.building_code=located.building_code;");
                System.out.println("SELECT imparted.course_code,building.location,"
                        + "imparted.start_time,imparted.end_time,course.name,imparted.day,building.building_code,imparted.room_code FROM is_registered,"
                        + "building,located,student,course,imparted WHERE student.username=\"" + username + "\"\n"
                        + "AND course.course_code=imparted.course_code AND student.username=is_registered.username AND is_registered.course_code=imparted.course_code \n"
                        + "AND located.room_code=imparted.room_code AND building.building_code=located.building_code;");
                String start_time;
                String end_time;
                String course_name;
                String course_code;
                String room_code;
                String building_code;
                String building_location;
                String day;
                while (query.next()) {
                    start_time = query.getString(3);
                    end_time = query.getString(4);
                    course_name = query.getString(5);
                    course_code = query.getString(1);
                    room_code = query.getString(8);
                    building_code = query.getString(7);
                    building_location = query.getString(2);
                    day = query.getString(6);

                    aux = new Course(day, start_time, end_time, course_name, course_code, room_code, building_location);
                    result.add(aux);
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        } else {
            System.err.println("You must log in first.");
        }
        return result;
    }

    public boolean removeCourse(String course_code) {
        boolean success = false;
        if (username != null) {
            try {
                statement.execute("DELETE FROM is_registered WHERE course_code=\"" + course_code + "\" AND username=\"" + username + "\";");
                success = true;
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        } else {
            System.err.println("You must log in first.");
        }
        return success;
    }

    public Course getCourseAt(String time, String day) {
        day = day.toUpperCase();
        ResultSet query = null;
        Course result = null;
        String start_time;
        String end_time;
        String course_name;
        String course_code;
        String room_code;
        String building_code;
        String building_location;
        if (username != null) {
            try {
                query = statement.executeQuery("SELECT distinct imparted.course_code,building.location,"
                        + "imparted.start_time,imparted.end_time,course.name,imparted.day,building.building_code,imparted.room_code"
                        + " FROM course,imparted,located,building,is_registered WHERE "
                        + "located.building_code=building.building_code AND"
                        + " is_registered.username=\"" + username + "\" AND located.room_code=imparted.room_code AND "
                        + "imparted.course_code=course.course_code AND imparted.course_code=is_registered.course_code AND imparted.start_time<=\"" + time + "\" "
                        + "AND imparted.end_time>\"" + time + "\" "
                        + "AND imparted.day=\"" + day + "\" ORDER BY start_time;"
                );
                System.out.println("SELECT distinct imparted.course_code,building.location,"
                        + "imparted.start_time,imparted.end_time,course.name,imparted.day,building.building_code,imparted.room_code"
                        + " FROM course,imparted,located,building,is_registered WHERE "
                        + "located.building_code=building.building_code AND"
                        + " is_registered.username=\"" + username + "\" AND located.room_code=imparted.room_code AND "
                        + "imparted.course_code=course.course_code AND imparted.course_code=is_registered.course_code AND imparted.start_time<=\"" + time + "\" "
                        + "AND imparted.end_time>\"" + time + "\" "
                        + "AND imparted.day=\"" + day + "\";");
                while (query.next()) {
                    start_time = query.getString(3);
                    end_time = query.getString(4);
                    course_name = query.getString(5);
                    course_code = query.getString(1);
                    room_code = query.getString(8);
                    building_code = query.getString(7);
                    building_location = query.getString(2);
                    result = new Course(day, start_time, end_time, course_name, course_code, room_code, building_location);
                }
            } catch (SQLException ex) {

                System.out.println(ex);
            }
        } else {
            System.err.println("You must log in first.");
        }
        return result;
    }

    public Course getNextCourse(String time, String day) {
        day = day.toUpperCase();
        ResultSet query = null;
        Course result = null;
        String start_time;
        String end_time;
        String course_name;
        String course_code;
        String room_code;
        String building_code;
        String building_location;
        if (username != null) {
            try {
                query = statement.executeQuery("SELECT distinct imparted.course_code,building.location,"
                        + "imparted.start_time,imparted.end_time,course.name,imparted.day,building.building_code,imparted.room_code"
                        + " FROM course,imparted,located,building,is_registered WHERE "
                        + "course.course_code=is_registered.course_code AND located.building_code=building.building_code AND"
                        + " is_registered.username=\"" + username + "\" AND located.room_code=imparted.room_code AND "
                        + "imparted.course_code=is_registered.course_code AND imparted.start_time>\"" + time + "\" "
                        + "AND imparted.day=\"" + day + "\" ORDER BY start_time;"
                );

                while (query.next()) {
                    start_time = query.getString(3);
                    end_time = query.getString(4);
                    course_name = query.getString(5);
                    course_code = query.getString(1);
                    room_code = query.getString(8);
                    building_code = query.getString(7);
                    building_location = query.getString(2);
                    result = new Course(day, start_time, end_time, course_name, course_code, room_code, building_location);
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        } else {
            System.err.println("You must log in first.");
        }
        return result;
    }

    public boolean removeAllCourses() {
        boolean success = false;
        if (username != null) {
            try {
                statement.execute("DELETE FROM is_registered WHERE username=\"" + username + "\";");
                success = true;
            } catch (SQLException ex) {
            }
        } else {
            System.err.println("You must log in first.");
        }
        return success;
    }
}
