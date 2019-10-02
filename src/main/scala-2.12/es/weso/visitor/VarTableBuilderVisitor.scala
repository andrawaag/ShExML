package es.weso.visitor

import es.weso.ast._

import scala.collection.mutable

/**
  * Created by herminio on 26/12/17.
  */
class VarTableBuilderVisitor(val variableMap: mutable.HashMap[Variable, VarResult]) extends DefaultVisitor[Map[String, Any], Unit] {

  override def doVisit(ast: AST, optionalArgument: Map[String, Any]): Unit = ast match {
    case Prefix(name, value) => variableMap += ((name, value))
    case Source(name, value) => variableMap += ((name, value))
    case Query(name, value) => variableMap += ((name, value))
    case Expression(name, value) => variableMap += ((name, value))
    case Iterator(name, value, fields, iterators) => registerIterator(name, value, fields, iterators, optionalArgument)
    case NestedIterator(name, value, fields, iterators) => registerIterator(name, value, fields, iterators, optionalArgument)
    case Field(name, value) => {
      val fieldName = if(optionalArgument("variable") == "") name.name else optionalArgument("variable") + "." + name.name
      variableMap += ((Var(fieldName), value))
    }
    case m: Matchers => variableMap += ((m.name, m))
    case s: Shape => variableMap += ((s.shapeName, s))
    case default => super.visit(default, optionalArgument)
  }

  def registerIterator(name: Var, value: QueryClause, fields: List[Field], iterators: List[NestedIterator], optionalArgument: Map[String, Any]) {
    val iteratorName = if(optionalArgument("variable") == "") name.name else optionalArgument("variable") + "." + name.name
    val finalValue = value match {
      case FieldQuery(query) => optionalArgument("type") match {
        case XmlPath(_) => XmlPath(query)
        case JsonPath(_) => JsonPath(query)
      }
      case _ => value
    }
    variableMap += ((Var(iteratorName), finalValue))
    val topIteratorType = value match {
      case FieldQuery(_) => optionalArgument("type").asInstanceOf[QueryClause]
      case x: XmlPath => x
      case j: JsonPath => j
    }
    val newOptionalArgument = Map(
      "variable" -> iteratorName,
      "type" -> topIteratorType
    )
    fields.foreach(doVisit(_, newOptionalArgument))
    iterators.foreach(doVisit(_, newOptionalArgument))
  }

  override def doVisitDefault(): Unit = ???

}
