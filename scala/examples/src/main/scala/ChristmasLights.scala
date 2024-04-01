import ChristmasLights.Instruction
import ChristmasLights.Instruction.{OFF, ON, TOGGLE}

import scala.Function.const
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
      case onRegex(c1, c2, c3, c4)     => ON(Coordinates(c1.toInt, c2.toInt), Coordinates(c3.toInt, c4.toInt))
      case offRegex(c1, c2, c3, c4)    => OFF(Coordinates(c1.toInt, c2.toInt), Coordinates(c3.toInt, c4.toInt))
      case toggleRegex(c1, c2, c3, c4) => TOGGLE(Coordinates(c1.toInt, c2.toInt), Coordinates(c3.toInt, c4.toInt))
  def intoCoordinates(c1: Coordinates, c2: Coordinates, effect: (x: Int, y: Int) => Unit): Unit =
    for {
      y <- Range.inclusive(c1.y, c2.y)
      x <- Range.inclusive(c1.x, c2.x)
    } yield effect(x, y)
  def countLitLights(instructions: String): Int =
    val lights = Range(0, 1000).flatMap(const(Range(0, 1000).map(const(false)))).toBuffer
    instructions
      .lines()
      .toScala(Vector)
      .map(Instruction.apply)
      .foreach:
        case ON(c1, c2)     => intoCoordinates(c1, c2, (x, y) => lights.update(y * 1000 + x, true))
        case OFF(c1, c2)    => intoCoordinates(c1, c2, (x, y) => lights.update(y * 1000 + x, false))
        case TOGGLE(c1, c2) => intoCoordinates(c1, c2, (x, y) => lights.update(y * 1000 + x, !lights(y * 1000 + x)))
    lights.count(identity)
