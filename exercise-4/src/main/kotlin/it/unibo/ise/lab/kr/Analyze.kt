package it.unibo.ise.lab.kr

import it.unibo.tuprolog.core.Struct
import it.unibo.tuprolog.core.Term
import it.unibo.tuprolog.theory.Theory

data class Analysis(val functors: Set<Symbol>, val predications: Set<Symbol>, val variableNames: Set<Symbol>)

fun analyze(theory: Theory): Analysis {
    val predications = enumeratePredicates(theory)
        .map { Symbol(it.functor, it.arity) }
        .toSet()
    val functors = enumeratePredicates(theory)
        .enumerateSymbolsInStructs()
        .filter { it !in predications }
        .toSet()
    val variables = theory.rules.flatMap { it.variables }.distinct().map { Symbol(it.name) }.toSet()
    return Analysis(functors, predications, variables)
}

private fun enumeratePredicates(theory: Theory): Sequence<Struct> =
    theory.rules.asSequence()
        .flatMap { it.bodyItems.asSequence() + it.head }
        .filterIsInstance<Struct>()
        .filter { !it.isTrue  }

private fun <T : Term> Sequence<T>.enumerateSymbolsInStructs(): Sequence<Symbol> =
    sequence {
        for (item in filterIsInstance<Struct>()) {
            yield(Symbol(item.functor, item.arity))
            for (arg in item.args) {
                if (arg is Struct) {
                    yield(Symbol(arg.functor, arg.arity))
                    arg.argsSequence.enumerateSymbolsInStructs()
                }
            }
        }
    }
