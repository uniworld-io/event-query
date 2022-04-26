#!/bin/bash
while true; do
  pid=`ps -ef | grep unichaineventquery | grep -v grep | awk '{print $2}'`
  if [ -n "$pid" ]; then
    kill -15 $pid
    echo "ending unichain event query process"
    sleep 1
  else
    echo "unichain event query killed successfully!"
    break
  fi
done

nohup java -jar target/unichaineventquery-1.0.0-SNAPSHOT.jar >> query.log 2>&1 &

sleep 10
echo "ok!"
