import mysql.connector
import parameters
import sys
import course


class DBConnection:
    def __init__(self):
        self.db = mysql.connector.connect(
            host=parameters.DB_URL,
            user=parameters.DB_USER,
            passwd=parameters.DB_PASSWORD,
            database=parameters.DB_NAME
        )
        self.username = None

    def is_valid_user(self, username):
        cursor = self.db.cursor()
        valid = False
        cursor.execute(
            "SELECT * FROM student WHERE username=\"{}\";".format(username))
        cursor.fetchall()
        if cursor.rowcount == 1:
            valid = True
        return valid

    def login(self, username):
        success = self.is_valid_user(username)
        if success:
            self.username = username
        return success

    def logout(self):
        self.username = None

    def get_location(self, building):
        cursor = self.db.cursor()
        cursor.execute(
            "SELECT location FROM building WHERE building_code=\"{}\";".format(building))
        result = cursor.fetchall()[0][0]
        return result

    def create_user(self, username, name):
        success = False
        try:
            cursor = self.db.cursor()
            cursor.execute("INSERT INTO student(username,name) VALUES (\"{}\",\"{}\") ".format(
                username, name))
            self.db.commit()
            success = True
        except Exception:
            print("The user already exists.")
        return success

    def add_course(self, c):
        success = False
        start_time = c.start_time
        end_time = c.end_time
        course_name = c.course_name
        room_code = c.room_code
        building_code = c.building_code
        building_location = c.building_location
        day = c.day

        if self.username != None:
            cursor = self.db.cursor()
            cursor.execute(
                "INSERT INTO course SELECT * FROM (SELECT \"{0}\") AS tmp WHERE NOT EXISTS (SELECT course_name FROM course WHERE course_name=\"{0}\") LIMIT 1;".format(course_name))
            cursor.execute(
                "INSERT INTO room SELECT * FROM (SELECT \"{0}\") AS tmp WHERE NOT EXISTS (SELECT room_code FROM room WHERE room_code=\"{0}\") LIMIT 1;".format(room_code))
            cursor.execute(
                "INSERT INTO is_registered SELECT * FROM (SELECT \"{0}\",\"{1}\") AS tmp WHERE NOT EXISTS (SELECT course_name FROM is_registered WHERE course_name=\"{1}\") LIMIT 1;".format(self.username, course_name))

            cursor.execute("INSERT INTO imparted SELECT * FROM (SELECT \"{0}\",\"{1}\",\"{2}\",\"{3}\",\"{4}\") AS tmp WHERE NOT EXISTS (SELECT course_name FROM imparted WHERE course_name=\"{0}\") LIMIT 1;".format(
                course_name, room_code, day, start_time, end_time))

            cursor.execute(
                "INSERT INTO located SELECT * FROM (SELECT \"{0}\",\"{1}\") AS tmp WHERE NOT EXISTS (SELECT room_code FROM located WHERE room_code=\"{0}\") LIMIT 1;".format(room_code, building_code))

            self.db.commit()
            success = True
        return success

    def get_all_courses(self):
        result = []
        print(self.username)
        if self.username != None:
            cursor = self.db.cursor(dictionary=True)
            cursor.execute("SELECT * FROM is_registered,building,located,student,course,imparted WHERE student.username=\"{}\"AND course.course_name=imparted.course_name AND student.username=is_registered.username AND is_registered.course_name=imparted.course_name AND located.room_code=imparted.room_code AND building.building_code=located.building_code;".format(self.username))
            row = cursor.fetchone()
            while row != None:
                aux = course.Course(course_name=row['course_name'], start_time=row['start_time'],
                                    end_time=row['end_time'], day=row['day'], room_code=row['room_code'])
                result.append(aux)
                row = cursor.fetchone()
        return result

    def remove_course(self, course_name):
        success = False
        if self.username != None:
            cursor = self.db.cursor()
            try:
                cursor.execute("DELETE FROM is_registered WHERE username=\"{}\" AND course_name=\"{}\";".format(
                    self.username, course_name))
                self.db.commit()
                success = True
            except Exception:
                pass
        return success

    def remove_all_courses(self):
        success = False
        if self.username != None:
            cursor = self.db.cursor()
            try:
                cursor.execute(
                    "DELETE FROM is_registered WHERE username=\"{}\"".format(self.username))
                self.db.commit()
                success = True
            except Exception:
                pass
        return success

    def get_next_course(self,time, day):
        day=day.upper()
        result = None
        cursor = self.db.cursor(dictionary=True)
        cursor.execute("SELECT distinct imparted.course_name,building.location,imparted.start_time,imparted.end_time,course.course_name,imparted.day,building.building_code,imparted.room_code FROM course,imparted,located,building,is_registered WHERE course.course_name=is_registered.course_name AND located.building_code=building.building_code AND is_registered.username=\"{0}\" AND located.room_code=imparted.room_code AND imparted.course_name=is_registered.course_name AND imparted.start_time>\"{1}\" AND imparted.day=\"{2}\" ORDER BY start_time;".format(
            self.username, time, day))
        data = cursor.fetchone()
        if data==None:
            result=None
        else:
            result = course.Course(course_name=data['course_name'], start_time=data['start_time'],
                                   end_time=data['end_time'], day=data['day'], room_code=data['room_code'])

        return result
"""
obj = DBConnection()
print(obj.login("Juan", "pass"))
# print(obj.get_location("I"))
#obj.create_user("lolo", "x", "Lolo", "mail")
#print(obj.is_valid_user("lolo", "x"))
co = course.Course("name", "code", "12:00", "13:00",
                   "monday", "IB231", obj.get_location("I"))
obj.add_course(co)
e = obj.get_all_courses()
for x in e:
    print(x.to_s())

obj.remove_course("code")
print("code borrado")
v = obj.get_all_courses()
for x in v:
    print(x.to_s())

obj.get_next_course(day="monday",time="2:00")
"""
