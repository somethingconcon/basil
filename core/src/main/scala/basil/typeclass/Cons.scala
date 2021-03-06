package basil.typeclass

import cats.Functor

/**
  * Typeclass to provide capability of prepending an element into
  * a source of element
  *
  * todo: Create laws for it with drop?
  */
trait Cons[Col[_]] {
  def cons[E](cols: Col[E], e: E): Col[E]
}

object Cons {
  def apply[F[_]](implicit c: Cons[F]): Cons[F] = c

  implicit val ListCons: Cons[List] = new Cons[List] {
    override def cons[E](cols: List[E], e: E): List[E] = e :: cols
  }
  implicit def stackCons[E[_]: Functor, F[_]: Cons]: Cons[EffStack[E, F, ?]] =
    new Cons[EffStack[E, F, ?]] {
      override def cons[I](cols: EffStack[E, F, I], e: I): EffStack[E, F, I] = {
        Functor[E].map(cols) { fa =>
          Cons[F].cons(fa, e)
        }
      }
    }
}
