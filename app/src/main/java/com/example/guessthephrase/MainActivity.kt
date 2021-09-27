package com.example.guessthephrase

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

lateinit var phrase: Phrase
lateinit var turn: String
var guessCount = 10
lateinit var guessList: ArrayList<String>
lateinit var guessedLetters: ArrayList<Char>
lateinit var constraintLayout: ConstraintLayout
lateinit var phraseView: TextView
lateinit var letterView: TextView
lateinit var recyclerView: RecyclerView
lateinit var input: EditText
lateinit var button: Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        constraintLayout = findViewById(R.id.cl)
        phraseView = findViewById(R.id.tvPhrase)
        letterView = findViewById(R.id.tvGuessedLetters)
        input = findViewById(R.id.etGuess)
        button = findViewById(R.id.btnGuess)

        // set game
        setGame()

        button.setOnClickListener {
            if (input.text.isEmpty()){
                Snackbar.make(constraintLayout, "Enter a $turn", Snackbar.LENGTH_SHORT).show()
            }
            else
                when(turn) {
                    "phrase" -> checkPhraseGuess()
                    "letter" -> checkLetterGuess()
                }
        }

    }

    private fun setGame(){
        phrase = Phrase() // phrases logic have separate class
        guessCount = 10
        guessList = arrayListOf()
        guessedLetters = arrayListOf()
        phraseView.text = "Phrase: ${phrase.encodePhrase()}"
        letterView.text = "Guessed Letters: "
        input.setText("")
        recyclerView = findViewById(R.id.rvGuesses)
        recyclerView.adapter = RecyclerViewAdapter(guessList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        turn = "phrase"
    }

    private fun checkPhraseGuess(){
        val userPhrase = input.text.toString()
        if (userPhrase.equals(phrase.getPhrase(), true)){
            alert() // that's it? check if there's something else
        }
        else {
            guessList.add("Wrong guess: $userPhrase")
            input.setText("")
            recyclerView.adapter!!.notifyDataSetChanged()
        }
        input.hint = "Guess a letter"
        turn = "letter"
    }

    private fun checkLetterGuess(){
        // First make sure that there is one letter entered, show a snack bar otherwise
        if (input.text.length > 1){
            Snackbar.make(constraintLayout, "Enter one letter only", Snackbar.LENGTH_SHORT).show()
            input.setText("")
            return
        }

        val letter = input.text.toString()[0]
        // Then check if that letter is in the phrase chosen using letterOccurrences()
        val occurrences = phrase.letterOccurrences(letter)
        if (occurrences > 0){
            // if yes, add to guessList "Found $occurrences $letter(s)\n$--guessCount guesses remaining", and update the phrase using phrase.decodePhrase()
            guessList.add("Found $occurrences $letter(s)\n${--guessCount} guesses remaining")
            val phraseDecoded = phrase.decodePhrase(phraseView.text.toString(), letter)
            phraseView.text = "Phrase: $phraseDecoded"
            recyclerView.adapter!!.notifyDataSetChanged()
            input.setText("")
            // if phraseDecoded is phrase.getPhrase() or guessCount == 0
            if (phraseDecoded == phrase.getPhrase() || guessCount == 0){
                // alert user with the phrase and disable input & button
                alert()
            }
        }
        else {
            // if no, add to guessList "No $letter s found\n$--guessCount guesses remaining"
            guessList.add("No ${letter}s found\n${--guessCount} guesses remaining")
            recyclerView.adapter!!.notifyDataSetChanged()
            input.setText("")
            if (guessCount == 0){
                // alert user with the phrase and disable input & button
                alert()
            }
        }
        // add letter to guessedLetters and update letterView
        guessedLetters.add(letter)
        letterView.text = "Guessed Letters: $guessedLetters"
        turn = "phrase"
        input.hint = "Guess the full phrase"
    }

    private fun alert(){
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("The phrase was: ${phrase.getPhrase()}.")
                .setPositiveButton("Play Again", DialogInterface.OnClickListener{
                    _, _ -> setGame()
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, _ -> dialog.cancel()
                    input.isEnabled = false
                    button.isEnabled = false
                })
        val alert = dialogBuilder.create()
        alert.setTitle("Game ended!")
        alert.show()
    }

}