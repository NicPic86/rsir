Recommender System for Scientific's Events
====

Inside this repository You will find all the scripts and files used for our project

In the Java folder you will find the code used for the Recommender System (final update to do!)

==
Files name


* script
* wikicfp.v1.2008.xml
* wikicfp.v1.2009.xml
* wikicfp.v1.2010.xml
* final.sql
* association event category.py 
* counter recurrences categories.py 
* creation script table ID_CAT.py 	
* creation script table REC_CAT.py 
* event-category-recurrence.txt 
* events_for_categories.txt 
* keywords.txt 	
* recurrences_for_categories.txt 	
* script table ID_CAT.sql 
* script table REC_CAT.sql 	
* script table event.sql 

===
Description of the file


- Script is written in python and it's used for reading the xml file and producing the .sql file.
- The three xml file contains all the events taken from the web site http://www.wikicfp.com/.
- We have compress the three xml in one file, called xml_final_file.xml (to big to store) and the script works on it 
  producing final.sql.
- final.sql is the script used for creating the database.
- association event category, written in python, associate every event to the category.
- counter recurrences categories counts how many categories events are associated to a category.
- creation script table ID_CAT and REC_CAT, are two script used for inserting the data into the database.
- event-category-recurrence is the resulting .txt from the script association event category.
- events_for_categories is the resulting .txt from the script creation script table ID_Cat.
- keyword.txt is the file with all the categories (taken from the web site http://www.wikicfp.com).
- recurrences_for_categories is the resulting .txt from the script creation script table REC_CAT.
- script table ID_CAT, REC_CAT, event .sql are script used to insert the data into the database.


	

