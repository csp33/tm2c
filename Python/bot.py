import telepot
from parameters import TOKEN
from telepot.loop import MessageLoop
import time
import db_connection
from telepot.namedtuple import ReplyKeyboardMarkup, KeyboardButton, ReplyKeyboardRemove
from course import *
import datetime

bot = telepot.Bot(TOKEN)

adding_course = False
removing_course = False
taking_to_class = False

course_code = None
course_name = None
course_day = None
course_start_time = None
course_end_time = None
course_room = None

menu = [
    [KeyboardButton(text="Add a course"), KeyboardButton(text="Remove a course"),
     KeyboardButton(text="See my courses"), KeyboardButton(
         text="Take me to class using the code"),
     KeyboardButton(text="Take me to the next class")]
]
reply_markup = ReplyKeyboardMarkup(keyboard=menu)


def reset_variables():
    global adding_course
    global removing_course
    global course_day
    global course_code
    global course_name
    global course_room
    global course_start_time
    global course_end_time
    global taking_to_class

    adding_course = False
    removing_course = False
    taking_to_class = False

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
    global course_day
    global course_code
    global course_name
    global course_room
    global course_start_time
    global course_end_time
    global taking_to_class
    global db

    content_type, chat_type, chat_id = telepot.glance(msg)
    if adding_course:
        if course_code == None:
            course_code = msg['text']
            bot.sendMessage(
                chat_id, 'Tell me the name of the course', reply_markup=ReplyKeyboardRemove())
        elif course_name == None:
            course_name = msg['text']
            bot.sendMessage(
                chat_id, 'Tell me the day of the course', reply_markup=ReplyKeyboardRemove())
        elif course_day == None:
            course_day = msg['text']
            bot.sendMessage(
                chat_id, 'Tell me the start time of the course', reply_markup=ReplyKeyboardRemove())
        elif course_start_time == None:
            course_start_time = msg['text']
            bot.sendMessage(
                chat_id, 'Tell me the end time of the course', reply_markup=ReplyKeyboardRemove())
        elif course_end_time == None:
            course_end_time = msg['text']
            bot.sendMessage(
                chat_id, 'Tell me the room of the course', reply_markup=ReplyKeyboardRemove())
        elif course_room == None:
            course_room = msg['text']
            c = Course(course_name=course_name, day=course_day,
                       course_code=course_code, room_code=course_room,
                       end_time=course_end_time,
                       start_time=course_start_time)
            reset_variables()
            db.add_course(c)

    elif removing_course:
        course_code = msg['text']
        success = db.remove_course(course_code)
        if success:
            msg = "Successfully removed."
        else:
            msg = "Wrong code"
        bot.sendMessage(chat_id, msg, reply_markup=ReplyKeyboardRemove())
        removing_course = False
    elif taking_to_class:
        code = msg['text']
        first, second, third = take_me(code)
        bot.sendMessage(chat_id, first)
        bot.sendMessage(chat_id, second)
        bot.sendMessage(chat_id, third)
        taking_to_class = False
    else:
        if '/start' in msg['text']:
            db.create_user(username=chat_id, password='1234',
                           name=msg['from']['first_name'], email='mail')
            db.login(username=chat_id, password="1234")
            bot.sendMessage(chat_id, "Welcome {}!".format(
                msg['from']['first_name']), reply_markup=ReplyKeyboardRemove())
        elif 'Add a course' in msg['text']:
            # Add course
            adding_course = True
            bot.sendMessage(
                chat_id, 'Tell me the code of the course', reply_markup=ReplyKeyboardRemove())
        elif 'Remove a course' in msg['text']:
            # Remove course
            removing_course = True
            bot.sendMessage(
                chat_id, 'Tell me the code of the course to remove', reply_markup=ReplyKeyboardRemove())

        elif 'See my courses' in msg['text']:
            # See my courses
            courses = db.get_all_courses()
            for c in courses:
                bot.sendMessage(chat_id, c.to_s(), parse_mode='Markdown')

        elif 'Take me to class using the code' in msg['text']:
            # Take me to class using the code
            bot.sendMessage(
                chat_id, 'Tell me the code of the course you want to go', reply_markup=ReplyKeyboardRemove())
            taking_to_class = True
        elif 'Take me to the next class' in msg['text']:
            now = datetime.datetime.now()
            day = get_day(datetime.datetime.today().weekday())
            course = db.get_next_course(day=day, time=now.strftime("%H:%M"))
            if course == None:
                bot.sendMessage(chat_id, "You don't have any courses today.")
            else:
                bot.sendMessage(chat_id, "Your next course is *{}*".format(course.course_name), parse_mode='Markdown')
                first, second, third = take_me(course.room_code)
                bot.sendMessage(chat_id, first)
                bot.sendMessage(chat_id, second)
                bot.sendMessage(chat_id, third)
        else:
            bot.sendMessage(chat_id, 'Unavailable option')
    if not taking_to_class and not adding_course and not removing_course:
        # bot.sendMessage(
        #    chat_id, 'Add a course/Remove a course/See my courses/Take me to class using the code')
        bot.sendMessage(chat_id, 'Choose an option:',
                        reply_markup=reply_markup)


MessageLoop(bot, handle).run_as_thread()
while 1:
    time.sleep(1)
