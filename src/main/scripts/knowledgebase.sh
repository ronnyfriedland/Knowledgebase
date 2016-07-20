#!/bin/sh
### BEGIN INIT INFO
# Provides:          Knowledgebase 2.0
# Required-Start:    $local_fs $time $syslog
# Required-Stop:     $local_fs $time $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Description:       Starts/Stops the knowledgebase web application
### END INIT INFO

SCRIPT="java -Xms32m -Xmx512m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=heapdump.hprof -Djava.util.logging.config.file=conf/logging.properties -jar knowledgebase-0.9.1-SNAPSHOT.jar 2>&1"
PIDFILE=knowledgebase.pid

cd $(dirname $(readlink -f $0))

start() {
  if [ -f $PIDFILE ] && kill -0 $(cat $PIDFILE); then
    echo 'Service already running' >&2
    return 1
  fi
  echo 'Starting service…' >&2
  $SCRIPT &
  echo "$!" > $PIDFILE
  echo 'Service started' >&2
}

stop() {
  if [ ! -f "$PIDFILE" ] || ! kill -0 $(cat "$PIDFILE"); then
    echo 'Service not running' >&2
    return 1
  fi
  echo 'Stopping service…' >&2
  kill -15 $(cat "$PIDFILE") && rm -f "$PIDFILE"
  echo 'Service stopped' >&2
}


case "$1" in
  start)
    start
    ;;
  stop)
    stop
    ;;
  restart)
    stop
    start
    ;;
  *)
    echo "Usage: $0 {start|stop|restart}"
esac