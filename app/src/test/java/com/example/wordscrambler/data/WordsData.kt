package com.example.wordscrambler.data

const val MAX_NO_OF_WORDS = 10
const val SCORE_INCREASE = 20

val allWords: Set<String> =
    setOf(
        "at",
        "sea",
        "home",
        "arise",
        "banana",
        "android",
        "birthday",
        "briefcase",
        "motorcycle",
        "cauliflower"
    )


// like we are using a fixed set of words in this test case where we can pull a word by length based on a single scrambled instance length
// so created a map of string length as key for the string itself and a fun to get a word based on the length of scrambled string
private val wordLengthMap: Map<Int, String> = allWords.associateBy({it.length}, {it})
internal fun getUnscrambledWord(scrambledWord: String) = wordLengthMap[scrambledWord.length] ?: ""