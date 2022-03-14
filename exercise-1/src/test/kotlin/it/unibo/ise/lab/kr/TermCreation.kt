package it.unibo.ise.lab.kr

import it.unibo.tuprolog.core.*
import it.unibo.tuprolog.core.List
import it.unibo.tuprolog.core.parsing.TermParser
import org.gciatto.kt.math.BigDecimal
import org.gciatto.kt.math.BigInteger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@Suppress("USELESS_IS_CHECK")
class TermCreation {
    /**
     * Supports parsing terms and clauses out of strings using the Prolog syntax for both
     */
    private val parser = TermParser.withNoOperator()

    /**
     * Supports formatting a term in user-friendly Prolog syntax
     */
    private val formatter = TermFormatter.prettyVariables()

    @Test
    fun atomCreation() {
        val representations = listOf("anAtom", "'an atom with spaces'")
        for (repr in representations) {
            val term: Term = TODO("Create an $Atom term whose representation is equal to $repr")
            val expected = parser.parseTerm(repr)

            assertEquals(expected, term)
            assertTrue(term is Struct)
            assertTrue(term is Constant)
            assertTrue(term is Atom)
            assertEquals(repr.replace("'", ""), term.value)
            assertEquals(repr, formatter.format(term))
        }
    }

    @Test
    fun integerCreation() {
        val integers = listOf(1, 2, 0, -1)
        for (int in integers) {
            val term: Term = TODO("Create an $Integer term whose value is equal to $int")
            val representation = int.toString()
            val expected = parser.parseTerm(representation)

            assertEquals(expected, term)
            assertTrue(term is Numeric)
            assertTrue(term is Constant)
            assertTrue(term is Integer)
            assertEquals(BigInteger.of(int), term.value)
            assertEquals(representation, formatter.format(term))
        }
    }

    @Test
    fun realCreation() {
        val representations = listOf("1.2", "-3.4", "0.0", kotlin.math.PI.toString())
        for (repr in representations) {
            val term: Term = TODO("Create a $Real term whose representation is equal to $repr")
            val expected = parser.parseTerm(repr)

            assertEquals(expected, term)
            assertTrue(term is Numeric)
            assertTrue(term is Constant)
            assertTrue(term is Real)
            assertEquals(BigDecimal.of(repr), term.value)
            assertEquals(repr, formatter.format(term))
        }
    }

    @Test
    fun variableCreation() {
        val names = listOf("A", "B", "_", "_A", "_B", "SomeVariable")
        for (name in names) {
            val term: Term = TODO("Create a ${Var}iable term whose name is equal to $name")
            val expected = parser.parseTerm(name)

            assertNotEquals(expected, term) // notice this!
            assertTrue(expected.equals(term, useVarCompleteName = false))
            assertTrue(term is Var)
            assertEquals(name, term.name)
            assertEquals(name, formatter.format(term))
            assertTrue(term.completeName.startsWith(name + "_"))
            assertEquals(name == "_", term.isAnonymous)
        }
    }

    @Test
    fun structCreation() {
        val representation = "person(giovanni, ciatto, 30)"

        val term: Term = TODO("Create a ${Struct}ured term whose representation is equal to $representation")
        val expected = parser.parseTerm(representation)

        assertEquals(expected, term)
        assertTrue(term is Struct)
        assertEquals("person", term.functor)
        assertEquals(3, term.arity)
        assertEquals(
            listOf(Atom.of("giovanni"), Atom.of("ciatto"), Integer.of(30)),
            term.args
        )
        assertEquals(term[0], Atom.of("giovanni"))
        assertEquals(term[1], Atom.of("ciatto"))
        assertEquals(term[2], Integer.of(30))
        assertEquals(representation, formatter.format(term))

        val newTerm = term.addLast(Var.of("DateOfBirth"))
        val newRepresentation = "person(giovanni, ciatto, 30, DateOfBirth)"
        val newExpected = parser.parseTerm(newRepresentation)

        assertTrue(newExpected.equals(newTerm, useVarCompleteName = false))
    }

    @Test
    fun listCreation() {
        val representation = "[1, a, f(x)]"

        val term: Term = TODO("Create a ${List} term whose representation is equal to $representation")
        val expected = parser.parseTerm(representation)

        assertEquals(expected, term)
        assertTrue(term is Struct)
        assertTrue(term is List)
        assertTrue(term is Cons)
        assertEquals(".", term.functor)
        assertEquals(2, term.arity)
        assertEquals(term[0], Integer.of(1))
        assertEquals(term[1], List.of(Atom.of("a"), Struct.of("f", Atom.of("x"))))
        assertEquals(representation, formatter.format(term))

        assertEquals(
            actual = term,
            expected = Cons.of(
                head = Integer.of(1),
                tail = Cons.of(
                    head = Atom.of("a"),
                    tail = Cons.of(
                        head = Struct.of("f", Atom.of("x")),
                        tail = EmptyList.instance
                    )
                )
            )
        )
    }
}