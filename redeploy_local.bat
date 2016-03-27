rm -rf target
mvn tomcat7:redeploy -Dmaven.tomcat.path=/ -Dmaven.tomcat.url=http://laptop:8080/manager/text
