/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tm2c;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

/**
 *
 * @author csp98
 */
public class Dashboard {

    //Get the instructions to arrive to the class by decyphering the room code.
    public static String getInstructionsToArrive(Course c) {
        String result = "", code, floor = "", room;
        boolean end = false;

        result += "\t\t" + c.getCourseName() + "\n";
        result += "1. Go to building " + c.getBuildingCode() + " (open the link): ";

        result += c.getBuildingLocation() + "\n";

        code = c.getRoomCode();

        for (int i = 0; i < code.length() && !end; i++) {
            if (Character.isDigit(code.charAt(i))) {
                end = true;
                floor += code.charAt(i);
            }
        }

        result += "2. Go to this floor: " + floor + "\n";

        room = code.substring(1);

        result += "3. Go to this classroom: " + room + "\n";

        return result;
    }

    public static String getInstructionsToArrive(String room_code) {
        String result = "", floor = "", room;
        DBConnection aux = new DBConnection();
        boolean end = false;

        String building_code = "" + room_code.charAt(0);
        String location = aux.getLocation(building_code);

        result += "1. Go to building " + building_code + " (open the link): ";

        result += location + "\n";

        for (int i = 0; i < room_code.length() && !end; i++) {
            if (Character.isDigit(room_code.charAt(i))) {
                end = true;
                floor += room_code.charAt(i);
            }
        }

        result += "2. Go to this floor: " + floor + "\n";

        room = room_code.substring(1);

        result += "3. Go to this classroom: " + room + "\n";

        return result;
    }

    public static void sleep(int sec) {
        int ms = sec * 100;
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
    }

    public static String getDay(int d) {
        String result = "";
        switch (d) {
            case Calendar.SUNDAY:
                result = "Sunday";
                break;

            case Calendar.MONDAY:
                result = "Monday";
                break;

            case Calendar.THURSDAY:
                result = "Thursday";
                break;

            case Calendar.WEDNESDAY:
                result = "Wednesday";
                break;

            case Calendar.TUESDAY:
                result = "Tuesday";
                break;

            case Calendar.FRIDAY:
                result = "Friday";
                break;

            case Calendar.SATURDAY:
                result = "Saturday";
                break;

        }

        return result;
    }

    public static void showLoginMenu() {
        System.out.println("1. Register." + "\n" + "2. Login." + "\n" + "3. Take me to class using the code.\n" + "4. Exit.");
    }

    public static void showOptionsMenu() {
        System.out.println("1. Show my courses." + "\n" + "2. Add a course." + "\n" + "3. Erase a course." + "\n" + "4. Erase all courses." + "\n"
                + "5. Take me to the next course." + "\n" + "6. Take me to the next class at an specified time" + "\n"
                + "7. Take me to the class that is being imparted at an specified time" + "\n" + "8. Exit.");
    }

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        DBConnection c = new DBConnection();
        Calendar calendar = GregorianCalendar.getInstance();

        Course aux;
        String username, password, name, email, course_delete, cour_code, cour_name, cour_class, cour_day, cour_start, cour_end, hour, day;
        boolean success;
        char login_input;
        showLoginMenu();
        System.out.println("Enter an option:");
        login_input = keyboard.next().charAt(0);
        
        
        while (login_input != '4') {
            switch (login_input) {
                case '1':
                    System.out.println("You are going to proceed with the registration.");

                    System.out.println("Username: ");
                    username = keyboard.next();

                    System.out.println("Password: ");

                    password = keyboard.next();

                    System.out.println("Name: ");
                    name = keyboard.next();

                    System.out.println("Email: ");
                    email = keyboard.next();

                    success = c.createUser(username, password, name, email);

                    if (success) {
                        System.out.println("User registered with success!");
                    } else {
                        System.out.println("The name is already in use, the registration could not be possible.");
                    }

                    break;
                case '2':
                    System.out.println("Username: ");
                    username = keyboard.next();

                    System.out.println("Password: ");
                    password = keyboard.next();

                    boolean succesful_login = c.login(username, password);

                    if (!succesful_login) {
                        System.out.println("Wrong user/password");
                    } else {
                        char option_input;
                        showOptionsMenu();
                        System.out.println("Enter an option:");
                        option_input = keyboard.next().charAt(0);
                        while (option_input != '8') {

                            switch (option_input) {
                                case '1':
                                    System.out.println("These are the subjects in which you are registered: ");

                                    c.getAllCourses().forEach((course) -> {
                                        System.out.println(course);
                                    });

                                    break;
                                case '2':
                                    System.out.println("Introduce the code of the course: ");
                                    cour_code = keyboard.next();

                                    System.out.println("Introduce the name of the course: ");
                                    cour_name = keyboard.next();

                                    System.out.println("Introduce the room of the course: ");
                                    cour_class = keyboard.next();

                                    System.out.println("Introduce the day in which it takes place: ");
                                    cour_day = keyboard.next();

                                    System.out.println("Introduce the hour in which it starts: ");
                                    cour_start = keyboard.next();

                                    System.out.println("Introduce the hour in which it ends: ");
                                    cour_end = keyboard.next();

                                    success = c.addCourse(cour_code, cour_name, cour_class, cour_day, cour_start, cour_end);

                                    if (success) {
                                        System.out.println("The course " + cour_code + " has been added.");
                                    } else {
                                        System.out.println("You are already registered in this course.");
                                    }

                                    break;
                                case '3':
                                    System.out.println("Introduce the course you want to delete: ");
                                    course_delete = keyboard.next();

                                    success = c.removeCourse(course_delete);

                                    if (success) {
                                        System.out.println("The course " + course_delete + " has been deleted.");
                                    } else {
                                        System.out.println("You are not registered in this course.");
                                    }

                                    break;
                                case '4':
                                    c.removeAllCourses();

                                    System.out.println("All the courses has been deleted.");
                                    break;
                                case '5':
                                    aux = c.getNextCourse(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE), getDay(calendar.get(Calendar.DAY_OF_WEEK)));

                                    System.out.println(getInstructionsToArrive(aux));

                                    sleep(4);
                                    break;
                                case '6':
                                    System.out.println("Introduce the hour: ");
                                    hour = keyboard.next();

                                    System.out.println("Introduce the day: ");
                                    day = keyboard.next();

                                    aux = c.getNextCourse(hour, day);

                                    System.out.println(getInstructionsToArrive(aux));
                                    sleep(4);
                                    break;
                                case '7':
                                    System.out.println("Introduce the hour: ");
                                    hour = keyboard.next();

                                    System.out.println("Introduce the day: ");
                                    day = keyboard.next();

                                    aux = c.getCourseAt(hour, day);

                                    System.out.println(getInstructionsToArrive(aux));
                                    sleep(4);
                                    break;
                                case '8':
                                    break;
                                default:
                                    System.out.println("Unavailable option.");
                                    break;
                            }
                            showOptionsMenu();
                            System.out.println("Enter an option:");
                            option_input = keyboard.next().charAt(0);
                        }
                        break;
                    }
                case '3':
                    System.out.println("Introduce the code of the classroom: ");
                    cour_code = keyboard.next();

                    System.out.println(getInstructionsToArrive(cour_code));
                    sleep(4);
                    break;
                default:
                    System.out.println("Unavailable option.");
                    break;
            }
            showLoginMenu();
            System.out.println("Enter an option:");
            login_input = keyboard.next().charAt(0);
        }
        c.logout();
    }

}
