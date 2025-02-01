exec /opt/homebrew/bin/liquibase \
--driver=org.postgresql.Driver \
--url="jdbc:postgresql://localhost:5432/masterjava" \
--username=postgres \
--password= \
--changeLogFile=databaseChangeLog.sql \
--classpath=/opt/homebrew/bin \
update