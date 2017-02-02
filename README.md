The Term Project for CSSE 374. 
This program currently takes in a list of java classes and prints out their Name, fields, methods 
and Accessibilty (Public, Protected, Private)

HOW TO USE: 
Argument Flags:
-coi : highlights classes that violate the composition over inheritance design principle in orange
-sg  : highlights classes that are Singleton design patterns in blue
-p   : prints: Print Detector has been built and I'M DETECTING THINGS!
-u <.properties file path> : Sets the user settings file to be used instead of the default settings file
-s   : removes synthetic "lambda" methods from being showing in the UML diagram (or set to true in the properties file)
-r   : indicates that classes that are referenced in the given classes will also be printed in the UML diagram recursively (or set to true in the properties file)

general Use:
put all the flags that you want to use first before stating any of the classes you want drawn in the UML diagram. All flags MUST be before ALL java classes. Class names and flags should be seperated by one space. You can blacklist certain java class names by going into the properties file and inputing the classes that you do not want to printed recursively. the blacklist will not stop the classes given by the user from being printed


example properties file:
NOTE: every settings file needs a flags, synthetic, recursive, test and blacklist mappings. feel free to leave any mappings empty except for synthetic and recursive. they must be true or false

flags=-coi -sg -p
synthetic=true
recursive=true
test=javax/swing/JFrame java/awt/Frame
blacklist=java/


