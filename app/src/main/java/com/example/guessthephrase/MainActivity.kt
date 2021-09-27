package com.example.guessthephrase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

val listOfPhrases = Phrases().phrasesList // Phrases have separate class
lateinit var guessList: ArrayList<String>
lateinit var phraseView: TextView
lateinit var recyclerView: RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Set RecyclerView
        recyclerView = findViewById(R.id.rvGuesses)
        recyclerView.adapter = RecyclerViewAdapter(guessList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        phraseView = findViewById(R.id.tvPhrase)
        val phrasePosition = setPhrase()
    }

    private fun setPhrase(): Int {
        val phrasePosition = Random.nextInt(listOfPhrases.size)
        val chosenPhrase = listOfPhrases[phrasePosition]
        var phrase = ""
        for (character in chosenPhrase){
            phrase += if (character == ' ')
                ' '
            else
                '*'
        }
        phraseView.text = "$phrase\nGuessed letters: "
        return phrasePosition
    }
}