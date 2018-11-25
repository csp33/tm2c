import db_connection


class Course:
    def __init__(self, course_name, course_code, start_time, end_time,
                 day, room_code):
        tmp = db_connection.DBConnection()
        self.course_name = course_name
        self.course_code = course_code
        self.start_time = start_time
        self.end_time = end_time
        self.day = day.upper()
        self.room_code = room_code
        self.building_location = tmp.get_location(room_code[0])
        self.building_code = room_code[0]

    def to_s(self):
        return "*Course name*: {0}\n*Course code*: {1}\n*Course day*: {2}\n*Course start time*: {3}\n*Course end time*: {4}\n*Course building*: {5}\n*Course room*: {6} \n".format(self.course_name, self.course_code,
                                                                                               self.day, self.start_time, self.end_time, self.building_code, self.room_code)


def take_me(code):
    tmp = db_connection.DBConnection()
    room_code = code[1:]
    for c in room_code:
        if c.isdigit():
            floor = c
            break
    building = code[0]
    location = tmp.get_location(building)
    return 'Go to building {} ({})'.format(building.upper(), location), 'Go to floor {}'.format(floor), 'Find room {}'.format(room_code)
    # return 'Go to building {0} ({1}).\n Go to floor {2}. Find room {3}.'.format(building.upper(), location, floor, room_code)
