  import java.util.UUID

class Helper {

  def allCapitals(input: String): String = {
    input.toUpperCase
  }

  def addOne(number: Int): Int = {
    number + 1
  }

  def getName(str: String): String = {
    str.trim.split(" ", 2)(0)
  }

  def getSurname(str: String): String = {
    str.trim.split(" ", 2)(1)
  }

  def nameAndYear(name: String, year: Int): String = {
    name + year.toString
  }

  def getNameParts(name: String): Array[String] = {
    name.split(" ")
  }

  def generateUUID(): UUID = {
      UUID.randomUUID()
    }
}
