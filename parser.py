# -*- coding: utf-8 -*-
import demjson
import sqlite3

venues = demjson.decode(open("venues.txt").read())
events = demjson.decode(open("events.txt").read())

conn = sqlite3.connect('./eventDB')
c = conn.cursor()

# Create table
c.execute('drop table if exists events')
c.execute('''create table events
(id int, venueId int,
line1 text, line2 text,
start text, duration text)''')

for eventID in events:
   t = (eventID,events[eventID]["venueId"],
        events[eventID]["line1"],events[eventID]["line2"],
        events[eventID]["start"],events[eventID]["duration"])
   c.execute('insert into events values (?,?,?,?,?,?)',t)

c.execute('select * from events')
for row in c:
   print row

conn.commit()
conn.close()