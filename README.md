This GitHub Project contains the development process artifacts for a python prototype of the 'data import' algorithm for the Vehicle Survey problem
It contains -
    the original problem statement, in
	1-Project Brief.pdf
	1-sample data.txt
    a brief requirements analysis, in
	2-Requirements Analysis.pdf
    a high-level architecture, in
	3-Functional Hierarchy.pdf
    a detailed analysis of the Data Import process, in
	4-Import - Analysis Model.pdf
    a detailed pseudo-code design of the Data Import algorithm, in
	5-Import - Design Model.pdf
    an ECLIPSE project, TrafficSurvey, created with PyDev, that implements the Data Import algortihm in Python, containing
        a stand-alone python script that can be executed from a command line (or the Python Launcher App in MacOSX)
            DataImport.py
        a copy of the sample data (referred to by name in the script)
            test-data.txt
        a copy of the sample results as a .CSV file (with heading)
            test-results.csv

The CSV records contain, for each vehicle passing the counter,
	ID		sequential record number
	Direction	UP/DN  : Down trips pass over both A and B sensors
	Day		1...5
	Time		ms. since midnight  (0...86,399,999)
	Duration	elapsed between the two axles, in ms.
	Speed		estimated speed, assuming a 2.5m wheelbas
	Separation	time between vehicles in te same direction, in seconds
This file can be imported into a spreadsheet or a database.

