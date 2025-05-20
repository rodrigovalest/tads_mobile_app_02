package com.example.gamelistapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.gamelistapp.daos.GameDao
import com.example.gamelistapp.models.Game

class MainActivity : AppCompatActivity() {

    private lateinit var listViewGames: ListView
    private lateinit var editTextName: EditText
    private lateinit var editTextCompany: EditText
    private lateinit var editTextGenre: EditText
    private lateinit var buttonAdd: Button
    private lateinit var buttonSearch: Button

    private lateinit var gameDao: GameDao
    private var gamesList = mutableListOf<Game>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listViewGames = findViewById(R.id.listViewGames)
        editTextName = findViewById(R.id.editTextName)
        editTextCompany = findViewById(R.id.editTextCompany)
        editTextGenre = findViewById(R.id.editTextGenre)
        buttonAdd = findViewById(R.id.buttonAdd)
        buttonSearch = findViewById(R.id.buttonSearch)

        gameDao = GameDao(this)

        if (gameDao.findAll().isEmpty()) {
            gameDao.insert(Game(0, "The Legend of Zelda", "Nintendo", "Ação/Aventura"))
            gameDao.insert(Game(0, "Super Mario Bros", "Nintendo", "Plataforma"))
        }

        carregarLista()

        buttonAdd.setOnClickListener {
            val name = editTextName.text.toString().trim()
            val company = editTextCompany.text.toString().trim()
            val genre = editTextGenre.text.toString().trim()

            if (name.isEmpty() || company.isEmpty() || genre.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else {
                val game = Game(0, name, company, genre)
                val id = gameDao.insert(game)
                if (id > 0) {
                    Toast.makeText(this, "Jogo adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                    limparCampos()
                    carregarLista()
                } else {
                    Toast.makeText(this, "Erro ao adicionar o jogo", Toast.LENGTH_SHORT).show()
                }
            }
        }

        buttonSearch.setOnClickListener {
            val intent = Intent(this, QueryActivity::class.java)
            startActivity(intent)
        }

        listViewGames.setOnItemClickListener { _, _, position, _ ->
            val game = gamesList[position]
            val intent = Intent(this, DetailsGameActivity::class.java)
            intent.putExtra("gameId", game.id)
            startActivity(intent)
        }
    }

    private fun carregarLista() {
        gamesList = gameDao.findAll().toMutableList()
        val nomes = gamesList.map { it.name }
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nomes)
        listViewGames.adapter = adapter
    }

    private fun limparCampos() {
        editTextName.text.clear()
        editTextCompany.text.clear()
        editTextGenre.text.clear()
    }
}
