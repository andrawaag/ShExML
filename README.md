# ShExML

Shape Expressions Mapping Language (ShExML) is a DSL that offers a solution
for mapping and merging heterogeneous data sources. As being based on ShEx the
shape is the main foundation to define the transformations.

## Example
```
PREFIX : <http://example.com/>
SOURCE films_xml <https://rawgit.com/herminiogg/ShExML/master/src/test/resources/films.xml>
SOURCE films_json <https://rawgit.com/herminiogg/ShExML/master/src/test/resources/films.json>
QUERY film_ids_xml <//film/@id>
QUERY film_names_xml <//film/name>
QUERY film_years_xml <//film/year>
QUERY film_countries_xml <//film/country>
QUERY film_directors_xml <//film/director>
QUERY film_ids_json <$.films[*].id>
QUERY film_names_json <$.films[*].name>
QUERY film_years_json <$.films[*].year>
QUERY film_countries_json <$.films[*].country>
QUERY film_directors_json <$.films[*].director>
EXPRESSION film_ids <$films_xml.film_ids_xml UNION $films_json.film_ids_json>
EXPRESSION film_names <$films_xml.film_names_xml UNION $films_json.film_names_json>
EXPRESSION film_years <$films_xml.film_years_xml UNION $films_json.film_years_json>
EXPRESSION film_countries <$films_xml.film_countries_xml UNION $films_json.film_countries_json>
EXPRESSION film_directors <$films_xml.film_directors_xml UNION $films_json.film_directors_json>

:Films :[film_ids] {
    :name [film_names] ;
    :year [film_years] ;
    :country [film_countries] ;
    :director [film_directors] ;
}
```
This example shows how to map and merge two files (in JSON and XML) with different films. In the first part, the
declarations, we can define some 'variables' that can be used inside the shapes. Prefixes used in the resulting RDF,
sources to the files, queries to be applied over the files and expressions to merge and transform the queries results.
Then, the shapes are defined as in ShEx but using the previously defined expressions or composing them inside the
square brackets.

## Run the example
To run this example clone this project and run the following command:
```
> sbt "run films.shexml"
```
You can design your own files and run them using the same command but changing films.shexml for the path or the URL to
your target file.

