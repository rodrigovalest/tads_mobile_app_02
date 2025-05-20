package com.example.gamelistapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.gamelistapp.daos.GameDao
import com.example.gamelistapp.models.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QueryActivity : AppCompatActivity() {

    private lateinit var editTextSearchQuery: EditText
    private lateinit var buttonExecuteSearch: Button
    private lateinit var textViewSearchResults: TextView
    private lateinit var gameDao: GameDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query)

        gameDao = GameDao(this)

        editTextSearchQuery = findViewById(R.id.editTextSearchQuery)
        buttonExecuteSearch = findViewById(R.id.buttonExecuteSearch)
        textViewSearchResults = findViewById(R.id.textViewSearchResults)

        buttonExecuteSearch.setOnClickListener {
            val query = editTextSearchQuery.text.toString().trim()
            if (query.isNotEmpty()) {
                searchGames(query)
            } else {
                Toast.makeText(this, "Digite um termo para pesquisar", Toast.LENGTH_SHORT).show()
            }
        }
        val buttonBack = findViewById<Button>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun searchGames(query: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val results: List<Game> = gameDao.findByName(query)
                withContext(Dispatchers.Main) {
                    if (results.isNotEmpty()) {
                        val resultText = results.joinToString(separator = "\n\n") { game ->
                            "Nome: ${game.name}\nEmpresa: ${game.company}\nGênero: ${game.genre}"
                        }
                        textViewSearchResults.text = resultText
                    } else {
                        textViewSearchResults.text = "Nenhum jogo encontrado para '$query'."
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    textViewSearchResults.text = "Erro ao realizar a pesquisa."
                    Toast.makeText(this@QueryActivity, "Erro na pesquisa: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}
