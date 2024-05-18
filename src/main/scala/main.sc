import zio.prelude.fx.ZPure
import zio.prelude.ForEachOps

type TestResult[+A] = ZPure[Nothing, Unit, Unit, Any, Nothing, A]

def filterM[A](
  elems: Iterable[A]
)(predicate: A => TestResult[Boolean]): TestResult[Iterable[A]] =
  elems
    .forEach(elem => predicate(elem).map((_, elem)))
    .map(_.filter(_._1))
    .map(_.map(_._2))

def filterNotM[A](
  elems: Iterable[A]
)(predicate: A => TestResult[Boolean]): TestResult[Iterable[A]] =
  elems
    .forEach(elem => predicate(elem).map((_, elem)))
    .map(_.filter(!_._1))
    .map(_.map(_._2))

def isEven(in: Int): TestResult[Boolean] =
  for {
    num <- ZPure.succeed(in)
  } yield num % 2 == 0

def isOdd(in: Int): TestResult[Boolean] =
  for {
    num <- ZPure.succeed(in)
  } yield num % 2 != 0

def isNumber(in: String): TestResult[Boolean] =
  for {
    str <- ZPure.succeed(in)
    isNum = str.toIntOption match {
      case Some(_) => true
      case None    => false
    }
  } yield isNum

{
  val data: List[String] =
    List("1", "two", "3", "four", "5", "6", "7", "8", "9", "ten")
  println("data => " + data)

  for {
    test1_1 <- filterM(data)(isNumber)
    test1_2 <- data.filterM(isNumber)
    () = println("_______.filterM : " + test1_1)
    () = println("forEach.filterM : " + test1_2)
    test2_1 <- filterM(data)(isNumber)
    test2_11 <- filterNotM(test2_1.map(_.toInt))(isEven)
    test2_2 <- data.filterM(isNumber)
    test2_21 <- test2_2.map(_.toInt).filterM(isOdd)
    () = println("_______.filterNotM : " + test2_11)
    () = println("forEach.filterNotM : " + test2_21)
  } yield ()
}.run
