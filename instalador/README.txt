Pasos para construir el instalador

1. Instalar el programa nsis (Nullsoft Scriptable Install System) http://nsis.sourceforge.net/

2. Descargar y descomprimir en este directorio los siguientes archivos:
http://mirrors.nxnethosting.com/apache/tomcat/tomcat-8/v8.0.35/bin/apache-tomcat-8.0.35-windows-x86.zip
https://dev.mysql.com/get/Downloads/MySQL-5.7/mysql-5.7.13-win32.zip

3. Copiar el directorio webapps/ROOT de tomcat a este directorio

4. Borrar el contenido de WEB-INF/classes/META-INF/persistence.xml
El archivo debe existir pero estar vacio antes del proximo paso.

5. Ejecutar el comando:
makensis viajescompartidos.nsi

El instalador va a tardar algunos minutos mientras comprime los contenidos.
Particularmente MySQL lleva bastante tiempo.
Cuando este completo, se generara el archivo "InstalarViajesCompartidos.exe"
