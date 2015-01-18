Trains In City
===================
The local commuter railroad services a number of towns in Kiwiland. Because of monetary concerns, all of the tracks are 'one-way.' That is, a route from Kaitaia to Invercargill does not imply the existence of a route from Invercargill to Kaitaia. In fact, even if both of these routes do happen to exist, they are distinct and are not necessarily the same distance!

The purpose of this program is to help the railroad provide its customers with information about the routes. In particular, it computes the distance along a certain route, the number of different routes between two towns, and the shortest route between two towns.

Class Diagrams
-------------
![alt text](http://i.imgur.com/Z4jf2tF.png README.md URL")

Implementation Details
-------------
This application used Vogella's tutorial and its ***Dijkstra*** class with asist of Data Structures: Abstraction and Design Using Java book as a base and modified it as needed.

Applications starts with building a ***Graph*** object with information provided as text input from a file. Grap object holds vertex and edge informations as lists and contains methods to acces them. Same input file also contains task informations which will be applied on the graph. Task informations stored as ***GraphTask*** objects. Reading graph and task data is handled by ***GraphFromFileService*** and ***GraphTaskFromFileService*** classes.

Once Graph and GraphTask created application stars with executeing tasks by calling ***executeTasks*** in TestTrainsInCity class.

Results of the task printed on console using log4j console appander.

Missing Features 
-------------

* spring framework but Spring Tool Suite is used
* Unit, Integrity and Functional test
* Finding paths less than interms of travelling length
* Finding paths in maximum steps

References
-------------

 1. Vogella - Dijkstra's shortest path algorithm in Java - Tutorial - [Link](http://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html#algorithm_implementation)
 2. Data Structures: Abstraction and Design Using Java - Book - [Link](http://www.amazon.com/Data-Structures-Abstraction-Design-Using/dp/0470128704)
