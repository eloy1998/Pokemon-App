package com.example.pokemon

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import kotlin.random.Random




class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var heightTextView: TextView
    private lateinit var fetchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.pokemonImage)
        nameTextView = findViewById(R.id.pokemonName)
        heightTextView = findViewById(R.id.pokemonHeight)
        fetchButton = findViewById(R.id.fetchButton)

        fetchButton.setOnClickListener {
            fetchPokemon()
        }

        fetchPokemon() // Fetch one at startup
    }

    private fun fetchPokemon() {
        val randomId = Random.nextInt(1, 151)
        val url = "https://pokeapi.co/api/v2/pokemon/$randomId"

        val client = AsyncHttpClient()
        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>?,
                response: JSONObject
            ) {
                val name = response.getString("name")
                val height = response.getInt("height")
                val imageUrl = response.getJSONObject("sprites")
                    .getString("front_default")

                nameTextView.text = "Name: ${name.replaceFirstChar { it.uppercase() }}"
                heightTextView.text = "Height: $height"
                Glide.with(this@MainActivity)
                    .load(imageUrl)
                    .into(imageView)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                throwable: Throwable?,
                errorResponse: JSONObject?
            ) {
                nameTextView.text = "Failed to load Pokémon"
                heightTextView.text = ""
                imageView.setImageDrawable(null)
            }
        })
    }
}

