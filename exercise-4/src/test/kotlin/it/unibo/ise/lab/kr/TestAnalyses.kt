package it.unibo.ise.lab.kr

import kotlin.test.Test
import kotlin.test.assertEquals

class TestAnalyses {
    @Test
    fun testParents() {
        val theory = Theories.run { parenthood + genders + grandParenthood + motherhood + fatherhood }
        val analysisResult = analyze(theory)
        val expectedFunctors = listOf(
            "abraham",
            "beutel",
            "haran",
            "isaac",
            "jacob",
            "joseph",
            "labam",
            "milcah",
            "nahor",
            "rachel",
            "rebecca",
            "sarah",
            "terah",
        ).map { Symbol(it) }.toSet()
        val expectedPredicates = setOf(
            Symbol("parent", 2),
            Symbol("male", 1),
            Symbol("female", 1),
            Symbol("grandparent", 2),
            Symbol("mother", 2),
            Symbol("father", 2),
        )
        val expectedVariables = listOf("X", "Y", "Z").map { Symbol(it) }.toSet()
        val expectedResult = Analysis(expectedFunctors, expectedPredicates, expectedVariables)
        assertEquals(expectedResult, analysisResult)
    }
}