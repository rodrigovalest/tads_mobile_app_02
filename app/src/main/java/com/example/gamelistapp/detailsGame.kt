package com.example.gamelistapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gamelistapp.daos.GameDao
import com.example.gamelistapp.models.Game

class detailsGame : AppCompatActivity() {
    private var game: Game? = null
    private var originalGameName: String = ""

    private lateinit var editTextGame: EditText
    private lateinit var editTextDate: EditText
    private lateinit var editTextCompany: EditText
    private lateinit var editTextGenre: EditText
    private lateinit var editTextDesc: EditText
    private lateinit var buttonDelete: Button
    private lateinit var buttonEdit: Button

    private var editing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_details_game)

        editTextGame = findViewById(R.id.editViewGame)
        editTextDate = findViewById(R.id.editTextDate)
        editTextCompany = findViewById(R.id.editTextCompany)
        editTextGenre = findViewById(R.id.editTextGenre)
        editTextDesc = findViewById(R.id.editTextLineDesc)
        buttonDelete = findViewById(R.id.buttonDelete)
        buttonEdit = findViewById(R.id.buttonEdit)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bundle = intent.extras
        if (bundle != null) {
            originalGameName = bundle.getString("gameName", "God of War")

            if (originalGameName.isNotEmpty()) {
                val dao = GameDao(this)
                this.game = dao.findByName(originalGameName)
                println(game)
            }
        }

        populateFields()
        setFieldsEnabled(false)

        buttonDelete.setOnClickListener {
            if (editing) {
                cancelEdit()
            } else {
                deleteGame()
            }
        }
        buttonEdit.setOnClickListener {
            if (editing) {
                updateGame()
            } else {
                enableEditMode()
            }
        }
    }

    private fun populateFields() {
        editTextGame.setText(game?.name ?: "")
        editTextDate.setText(game?.releaseDate ?: "")
        editTextCompany.setText(game?.company ?: "")
        editTextGenre.setText(game?.genre ?: "")
        editTextDesc.setText(game?.description ?: "")
    }

    private fun setFieldsEnabled(isEnabled: Boolean) {
        editTextGame.isEnabled = isEnabled
        editTextDate.isEnabled = isEnabled
        editTextCompany.isEnabled = isEnabled
        editTextGenre.isEnabled = isEnabled
        editTextDesc.isEnabled = isEnabled
    }

    private fun deleteGame() {
        val dao = GameDao(this)
        dao.deleteByName(originalGameName)
        finish()
    }

    private fun enableEditMode() {
        setFieldsEnabled(true)
        editing = true
        buttonDelete.text = "Cancelar"
        buttonEdit.text = "Salvar"
    }

    private fun updateGame() {
        val updatedName = editTextGame.text.toString()
        val updatedDate = editTextDate.text.toString()
        val updatedCompany = editTextCompany.text.toString()
        val updatedGenre = editTextGenre.text.toString()
        val updatedDesc = editTextDesc.text.toString()

        val updatedGame = Game(
            name = updatedName,
            company = updatedCompany,
            releaseDate = updatedDate,
            genre = updatedGenre,
            description = updatedDesc
        )

        val dao = GameDao(this)
        dao.updateByName(originalGameName, updatedGame)

        finish()
    }

    private fun cancelEdit() {
        populateFields()
        setFieldsEnabled(false)
        editing = false
        buttonDelete.text = "Excluir"
        buttonEdit.text = "Editar"
    }
}