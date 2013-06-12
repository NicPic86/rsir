import re
import collections
import string

script_sql = open("script table REC_CAT.sql","w")
cat_file = open("recurrences_for_categories.txt", "r")

script_sql.write("-- DROP TABLE REC_CAT;\n")
script_sql.write("CREATE TABLE REC_CAT(keyword VARCHAR(40), recurrence INT);\n")

lines_list = cat_file.readlines()
i = 0

while i <len(lines_list):
    if i == len(lines_list)-1:
        break
    line_list_new = lines_list[i].split(",")
    stringSQL = "INSERT INTO REC_CAT (keyword, recurrence) VALUES ('"+line_list_new[0]+"', "+line_list_new[1]+");"
    #print stringSQL
    script_sql.write(stringSQL+"\n")
    i+=1

script_sql.close()
print "Terminated"
