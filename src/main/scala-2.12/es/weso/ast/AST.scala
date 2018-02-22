package es.weso.ast


/**
  * Created by herminio on 20/12/17.
  */
sealed trait AST

case class ShExML(declaration: List[Declaration], shape: List[Shape]) extends AST
case class Declaration(declarationStatement: DeclarationStatement) extends AST
case class Shape(shapeName: ShapeVar, shapePrefix: String, action: ExpOrVar, predicateObjects: List[PredicateObject]) extends VarResult


sealed trait DeclarationStatement extends AST

case class Prefix(name: Var, url: URL) extends DeclarationStatement
case class Source(name: Var, filePath: URL) extends DeclarationStatement
case class Query(name: Var, queryClause: QueryClause) extends DeclarationStatement
case class Expression(name: Var, exp: Exp) extends DeclarationStatement


sealed trait QueryClause extends VarResult {
  val query: String
}

case class JsonPath(query: String) extends QueryClause
case class XmlPath(query: String) extends QueryClause


sealed trait Exp extends ExpOrVar with VarResult
sealed trait LeftUnion extends Exp
sealed trait RightUnion extends Exp

case class SourceQuery(fileVar: Var, expressionVar: Var) extends LeftUnion with RightUnion
case class Union(left: LeftUnion, right: RightUnion) extends RightUnion
case class StringOperation(left: SourceQuery, right: SourceQuery, unionString: String) extends LeftUnion with RightUnion
case class Join(leftUnion: SourceQuery, rightUnion: SourceQuery, joinClause: SourceQuery) extends Exp


sealed trait ExpOrVar extends AST

sealed trait Variable extends ExpOrVar
case class Var(name: String) extends Variable
case class ShapeVar(name: String) extends Variable

case class PredicateObject(predicate: Predicate, objectOrShapeLink: ObjectOrShapeLink) extends AST
case class Predicate(prefix: String, extension: String) extends AST


sealed trait ObjectOrShapeLink extends AST

case class ObjectElement(prefix: String, action: ExpOrVar) extends ObjectOrShapeLink
case class ShapeLink(shape: ShapeVar) extends ObjectOrShapeLink

sealed trait VarResult extends AST

case class URL(url: String) extends VarResult