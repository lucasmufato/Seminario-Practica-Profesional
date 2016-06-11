@ECHO OFF

:: BatchGotAdmin
:-------------------------------------
REM  --> Check for permissions
    IF "%PROCESSOR_ARCHITECTURE%" EQU "amd64" (
>nul 2>&1 "%SYSTEMROOT%\SysWOW64\cacls.exe" "%SYSTEMROOT%\SysWOW64\config\system"
) ELSE (
>nul 2>&1 "%SYSTEMROOT%\system32\cacls.exe" "%SYSTEMROOT%\system32\config\system"
)

REM --> If error flag set, we do not have admin.
if '%errorlevel%' NEQ '0' (
    echo Requesting administrative privileges...
    goto UACPrompt
) else ( goto gotAdmin )

:UACPrompt
    echo Set UAC = CreateObject^("Shell.Application"^) > "%temp%\getadmin.vbs"
    set params = %*:"=""
    echo UAC.ShellExecute "cmd.exe", "/c ""%~s0"" %params%", "", "runas", 1 >> "%temp%\getadmin.vbs"

    "%temp%\getadmin.vbs"
    del "%temp%\getadmin.vbs"
    exit /B

:gotAdmin
    pushd "%CD%"
    CD /D "%~dp0"
:--------------------------------------    


java JavaHome >"%TEMP%\java_home.txt"
set /p JAVA_HOME=<"%TEMP%\java_home.txt"
set /p JRE_HOME=<"%TEMP%\java_home.txt"



IF NOT DEFINED JRE_HOME (
	ECHO ADVERTENCIA: DEBE INSTALAR JAVA PARA EJECUTAR VIAJES COMPARTIDOS
	TIMEOUT 10
)

IF EXIST mysql5 (
	IF NOT EXIST mysql5\data (
		ECHO INICIALIZANDO DATOS DE MySQL
		mysql5\bin\mysqld --log_syslog=0 --console --initialize-insecure

		ECHO INICIANDO SERVIDOR DE BASES DE DATOS MySQL
		START /B mysql5\bin\mysqld --log_syslog=0 --max_allowed_packet=32M -C utf8

		TIMEOUT 5
		ECHO ESTABLECIENDO PASSWORD PREDETERMINADO
		mysql5\bin\mysqladmin -u root password root
	) ELSE (
		ECHO INICIANDO MySQL
		START /B mysql5\bin\mysqld --log_syslog=0 --max_allowed_packet=32M -C utf8
	)
)

IF EXIST tomcat8 (
	ECHO INICIANDO SERVIDOR WEB TOMCAT
	SET CATALINA_OPTS=-Xmx512m
	CD tomcat8
	START /B bin\catalina.bat start
	CD ..
)

ECHO ESPERE 20 SEGUNDOS PARA QUE INICIE SERVIDOR TOMCAT
TIMEOUT 20
START /B http://localhost/configurar