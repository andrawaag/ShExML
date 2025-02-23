package com.herminiogarcia.shexml.rmlPrettyPrint

import com.herminiogarcia.shexml.{MappingLauncher, RDFStatementCreator}
import org.apache.jena.datatypes.xsd.XSDDatatype
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

class FilmsMySQLTest extends AnyFunSuite with Matchers with RDFStatementCreator with RMLTestConversion {

  private val example =
    """
      |PREFIX : <http://example.com/>
      |PREFIX dbr: <http://dbpedia.org/resource/>
      |PREFIX schema: <http://schema.org/>
      |PREFIX xs: <http://www.w3.org/2001/XMLSchema#>
      |SOURCE films_database <jdbc:mysql://localhost:3306/films>
      |ITERATOR films_iterator <sql: SELECT * FROM films;> {
      |    FIELD id <id>
      |    FIELD name <name>
      |    FIELD year <year>
      |    FIELD country <country>
      |    FIELD director <director>
      |}
      |
      |EXPRESSION films <films_database.films_iterator>
      |
      |:Films :[films.id] {
      |    :type :Film ;
      |    :name [films.name] @en ;
      |    :year [films.year] xs:gYear ;
      |    :country [films.country] ;
      |    :director [films.director] ;
      |}
    """.stripMargin

  private val mappingLauncher = new MappingLauncher("root", "root")
  private val result = mappingLauncher.launchRMLTranslation(example, true)
  private val prefix = "http://example.com/"
  private val output = doTranslation(result, prefix).getDefaultModel

  test("Shape 8 is translated correctly") {
    assert(output.contains(createStatement(prefix, "8", "type", "Film")))
    assert(output.contains(createStatementWithLiteral(prefix, "8", "name", "Tenet", "en")))
    assert(output.contains(createStatementWithLiteral(prefix, "8", "year", "2020", XSDDatatype.XSDgYear)))
    assert(output.contains(createStatementWithLiteral(prefix, "8", "country", "UK", XSDDatatype.XSDstring)))
    assert(output.contains(createStatementWithLiteral(prefix, "8", "director", "Christopher Nolan", XSDDatatype.XSDstring)))
  }

  test("Shape 9 is translated correctly") {
    assert(output.contains(createStatement(prefix, "9", "type", "Film")))
    assert(output.contains(createStatementWithLiteral(prefix, "9", "name", "Batman Begins", "en")))
    assert(output.contains(createStatementWithLiteral(prefix, "9", "year", "2005", XSDDatatype.XSDgYear)))
    assert(output.contains(createStatementWithLiteral(prefix, "9", "country", "USA", XSDDatatype.XSDstring)))
    assert(output.contains(createStatementWithLiteral(prefix, "9", "director", "Christopher Nolan", XSDDatatype.XSDstring)))
  }

}
