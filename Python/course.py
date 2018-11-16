

class Course:
    def __init__(self, course_name, course_code, start_time, end_time,
                 day, room_code, location):
        self.course_name = course_name
        self.course_code = course_code
        self.start_time = start_time
        self.end_time = end_time
        self.day = day.upper()
        self.room_code = room_code
        self.building_location = location
        self.building_code = room_code[0]

    def to_s(self):
        return "{0} {1} {2} {3} {4} {5} {6} \n".format(self.course_name,self.course_code,
                                            self.day,self.start_time,self.end_time
                                            ,self.building_code,self.room_code)
