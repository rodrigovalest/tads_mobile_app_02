package com.example.gamelistapp

import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.gamelistapp.daos.GameDao
import com.example.gamelistapp.models.Game

class DetailsGameActivity : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var editCompany: EditText
    private lateinit var editGenre: EditText
    private lateinit var buttonUpdate: Button
    private lateinit var buttonDelete: Button

    private lateinit var gameDao: GameDao
    private var currentGame: Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_game)

        editName = findViewById(R.id.editTextDetailName)
        editCompany = findViewById(R.id.editTextDetailCompany)
        editGenre = findViewById(R.id.editTextDetailGenre)
        buttonUpdate = findViewById(R.id.buttonUpdate)
        buttonDelete = findViewById(R.id.buttonDelete)

        gameDao = GameDao(this)

        val gameId = intent.getIntExtra("gameId", -1)
        if (gameId != -1) {
            currentGame = gameDao.findById(gameId)
            currentGame?.let {
                editName.setText(it.name)
                editCompany.setText(it.company)
                editGenre.setText(it.genre)
            }
        }

        buttonUpdate.setOnClickListener {
            val name = editName.text.toString().trim()
            val company = editCompany.text.toString().trim()
            val genre = editGenre.text.toString().trim()

            if (name.isEmpty() || company.isEmpty() || genre.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else {
                currentGame?.let {
                    it.name = name
                    it.company = company
                    it.genre = genre
                    val updated = gameDao.update(it)
                    if (updated) {
                        Toast.makeText(this, "Jogo atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Erro ao atualizar o jogo", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        buttonDelete.setOnClickListener {
            currentGame?.let {
                AlertDialog.Builder(this)
                    .setTitle("Excluir Jogo")
                    .setMessage("Tem certeza que deseja excluir '${it.name}'?")
                    .setPositiveButton("Sim") { _, _ ->
                        val deleted = gameDao.delete(it.id)
                        if (deleted) {
                            Toast.makeText(this, "Jogo excluído com sucesso!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, "Erro ao excluir o jogo", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Não", null)
                    .show()
            }
        }
    }
}
