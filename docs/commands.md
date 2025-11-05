# Comandos

- qry query
- add add
- upd update
- del delete
- sel select
- key clave
- val valor
- nav navegación

## Query (commando qry)

### Búsqueda full-text de un texto

> qry ft estados

id:838, parent:835, label:Estados Unidos de América, generic:H Kliczkowski, tag:ocio y cultura:lectura:libro de viajes
id:844, parent:835, label:Estados Unidos. Un descubrimiento culinario, generic:Könemann, tag:ocio y cultura:lectura:libro de gastronomía
id:855, parent:850, label:Estados Unidos. Agosto 2004, generic:Fotoprix, tag:recuerdos:fotografías y video:album de fotos
id:913, parent:907, label:Estados Unidos. Suroeste y Las Vegas, generic:El País Aguilar. Guías visuales, tag:ocio y cultura:lectura:guía de viajes

## Select (comando sel)

Si seleccionamos un item, nos mostrará sus parents y los detalles del mismo.

> sel 855

id: 855
01 parent: 850 Piso Terrassa Av Barcelona:Comedor:F02
02 label:Estados Unidos. Agosto 2004
03 generic:Fotoprix
04 tag:recuerdos:fotografías y video:album de fotos

## CRUD de un registro

### Añadir propiedad

> add key {valor}

Mostrará la lista de propiedades a utilizar. 
1. generic
2. tag

    Si se introduce un valor filtrará la lista por ese valor
    
    > add key ta
    1. tag

Propiedades de tags. Si se introduce un valor filtrará
> key 1 {valor}
1. recuerdos:fotograrías y video: album de fotos

> key perico

Introduce una clave a medida

Tomará el tercer registro como valor
> val 3

Tomará el texto como valor
> val Estados Unidos

### Borrar propiedad

> del 4

Borrará la primera propiedad

### Actualizar propiedad

> upd 2 {valor}

Editará el valor de la tupla 2. En primer lugar mostrará el valor actual (valor 0), y a continuación mostrará la lista de valores posibles, filtrando opcionalmente por valor. 
0. recuerdos:fotograrías y video: album de fotos
1. recuerdos:fotograrías y video: album de fotos

> val 1

## Añadir (add)

- Añadir un hijo del registro seleccionado (**chi**ld, hijo)

> add chi 

- Añadir un hermano o sibling (**sib**ling, mismo padre) del registro seleccionado

> add sib

- Añadir un registro en la raíz (**roo**t)

> add roo

- Añadir una propiedad al registro seleccionado

> add key {valor}

## Navegar (nav)

Cuando se ha seleccionado que se quiere editar un valor con jerarquía, se puede navegar abajo y arriba.

Supongamos que nuestro contexto es

id: 855
01 parent: 850 Piso Terrassa Av Barcelona:Comedor:F02
02 label:Estados Unidos. Agosto 2004

Editamos el campo de parent

> upd 1
0. 850 Piso Terrassa Av Barcelona:Comedor:F02

> nav up
1. 850 Piso Terrassa Av Barcelona:Comedor

> nav sib
1. 850 Piso Terrassa Av Barcelona:Comedor:F03
2. 850 Piso Terrassa Av Barcelona:Comedor:F04

> nav dow
1. 850 Piso Terrassa Av Barcelona:Comedor:F02:caja1
2. 850 Piso Terrassa Av Barcelona:Comedor:F02:caja2

Como en los otros casos, en el momento que deseemos, indicamos 

> val 1
