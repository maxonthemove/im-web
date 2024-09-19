#!/bin/bash
ps aux | grep app.jar | grep -v grep | awk '{print $2}' | while read pid
do
  echo "Killing process $pid"
  kill -9 $pid
done

nohup java -jar app.jar >log 2>&1 &
echo "service start complete. pid: $!"