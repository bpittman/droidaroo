# -*- coding: utf-8 -*-
import demjson
import sqlite3

rawVenues = demjson.decode(open("venues.txt").read())
events = demjson.decode(open("events.txt").read())

venues = {}
for venueID in rawVenues:
   venues[rawVenues[venueID]["id"]] = rawVenues[venueID]["line1"]

conn = sqlite3.connect('../droidaroo/res/raw/eventdb')
c = conn.cursor()

# Create tables
c.execute('drop table if exists android_metadata')
c.execute('create table android_metadata ("locale" TEXT DEFAULT "en_US")')
c.execute('insert into android_metadata values ("en_US")')

c.execute('drop table if exists events')
c.execute('''create table events
(_id int, venueId text,
line1 text, line2 text,
start text, duration text)''')

for eventID in events:
   t = (eventID,venues[events[eventID]["venueId"]],
        events[eventID]["line1"],events[eventID]["line2"],
        events[eventID]["start"],events[eventID]["duration"])
   c.execute('insert into events values (?,?,?,?,?,?)',t)

c.execute('drop table if exists venues')
c.execute('create table venues (_id int, venueId text)')

for venueID in venues:
   t = (venueID, venues[venueID])
   c.execute('insert into venues values (?,?)',t)

c.execute('select * from events')
for row in c:
   print row

c.execute('select * from venues')
for row in c:
   print row

conn.commit()
conn.close()
