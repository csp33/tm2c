import telepot
from parameters import TOKEN
from telepot.loop import MessageLoop
import time
import db_connection
import course
bot = telepot.Bot(TOKEN)

adding_course = False
removing_course = False
asking_for_code = False

course_code = None
course_name = None
course_day = None
course_start_time = None
course_end_time = None
course_room = None

def reset_variables():
    global adding_course
    global removing_course
    global asking_for_code
    global course_day
    global course_code
    global course_name
    global course_room
    global course_start_time
    global course_end_time

    adding_course = False
    removing_course = False
    asking_for_code = False

    course_code = None
    course_name = None
    course_day = None
    course_start_time = None
    course_end_time = None
    course_room = None

db = db_connection.DBConnection()
db.login("Juan", "pass")


def handle(msg):
    global adding_course
    global removing_course
    global asking_for_code
    global course_day
    global course_code
    global course_name
    global course_room
    global course_start_time
    global course_end_time
    global db

    content_type, chat_type, chat_id = telepot.glance(msg)
    if adding_course:
        if course_code == None:
            course_code = msg['text']
            bot.sendMessage(chat_id, 'Tell me the name of the course')
        elif course_name == None:
            course_name = msg['text']
            bot.sendMessage(chat_id, 'Tell me the day of the course')
        elif course_day == None:
            course_day = msg['text']
            bot.sendMessage(chat_id, 'Tell me the start time of the course')
        elif course_start_time == None:
            course_start_time = msg['text']
            bot.sendMessage(chat_id, 'Tell me the end time of the course')
        elif course_end_time == None:
            course_end_time = msg['text']
            bot.sendMessage(chat_id, 'Tell me the room of the course')
        elif course_room == None:
            course_room = msg['text']
            reset_variables()
            c = course.Course(course_name=course_name, day=course_day,
                              course_code=course_code, room_code=course_room,
                              end_time=course_end_time,
                              start_time=course_start_time)
            db.add_course(c)

    elif removing_course:
        course_code = msg['text']
        success=db.remove_course(course_code)
        if success:
            msg="Successfully removed."
        else:
            msg="Wrong code"
        bot.sendMessage(chat_id, msg)
        removing_course = False
    else:
        if 'Add a course' in msg['text']:
            # Add course
            adding_course = True
            bot.sendMessage(chat_id, 'Tell me the code of the course')
        elif 'Remove a course' in msg['text']:
            # Remove course
            removing_course=True
            bot.sendMessage(chat_id, 'Tell me the code of the course to remove')
            print("Not yet")

        elif 'See my courses' in msg['text']:
            # See my courses
            print("Not yet")

        elif 'Take me to class using the code' in msg['text']:
            # Take me to class using the code
            print("Not yet")
        else:
            bot.sendMessage(chat_id, 'Unavailable option')
    if not adding_course and not removing_course:
        bot.sendMessage(
            chat_id, 'Add a course/Remove a course/See my courses/Take me to class using the code')


MessageLoop(bot, handle).run_as_thread()
while 1:
    time.sleep(1)
