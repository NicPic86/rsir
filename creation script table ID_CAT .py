import re
import collections
import string

out_file = open("events_for_categories.txt","w")
script_sql = open("script table ID_CAT.sql","w")
result_file = open("event-category-recurrence.txt", "r")

script_sql.write("-- DROP TABLE id_cat;\n")
script_sql.write("CREATE TABLE ID_CAT(keyword VARCHAR(40), idsevent VARCHAR(1200));\n")

lines_list = result_file.readlines()
i=0
lengthList = len(lines_list)
line_new = lines_list[i]
line_list_new = line_new.split(",")
cat_new = line_list_new[1]
cat_old = line_list_new[1]
events = []
idEvent = 0

while i<=lengthList:

    if i<lengthList:
        cat_new = line_list_new[1]
        idEvent = line_list_new[0]    
        line_new = lines_list[i]
        line_list_new = line_new.split(",")
        events.append(idEvent)
    if i>=lengthList:
        cat_new = "loop_terminated"
    if cat_new != cat_old:
        idEvent = line_list_new[0]
        events.append(idEvent)
        stringEvents = ""
        stringSQL = ""
        stringEvents = ",".join(events)
        stringSQL = "INSERT INTO ID_CAT (keyword, idsevents) VALUES ('"+cat_old+"', '"+stringEvents+"');"
        script_sql.write(stringSQL+"\n")
        out_file.write(cat_old+":"+stringEvents+"\n")
        events = []

    cat_old = cat_new
    i+=1
    
out_file.close()
script_sql.close()
print "Terminated"

