package com.example.guessthephrase

import android.util.Log
import java.text.ParsePosition
import kotlin.random.Random

class Phrase {

    /*
     phrasesList:
     Predefined set of phrases
     */
    private val phrasesList: List<String> = listOf(
            "Work hard",
            "Do not give up",
            "No pain no gain",
            "Everything will be OK"
    )

    /*
     phrase:
     randomly-chosen phrase from phrasesList
     */
    private val phrase: String

    /*
     init:
     initializes the object by choosing a phrase randomly from phrasesList
     */
    init {
        phrase = phrasesList[Random.nextInt(phrasesList.size)]
    }


    /*
     getPhrase()
     Gets the phrase
     */
    fun getPhrase(): String {
        return phrase
    }

    /*
     encodePhrase():
     Encodes the phrase with * (spaces ignored)
     */
     fun encodePhrase(): String {
        var encodedPhrase = ""
        for (character in phrase){
            encodedPhrase += if (character == ' ')
                character
            else
                '*'
        }
        return encodedPhrase
    }

    /*
     decodePhrase():
     Decodes given letter in given encoded phrase
     */
    fun decodePhrase(encodedPhrase: String, letter: Char): String {
        var decodedPhrase = ""
        var encodedPhraseTrimmed = encodedPhrase.removePrefix("Phrase: ") // to avoid dealing with title
        for (character in phrase.indices){
            decodedPhrase += if (phrase[character].equals(letter, true)) {
                phrase[character]
            } else {
                encodedPhraseTrimmed[character]
            }
        }
        return decodedPhrase
    }

    /*
     letterOccurrence():
     returns given letter occurrences in the phrase
     src: https://www.techiedelight.com/count-occurrences-character-string-kotlin/
     */
    fun letterOccurrences(letter: Char): Int {
       return phrase.filter { it.equals(letter, true) }.count()
    }
}