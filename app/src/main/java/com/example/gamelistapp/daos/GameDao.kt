package com.example.gamelistapp.daos

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.gamelistapp.models.Game

class GameDao(context: Context) {

    private val dbHelper = GameDatabaseHelper(context)

    fun seedDatabase() {
        val db = dbHelper.writableDatabase

        val cursor = db.rawQuery("SELECT COUNT(*) FROM games", null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()

        if (count > 0) {
            db.close()
            return
        }

        db.beginTransaction()
        try {
            val gamesToInsert = listOf(
                Game("Zelda: Breath of the Wild", "Nintendo", "2017-03-03", "Adventure", "Open world action-adventure game."),
                Game("Super Mario Odyssey", "Nintendo", "2017-10-27", "Platformer", "Mario explores new worlds."),
                Game("God of War", "Sony", "2018-04-20", "Action", "Kratos' journey with his son."),
                Game("The Witcher 3", "CD Projekt", "2015-05-19", "RPG", "Open world RPG with Geralt."),
                Game("Minecraft", "Mojang", "2011-11-18", "Sandbox", "Build and explore blocky worlds.")
            )

            gamesToInsert.forEach { game ->
                val values = ContentValues().apply {
                    put("name", game.name)
                    put("company", game.company)
                    put("releaseDate", game.releaseDate)
                    put("genre", game.genre)
                    put("description", game.description)
                }
                db.insert("games", null, values)
            }

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun save(game: Game) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name", game.name)
            put("company", game.company)
            put("releaseDate", game.releaseDate)
            put("genre", game.genre)
            put("description", game.description)
        }
        db.insert("games", null, values)
        db.close()
    }

    fun findAll(): List<Game> {
        val games = mutableListOf<Game>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM games", null)

        if (cursor.moveToFirst()) {
            do {
                games.add(fromCursor(cursor))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return games
    }

    fun findByName(name: String): Game? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM games WHERE name LIKE ? LIMIT 1", arrayOf("%$name%"))

        val game = if (cursor.moveToFirst()) {
            fromCursor(cursor)
        } else {
            null
        }

        cursor.close()
        db.close()
        return game
    }

    fun findAllByName(name: String): List<Game> {
        val games = mutableListOf<Game>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM games WHERE name LIKE ?", arrayOf("%$name%"))

        if (cursor.moveToFirst()) {
            do {
                games.add(fromCursor(cursor))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return games
    }

    fun updateByName(name: String, updatedGame: Game): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name", updatedGame.name)
            put("company", updatedGame.company)
            put("releaseDate", updatedGame.releaseDate)
            put("genre", updatedGame.genre)
            put("description", updatedGame.description)
        }
        val rowsAffected = db.update("games", values, "name = ?", arrayOf(name))
        db.close()
        return rowsAffected > 0
    }

    fun deleteByName(name: String): Boolean {
        val db = dbHelper.writableDatabase
        val rowsDeleted = db.delete("games", "name = ?", arrayOf(name))
        db.close()
        return rowsDeleted > 0
    }

    private fun fromCursor(cursor: Cursor): Game {
        return Game(
            name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
            company = cursor.getString(cursor.getColumnIndexOrThrow("company")),
            releaseDate = cursor.getString(cursor.getColumnIndexOrThrow("releaseDate")),
            genre = cursor.getString(cursor.getColumnIndexOrThrow("genre")),
            description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
        )
    }
}
