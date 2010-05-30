# -*- coding: utf-8 -*-
import demjson

venues = demjson.decode(open("venues.txt").read())
events = demjson.decode(open("events.txt").read())

for eventID in events:
   print events[eventID]["line1"], events[eventID]["start"]