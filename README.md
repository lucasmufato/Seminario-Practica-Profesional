# Seminario-Practica-Profesional
Programa de Viajes Compartidos

esta es la rama master

## INSTRUCCIONES

Para hacer funcionar la interfaz web es necesario instalar Tomcat 8.

El directorio del sitio web *(1)* es donde deben estar los archivos HTML, CSS y javascript 

Dentro del directorio del sitio es necesario que existan los siguientes archivos:  

*   [...]/WEB-INF/ directorio que va a contener los otros archivos

*   [...]/WEB-INF/web.xml Archivo de configuracion

*   [...]/WEB-INF/lib/ directorio para librerias .jar *(2)*

*   [...]/WEB-INF/classes/ directorio para .class compilados. Debe respetar el arbol de paquetes

*   [...]/WEB-INF/classes/META-INF adentro va "persistence.xml", la configuracion de JPA

*(1)*: En Debian el directorio del sitio es /var/lib/tomcat8/webapps/ROOT  
*(2)*: En lib tuve que poner los siguientes archivos:
*    json-simple-1.1.1.jar
*    mysql-connector-java.jar
*    javax.persistence_2.1.0.v201304241213.jar
*    javax.persistence.source_2.1.0.v201304241213.jar
*    org.eclipse.persistence.jpa.modelgen_2.5.2.v20140319-9ad6abd.jar
*    org.eclipse.persistence.jpa.modelgen.source_2.5.2.v20140319-9ad6abd.jar
*    org.eclipse.persistence.jpars_2.5.2.v20140319-9ad6abd.jar
*    org.eclipse.persistence.jpars.source_2.5.2.v20140319-9ad6abd.jar
*    eclipselink.jar
//jasper reports requiere tambien:
*	commons-beanutils-1.9.0.jar
*	commons-collections-3.2.1.jar
*	commons-digester-2.1.jar
*	commons-logging-1.1.1.jar
*	groovy-all-2.4.3.jar
*	itext-2.1.7.js4.jar
*	jasperreports-6.2.0.jar
*	jfreechart-1.0.19.jar // este me parece que se me coló pero lo pongo igual
*	poi-3.10.1.jar


## NOTAS SOBRE EL CODIGO:

### Clase ControladorServlet

La clase ControladorServlet recibe peticiones HTTP como entrada y devuelve información en formato JSON.

Si se solicita el recurso mediante el comando GET de HTTP, devolvera información completa sobre Personas, Usuarios, Roles, Permisos y sus relaciones. Implementado en ControladorServlet.doGet()

Mediante el comando POST de HTTP puede solicitarse una operacion de alta/baja/modificación de dichas entidades. Implementado en ControladorServlet.doPost()

Toda peticion POST requiere dos parámetros obligatorios:  
	action: 'new', 'edit' o 'delete'  
	entity: 'persona', 'usuario', 'rol' ...  

Además, deberá incluir los campos necesarios para los atributos de la entidad en cuestión (nombre,  etc).  
Para las eliminaciones (action='delete') sólo es necesario el campo id.  
Para las altas (action='new') el campo id debe ser -1.  
