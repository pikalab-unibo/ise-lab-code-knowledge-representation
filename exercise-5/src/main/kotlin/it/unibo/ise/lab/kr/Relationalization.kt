package it.unibo.ise.lab.kr

import it.unibo.tuprolog.core.Atom
import it.unibo.tuprolog.core.Fact
import it.unibo.tuprolog.core.Indicator
import it.unibo.tuprolog.core.Struct
import it.unibo.tuprolog.theory.Theory

fun relationalise(theory: Theory, functor: String, vararg attributes: String): Theory {
    val relationalTheory = theory.get(Indicator.of(functor, attributes.size))
        .flatMapIndexed { index, rule ->
            val key = Atom.of("instance$index")
            sequenceOf(Struct.of(functor, key)) +
                attributes.asSequence().mapIndexed { attributeIndex, attribute ->
                    Struct.of(attribute, key, rule.head[attributeIndex])
                }
        }.map(Fact::of)
    return Theory.of(relationalTheory)
}
