package com.example.pokemon

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var pokemonInput: EditText
    private lateinit var searchButton: Button
    private lateinit var pokemonImage: ImageView
    private lateinit var pokemonName: TextView
    private lateinit var pokemonHeight: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Link XML views
        pokemonInput = findViewById(R.id.pokemon_input)
        searchButton = findViewById(R.id.fetchButton)
        pokemonImage = findViewById(R.id.pokemonImage)
        pokemonName = findViewById(R.id.pokemonName)
        pokemonHeight = findViewById(R.id.pokemonHeight)

        // Clear any default text
        pokemonName.text = ""
        pokemonHeight.text = ""

        // Button click listener
        searchButton.setOnClickListener {
            val query = pokemonInput.text.toString().trim().lowercase()

            if (query.isNotEmpty()) {
                fetchPokemonData(query)
            } else {
                Toast.makeText(this, "Please enter a Pokémon name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchPokemonData(pokemonNameQuery: String) {
        val url = "https://pokeapi.co/api/v2/pokemon/$pokemonNameQuery"
        val client = AsyncHttpClient()

        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                response: JSONObject
            ) {
                val name = response.getString("name").replaceFirstChar { it.uppercaseChar() }
                val height = response.getInt("height").toString()
                val imageUrl = response.getJSONObject("sprites")
                    .getString("front_default")

                pokemonName.text = "Name: $name"
                pokemonHeight.text = "Height: $height"

                Glide.with(this@MainActivity)
                    .load(imageUrl)
                    .into(pokemonImage)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                throwable: Throwable?,
                errorResponse: JSONObject?
            ) {
                Toast.makeText(
                    this@MainActivity,
                    "Failed to load Pokémon",
                    Toast.LENGTH_SHORT
                ).show()

                pokemonName.text = ""
                pokemonHeight.text = ""
                pokemonImage.setImageDrawable(null)
            }
        })
    }
}