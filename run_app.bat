@echo off
title MediKiosk Java Port Launcher
echo =========================================
echo Starting MediKiosk...
echo Please wait while the application compiles.
echo =========================================
echo Starting Database Server...
start "" /B "mariadb_env\mariadb-10.11.4-winx64\bin\mysqld.exe" --console
call maven_env\apache-maven-3.9.6\bin\mvn.cmd clean javafx:run
echo Shutting down Database Server...
"mariadb_env\mariadb-10.11.4-winx64\bin\mysqladmin.exe" -u root shutdown
pause
