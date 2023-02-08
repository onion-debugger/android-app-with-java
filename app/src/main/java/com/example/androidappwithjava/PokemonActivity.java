package com.example.androidappwithjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PokemonActivity extends AppCompatActivity {

    private TextView pokemonName;
    private TextView pokemonNumber;

    private TextView typeOne;
    private TextView typeTwo;
    private String url;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);

        url = getIntent().getStringExtra("url");

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        pokemonName = findViewById(R.id.pokemon_name);
        pokemonNumber = findViewById(R.id.pokemon_number);
        typeOne = findViewById(R.id.pokemon_type_one);
        typeTwo = findViewById(R.id.pokemon_type_two);

        loadMoreInfoAboutPokemon();
    }

    public void loadMoreInfoAboutPokemon() {
        typeOne.setText("");
        typeTwo.setText("");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {

                String name = response.getString("name");
                int id = response.getInt("id");
                pokemonName.setText(name);
                pokemonNumber.setText(String.format("#%03d", id));
                JSONArray typeEntries = response.getJSONArray("types");
                for (int i = 0; i < typeEntries.length(); i++) {
                    JSONObject typeEntry = typeEntries.getJSONObject(i);
                    int slot = typeEntry.getInt("slot");
                    String type = typeEntry.getJSONObject("type").getString("name");

                    if (slot == 1) {
                        typeOne.setText(type);
                    } else if (slot == 2) {
                        typeTwo.setText(type);
                    }

                }
            } catch (JSONException e) {
                Log.e("cs50", "Pokemon json error", e);
            }
        }, error -> {
            Log.e("cs50", "Pokemon details error: ", error);
        });

        requestQueue.add(request);
    }
}