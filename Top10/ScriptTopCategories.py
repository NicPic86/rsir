import re
import collections
import string

def lcount(keyword, fname):
    with open(fname, 'r') as fin:
        return sum([1 for line in fin if keyword in line])

out_file = open("topCategories.sql","w")
out_file.write("--DROP TABLE topCategories;\nCREATE TABLE topCategories (category text,recurrence integer);\n")
file_content = open("./keywords.txt","r")
file_string = file_content.read()
tmpkeywords = re.sub('[\t\n]', '', file_string)
keywords = tmpkeywords.split(';')

for i in keywords:
    out_file.write("INSERT INTO topCategories (category,recurrence) VALUES ('"+i+"',"+str(lcount(i, "./user.txt"))+");\n")

out_file.close()
print "Execution terminated."



