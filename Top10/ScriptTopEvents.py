import re
import collections
import string

def lcount(keyword, fname):
    with open(fname, 'r') as fin:
        return sum([1 for line in fin if keyword in line])

out_file = open("topEvents.sql","w")
out_file.write("--DROP TABLE topEvents;\nCREATE TABLE topEvents (eventid integer,recurrence integer);\n")
file_content = open("./listEventIDs.txt","r")
file_string = file_content.read()
tmpkeywords = re.sub('[\t\n]', '', file_string)
keywords = tmpkeywords.split(';')

for i in keywords:
    out_file.write("INSERT INTO topEvents (eventid,recurrence) VALUES ("+i+","+str(lcount(i, "./recommender.txt"))+");\n")

out_file.close()
print "Execution terminated."



