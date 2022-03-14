package it.unibo.ise.lab.kr

import it.unibo.tuprolog.core.Atom
import it.unibo.tuprolog.core.Struct
import it.unibo.tuprolog.core.Term

data class Functor(val name: String, val arity: Int = 0)

fun herbrand(vararg functors: Functor): Sequence<Term> {
    val constants = functors.filter { it.arity == 0 }.map { Atom.of(it.name) }.toSet()
    val functorsWithArity = functors.filter { it.arity > 0 }.toSet()
    return sequence {
        yieldAll(constants)
        val universe: MutableSet<Term> = constants.toMutableSet()
        var expansion: Set<Term>
        do {
            expansion = expand(universe, functorsWithArity).filter { it !in universe }.toSet()
            universe.addAll(expansion)
            yieldAll(expansion)
        } while (expansion.isNotEmpty())
    }
}

internal fun expand(universe: Set<Term>, functors: Set<Functor>): Sequence<Term> = sequence {
    for ((name, arity) in functors) {
        for (arguments in power(universe, arity)) {
            yield(Struct.of(name, arguments))
        }
    }
}

internal fun <T> power(items: Iterable<T>, n: Int): Sequence<List<T>> {
    return when {
        n <= 0 -> emptySequence()
        n == 1 -> items.asSequence().map { listOf(it) }
        else -> sequence {
            for (item in items) {
                for (smallerPower in power(items , n - 1)) {
                    yield(listOf(item) + smallerPower)
                }
            }
        }
    }
}

fun main() {
    herbrand(Functor("a"), Functor("b"), Functor("f", 2)).take(50).forEach {
        println(it)
    }
}
