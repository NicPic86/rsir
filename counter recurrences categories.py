import re
import collections
import string

def lcount(keyword, fname):
    with open(fname, 'r') as fin:
        return sum([1 for line in fin if keyword in line])

out_file = open("recurrences_for_categories.txt","w")
file_content = open("./keywords.txt","r")
file_string = file_content.read()
tmpkeywords = re.sub('[\t\n]', '', file_string)
keywords = tmpkeywords.split(';')

for i in keywords:
    out_file.write(i+","+str(lcount(i, "./event-category-recurrence.txt"))+"\n")

out_file.close()
print "Execution terminated."



