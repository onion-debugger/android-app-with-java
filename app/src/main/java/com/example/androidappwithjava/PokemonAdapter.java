package com.example.androidappwithjava;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {
    public static class PokemonViewHolder extends RecyclerView.ViewHolder {
        public  LinearLayout containerView;
        public TextView pokemonName;

        PokemonViewHolder(View view) {
            super(view);
            containerView = view.findViewById(R.id.pokedex_row);
            pokemonName = view.findViewById(R.id.pokedex_name);

            containerView.setOnClickListener(v -> {
                Pokemon current = (Pokemon) containerView.getTag();
                Intent intent = new Intent(v.getContext(), PokemonActivity.class);
                intent.putExtra("url", current.getUrl());

                v.getContext().startActivity(intent);
            });
        }
    }

    private List<Pokemon> pokemonList =  new ArrayList<>();
    private RequestQueue requestQueue;

    PokemonAdapter(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        loadPokemonFromRemote();
    }

    public void loadPokemonFromRemote() {
        String url = "https://pokeapi.co/api/v2/pokemon?limit=50";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray results = response.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);
                    String name = result.getString("name");
                    pokemonList.add(new Pokemon(
                                    name.substring(0, 1).toUpperCase() + name.substring(1),
                                    result.getString("url")
                    ));
                }
                notifyDataSetChanged(); // defined on the recyclerview
            } catch (JSONException e) {
                Log.d("cs50", "Json Error: ", e);
            }
         }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("cs50", "Pokemon List error", error);
            }
        });

        // Kick-offs the request to load the pokemon data
        requestQueue.add(request);
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pokodex_row, parent, false);
        return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        Pokemon current = pokemonList.get(position);
        holder.pokemonName.setText(current.getName());

        // A tag is an object associated with a view
        holder.containerView.setTag(current);
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }
}
