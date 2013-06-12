import re
import collections
import string
import urllib2
from bs4 import BeautifulSoup

def wordCount(keyword, stringInfo):
    count = 0
    if keyword in stringInfo:
        if count > 1:
            return count
        count=count+1
    return count

out_file = open("event-category-recurrence.txt","w")
xml_file = open("./xml_file_final.xml", "r")

soup = BeautifulSoup(xml_file)
rows = soup.findAll("row")

file_content = open("./categories.txt","r")
file_string = file_content.read()
tmpkeywords = re.sub('[\t\n]', '', file_string)
keywords = tmpkeywords.split(';')

eventid = 0

for k in keywords:
    keywordCounter = 0
    c=0
    print k
    for count in rows:
        tempsoup = rows[c]
        try:        
            ei = tempsoup.findAll("field", {"name":"eventid"})
            if not ei[0].contents[0].strip():
                ei = "No info"
            eventid = ei[0].contents[0].strip()
        except Exception:
            eventid = 0
        try:
            fn = tempsoup.findAll("field", {"name":"fullname"})
            s = fn[0].contents[0].strip()
            fullname = s.decode('utf-8')
            fullname = fullname.replace("'","_")
            i = tempsoup.findAll("field", {"name":"info"})
            s = i[0].contents[0].strip()
            info = s.decode('utf-8')
            info = info.replace("'","_")
        except:
            info = "No info"
            fullname = "No info"
        stringKey = str(k)
        numkey = wordCount(stringKey, info)
        if numkey > 0:
            with open ("event-category-recurrence.txt", "a") as out_file:
                stringconcat = str(eventid)+","+stringKey+","+str(numkey)+"\n"
                out_file.write(stringconcat)
        c=c+1       

out_file.close()
print "Execution terminated!"
