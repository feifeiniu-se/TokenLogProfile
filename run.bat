@echo off

set CLASSPATH_OLD=%CLASSPATH%

set CLASSPATH=%CLASSPATH%;.\lib\BeehiveZ.jar

set CLASSPATH=%CLASSPATH%;.

java -Xms500m -Xmx500m  -classpath "%CLASSPATH%" -Djava.library.path=./lib cn.edu.thss.iise.beehivez.client.BusinessProcessModelManager

set CLASSPATH=%CLASSPATH_OLD%
set CLASSPATH_OLD= 