#!/bin/bash
ps aux | grep app.jar | grep -v grep | awk '{print $2}' | while read pid
do
  echo "Killing process $pid"
  kill -9 $pid
done

echo "service shutdown."