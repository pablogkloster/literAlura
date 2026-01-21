📚 LiterAlura

Aplicación de consola desarrollada en Java con Spring Boot que permite buscar, visualizar y guardar libros utilizando la API pública Gutendex (Proyecto Gutenberg), con persistencia local mediante JPA/Hibernate.

El proyecto está pensado especialmente para el mercado hispanohablante, priorizando la experiencia del usuario final.

✨ Características principales

🔍 Búsqueda de libros mediante la API de Gutendex:

Por título

Por autor

Por género

Por idioma

Por año

Top 10 libros más descargados

🌍 Traducción automática de títulos, géneros y resúmenes al español.

💾 Guardado de libros y autores en base de datos local.

📖 Biblioteca local con:

Listado de libros guardados

Búsqueda por autor

Conteo de libros por autor

🧭 Navegación mediante menús claros e intuitivos.

🎯 Enfoque del proyecto

LiterAlura fue diseñada desde la perspectiva del usuario:

Pensada para personas de habla hispana, incluso cuando la información original proviene mayormente en inglés.

Se priorizó un menú simple, guiado y consistente, evitando sobrecargar al usuario con resultados extensos.

Los listados muestran una cantidad controlada de resultados y permiten seleccionar qué libro ver en detalle.

Se evita repetir libros del mismo autor para mejorar la legibilidad y la toma de decisiones.

🛠️ Tecnologías utilizadas

Java 17+

Spring Boot

Spring Data JPA

Hibernate

H2 / PostgreSQL (según configuración)

API Gutendex

Jackson (JSON)

Maven

▶️ Ejecución

Clonar el repositorio

Configurar la base de datos (o usar H2)

Ejecutar la aplicación

Navegar mediante el menú en consola

📌 Notas finales

Este proyecto forma parte de un desafío educativo, pero fue desarrollado aplicando buenas prácticas de:

separación de responsabilidades

diseño orientado al usuario

reutilización de lógica

claridad en la interacción por consola


