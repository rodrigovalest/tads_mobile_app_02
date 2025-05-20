package com.example.gamelistapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamelistapp.adapter.MainGameAdapter
import com.example.gamelistapp.daos.GameDao

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dao: GameDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView2)
        recyclerView.layoutManager = LinearLayoutManager(this)

        dao = GameDao(this)
        dao.seedDatabase()
        val allGames = dao.findAll()

        val adapter = MainGameAdapter(allGames) { game ->
            val intent = Intent(this, detailsGame::class.java)
            intent.putExtra("gameName", game.name)
            startActivity(intent)
        }

        recyclerView.adapter = adapter

        findViewById<Button>(R.id.button).setOnClickListener {
            startActivity(Intent(this, QueryActivity::class.java))
        }
    }
}

