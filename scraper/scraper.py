# -*- coding: utf-8 -*-
import demjson
import sqlite3
import datetime,time
import re

#we want late-night shows to be considered part of the previous day.
#for instance a show that begins at 2am on Friday should actually be
#listed as a Thursday show. So we count anything prior to 9am as
#belonging to the prior day.
thursStart = time.strptime("2011-06-09 09:00:00", "%Y-%m-%d %H:%M:%S")
friStart = time.strptime("2011-06-10 09:00:00", "%Y-%m-%d %H:%M:%S")
satStart = time.strptime("2011-06-11 09:00:00", "%Y-%m-%d %H:%M:%S")
sunStart = time.strptime("2011-06-12 09:00:00", "%Y-%m-%d %H:%M:%S")

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
fulltime text, stringtime text,
minor bool, day text)''')

for eventID in events:
   if events[eventID]["venueId"] == "": continue
   if events[eventID]["line1"] == "": continue
   if events[eventID]["start"] == "": continue
   if events[eventID]["minor"] == "": continue
   if events[eventID]["duration"] == "": continue

   fullTime = time.strptime(events[eventID]["start"], "%Y-%m-%d %H:%M:%S")
   if fullTime < friStart: day = "Thursday"
   elif fullTime < satStart: day = "Friday"
   elif fullTime < sunStart: day = "Saturday"
   else: day = "Sunday"
   startTime = time.strftime("%I:%M%p",fullTime)
   intTime = time.mktime(fullTime)
   intTime += events[eventID]["duration"]*60
   endTime = time.strftime("%I:%M%p",time.localtime(intTime))

   t = (eventID,venues[events[eventID]["venueId"]],
        events[eventID]["line1"],events[eventID]["line2"],
        events[eventID]["start"],startTime+'-'+endTime,
        bool(rawVenues[events[eventID]["venueId"]]["secondary"]),day)
   c.execute('insert into events values (?,?,?,?,?,?,?,?)',t)

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
