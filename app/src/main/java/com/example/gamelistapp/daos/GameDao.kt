package com.example.gamelistapp.daos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.gamelistapp.models.Game

class GameDao(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "gamesdb"
        private const val DB_VERSION = 1
        private const val TABLE_NAME = "games"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                company TEXT NOT NULL,
                genre TEXT NOT NULL
            );
        """.trimIndent()
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun findAll(): List<Game> {
        val games = mutableListOf<Game>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, name, company, genre FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val company = cursor.getString(cursor.getColumnIndexOrThrow("company"))
                val genre = cursor.getString(cursor.getColumnIndexOrThrow("genre"))
                games.add(Game(id, name, company, genre))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return games
    }

    fun findById(id: Int): Game? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, name, company, genre FROM $TABLE_NAME WHERE id = ?", arrayOf(id.toString()))
        var game: Game? = null

        if (cursor.moveToFirst()) {
            game = Game(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getString(cursor.getColumnIndexOrThrow("company")),
                cursor.getString(cursor.getColumnIndexOrThrow("genre"))
            )
        }
        cursor.close()
        db.close()
        return game
    }

    fun insert(game: Game): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", game.name)
            put("company", game.company)
            put("genre", game.genre)
        }
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id
    }

    fun findByName(name: String): List<Game> {
        val games = mutableListOf<Game>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id, name, company, genre FROM $TABLE_NAME WHERE name LIKE ?",
            arrayOf("%$name%")
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val gameName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val company = cursor.getString(cursor.getColumnIndexOrThrow("company"))
                val genre = cursor.getString(cursor.getColumnIndexOrThrow("genre"))
                games.add(Game(id, gameName, company, genre))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return games
    }

    // *** NOVOS MÉTODOS ***

    fun update(game: Game): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", game.name)
            put("company", game.company)
            put("genre", game.genre)
        }
        val rowsAffected = db.update(TABLE_NAME, values, "id = ?", arrayOf(game.id.toString()))
        db.close()
        return rowsAffected > 0
    }

    fun delete(id: Int): Boolean {
        val db = writableDatabase
        val rowsDeleted = db.delete(TABLE_NAME, "id = ?", arrayOf(id.toString()))
        db.close()
        return rowsDeleted > 0
    }
}
