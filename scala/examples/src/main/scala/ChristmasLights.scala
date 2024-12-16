import ChristmasLights.Instruction.{OFF, ON, TOGGLE}

import java.lang.System.lineSeparator
import scala.jdk.StreamConverters.*
import scala.util.matching.Regex

object ChristmasLights:

  case class Coordinates(x: Int, y: Int)

  enum Instruction:
    case ON(corner1: Coordinates, corner2: Coordinates)
    case OFF(corner1: Coordinates, corner2: Coordinates)
    case TOGGLE(corner1: Coordinates, corner2: Coordinates)

  object Instruction:
    private val onRegex: Regex = """turn on (\d+),(\d+) through (\d+),(\d+)""".r
    private val offRegex: Regex = """turn off (\d+),(\d+) through (\d+),(\d+)""".r
    private val toggleRegex: Regex = """toggle (\d+),(\d+) through (\d+),(\d+)""".r
    def apply(line: String): Instruction = line match
      case onRegex(x1, y1, x2, y2)     => ON(Coordinates(x1.toInt, y1.toInt), Coordinates(x2.toInt, y2.toInt))
      case offRegex(x1, y1, x2, y2)    => OFF(Coordinates(x1.toInt, y1.toInt), Coordinates(x2.toInt, y2.toInt))
      case toggleRegex(x1, y1, x2, y2) => TOGGLE(Coordinates(x1.toInt, y1.toInt), Coordinates(x2.toInt, y2.toInt))

  def applyToRectangle[T](c1: Coordinates, c2: Coordinates, effect: (x: Int, y: Int) => T): Seq[T] =
    Range(c1.y, c2.y + 1).flatMap(y => Range(c1.x, c2.x + 1).map(x => effect(x, y)))

  def countLitLights(instructions: String): Int =
    val lights = applyToRectangle(Coordinates(0, 0), Coordinates(999, 999), (_, _) => false).toBuffer
    instructions
      .split(lineSeparator())
      .map(Instruction.apply)
      .foreach:
        case ON(c1, c2)     => applyToRectangle(c1, c2, (x, y) => lights.update(y * 1000 + x, true))
        case OFF(c1, c2)    => applyToRectangle(c1, c2, (x, y) => lights.update(y * 1000 + x, false))
        case TOGGLE(c1, c2) => applyToRectangle(c1, c2, (x, y) => lights.update(y * 1000 + x, !lights(y * 1000 + x)))
    lights.count(identity)
