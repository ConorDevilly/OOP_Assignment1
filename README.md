# OOP_Visualisation_Assignment
The objective of this assignment is to "create a striking visualisation of a dataset using Processing".

# Background
The dataset I have chosen to use is the CVE data from 2003 - 2015.
CVE stands for "Common Vulnerabilities and Exposures". 
It is "a dictionary of publicly known information security vulnerabilities and exposures".
The CVE website can be found at: https://cve.mitre.org/

CVE's can be categorised into CWEs. 
CWE stands for "Common Weakness Enumeration".

Basically, a CVE is a security vulnerability report and a CWE is a category of security vulnerability.

For this project, I extract the CWE fields from the CVE data from 2003 - 2015.
I put all of these fields into an SQLite database and create graphs based on that information.

My reason for using an SQLite database is:

1. The CVE data comes to about 400MB in total. This is a lot of data to go through everytime the program is run. So to reduce load times, I make a small program that extracts the CWE field from each CVE and put it in an SQLite database that the main program will use.
2. There's a lot of different graphs that can be toggled on and off in this project and its much easier to select the data you want using SQL rather than Java.

In case anyone looking at this is actually interested in CVE / CWE data, I'm using the NVD Cross Section View of CWE.
More information about this can be found at: https://nvd.nist.gov/cwe.cfm

To download the data this project is based on go to:
https://nvd.nist.gov/download.cfm
And download each XML (Version 2.0) file from 2003 - 2015.

# The Project Itself
There are three graphs to chose from: a word cloud, a line graph and a pie chart.
By default, the project starts in Word Cloud mode.
To select a different graph mode, click the associated icon on the left of the program.

![WordCloudMode](/WordCloudScreen.png)

In Word Cloud and Pie Chart mode, the slider on the right controls what year the graph you are looking at is for.
NOTE: In word cloud mode, its takes some time to switch between years. This is because there's a lot of checking and rechecking positions when calculating where every word can fit.
In Pie Chart mode, if you move the mouse around the chart you'll see the name of the CWE each segment represents.
You'll also get a count of the CWE's reported in that year the the percent that each segment accounts for.

![PieChartMode](/PieChartScreen.png)

In Line Graph mode, there is a list on the right containing a list of different security vulnerability types.
If you select one, it will turn to red and the associated graph will draw on the screen.
You can select many graphs. They will all be drawn on top of the one axis to allow for comparisons.
If you hover the mouse over a graph, it will highlight that graph and display information about its associated CWE below the axis.

![LineGraphMode](/LineGraphScreen.png)

The drawing and scaling of graphs in Line Graph mode is all animated (animated quite slowly though).

# How this project meets the requirements in the Assignment Spec.
Core Features:

* I selected the CVE dataset as the data I wanted to investigate
* This dataset is loaded into my program
* It includes a Line Graph. We covered Line Graphs in class.
* It includes a Word Cloud. We did not cover Word Clouds in class.
* It includes a menu (kind of...the icons on the right are my menu).
* Simple stats about the dataset are included in Pie Chart mode

Advanced Features:

(I'm not entirely sure what counts as an Advanced Feature so I'm just going to mention what I think is an Advanced Feature)
* ControlP5 is used to implement the GUI
*Classes are used, along with a bit on inheritance.
* Line graphs scale in proportion to the maximum value of all the graphs selected.
* The drawning and scaling of Line Graphs is animated.
* Data is taken from XML documents rather than CSV or plain text files.
* SQL is used to store and retrieve the data.
* Use of HashMaps and LinkedHashMaps.
* Line graphs can be highlighted (Advanced feature as figuring out if the mouse is on a line is really difficult).
