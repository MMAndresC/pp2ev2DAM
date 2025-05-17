## Puertos  
3308 - MariaDb  
8890 - Music app
  
## Testing  

Para lanzar todos los test
~~~  
mvn test 
~~~   
  
Lanzar determinados tests

~~~  
mvn test -Dtest="archivo_test.java"
~~~ 

## Docker

### Producción
Para crear fichero .jar en la carpeta **/target** para a partir de ahí crear la imagen de Docker:
~~~  
mvn clean package -DskipTests 
~~~  
- Generar la imagén de la app:
~~~
docker build -t music-api .  
~~~
- Levantar el contenedor:
~~~
docker compose -f docker-compose.prod.yml up -d
~~~

Para consultar los logs del contenedor:
~~~
docker logs music-api
~~~

  ### Desarrollo
 
- Levantar el contenedor de MariaDb:  
~~~
docker compose -f docker-compose.dev.yml up -d
~~~
El contenedor está corriendo en el puerto 3308 para que no entre en conflicto con MariaDb en local.  
  
Para entrar en el contenedor y realizar consultas directamente a la base de datos:  
  ~~~  
    docker exec -it music-mariadb-dev mariadb -u main_user -p
  ~~~  
Para arrancar la app:  
`mvn spring-boot:run`  

***

### Local
   Si ya está instalado Mariadb en el equipo y se quiere ejecutar en local  
  - Cambiar el puerto en el fichero **application.properties** 
~~~  
spring.datasource.url=jdbc:mariadb://localhost:3306/music  
~~~
Poner de nuevo el puerto 3306 o el que corresponda a Mariadb en local 

Para realizar consultas directamente a la base de datos, se puede conectar directamente si se tiene instalado la consola de Mariadb o bien desde la consola de comandos con:  
~~~  
mysql -u main_user -p -h localhost -P 3306
~~~  

  
## Arrancar el proyecto  

~~~  
  mvn spring-boot:run
~~~    

La primera vez se instalarán todas las dependencias especificadas en el pom.xml  
Si alguna dependencia no se ha instalado bien o algo no funciona correctamente con este comando se fuerza a recompilar:  

~~~  
  mvn clean install
~~~ 

## Poblar la base de datos  
  
En el primer arranque en entorno **dev** se poblará la base de datos con varios registros para cada tabla, no volvera a hacerlo si detecta que la tabla contiene registros  
Si se necesita forzar que las pueble de nuevo, cambiando en **application.properties** la configuración de arranque de la base de datos de esta manera:  
~~~  
spring.jpa.hibernate.ddl-auto=create-drop
~~~    
Se forzará a que al arrancar borre las tablas y las vuelva a crear  
Volver a poner la configuración inicial:
~~~  
spring.jpa.hibernate.ddl-auto=update
~~~