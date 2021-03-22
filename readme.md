# Ejemplos de lectura/escritura con Kafka

Este módulo está enfocado a servir como ejemplo básico de la configuración e implementación de código para escribir y leer eventos de un topic Kafka.

La idea general es utilizar un solo tópico donde se almacenarán los eventos generados por alguna de las acciones realizadas en la aplicación, como puede ser la creación, actualización o borrado de los datos de una persona.

Estos eventos serán leídos desde dentro de la misma aplicación y se mostrarán en una sección de las páginas web usadas en la aplicación.

La visualización mostrará, por tanto, todas las acciones que estén realizando todos los usuarios de la aplicación, las cuales se estarán volcando al mismo topic y, por tanto, serán leídas por todos los participantes del curso.

