rm -rf target
mvn tomcat7:redeploy -Dmaven.tomcat.path=/ -Dmaven.tomcat.url=http://localhost/manager/text
