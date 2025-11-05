# Comandos

- qry query
- add add
- upd update
- sub submit
- del delete
- sel select

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

Piso Terrassa Av Barcelona:Comedor:F02
id:855, parent:850, label:Estados Unidos. Agosto 2004, generic:Fotoprix, tag:recuerdos:fotografías y video:album de fotos

## Editar

### Añadir una propiedad y valor

> edi add prop {valor}

Mostrará la lista de propiedades a utilizar. 
1. generic
2. tag

    Si se introduce un valor filtrará la lista por ese valor
    
    > edi add prop ta
    1. tag

Propiedades de tags. Si se introduce un valor filtrará
> pro 1 {valor}
1. recuerdos:fotograrías y video: album de fotos

Tomará el tercer registro como valor
> val 3

Tomará el texto como valor
> val Estados Unidos

### Actualizar una propiedad y valor

> edi upd prop

Mostrará la lista de tuplas de propiedad y valor

1. generic:Fotoprix
2. tag:recuerdos:fotografías y video:album de fotos

> del 1

Borrará la primera propiedad

> edi 2 {valor}

Editará el valor de la tupla 2. Mostrará la lista de valores posibles, filtrando opcionalmente por valor. 
1. recuerdos:fotograrías y video: album de fotos
