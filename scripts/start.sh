# 编译
# mvn clean install -Dmaven.test.skip=true

# 部署运行
#
APPNAME="ont-sourcing-0.0.1-SNAPSHOT.jar"
APPLOCATION="/home/ubuntu/ont-sourcing/target/ont-sourcing-0.0.1-SNAPSHOT.jar"
APPENV="/home/ubuntu/ont-sourcing/config/application-remotetest.properties"
APPPORT=7088
LOGLOCATION="/home/ubuntu/ont-sourcing/log/all.log"

# touch /root/ont-sourcing/log/all.log

#
ps -ef | grep $APPNAME | grep -v grep | awk '{print $2}' | xargs kill -9

#
# echo "nohup java -Dspring.profiles.active=$APPENV -Dserver.port=$APPPORT -jar $APPLOCATION >/dev/null 2>&1 &"
# nohup java -Dspring.profiles.active=$APPENV -Dserver.port=$APPPORT -jar $APPLOCATION >/dev/null 2>&1 &

echo "nohup java -Dspring.config.location=$APPENV -Dserver.port=$APPPORT -jar $APPLOCATION >/dev/null 2>&1 &"
nohup java -Dspring.config.location=$APPENV -Dserver.port=$APPPORT -jar $APPLOCATION >/dev/null 2>&1 &

#
tail -f $LOGLOCATION