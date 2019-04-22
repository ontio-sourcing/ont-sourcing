#
APPNAME="ont-sourcing-0.0.1-SNAPSHOT.jar"
APPLOCATION="/root/ont-sourcing/target/ont-sourcing-0.0.1-SNAPSHOT.jar"
APPENV="remotetest"
APPPORT=7088
LOGLOCATION="/root/ont-sourcing/log/all.log"

# touch /root/ont-sourcing/log/all.log

#
ps -ef | grep $APPNAME | grep -v grep | awk '{print $2}' | xargs kill -9

#
echo "nohup java -Dspring.profiles.active=$APPENV -Dserver.port=$APPPORT -jar $APPLOCATION >/dev/null 2>&1 &"
nohup java -Dspring.profiles.active=$APPENV -Dserver.port=$APPPORT -jar $APPLOCATION >/dev/null 2>&1 &

#
tail -f $LOGLOCATION