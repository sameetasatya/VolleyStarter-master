package com.example.volleystarter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView plotTextView;
    ImageView posterImageView;
    EditText searchbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plotTextView = findViewById(R.id.plot_tv);
        posterImageView = findViewById(R.id.poster_iv);
        searchbar = findViewById(R.id.search_bar);
    }

    public void fetchData(View view) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String searchItem = String.valueOf(searchbar.getText());
        String url ="http://www.omdbapi.com/?apikey=11a900d7&t=" + searchItem;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject responseObject = new JSONObject(response);
                            String responsePiecePlot = responseObject.getString("Plot");
                            plotTextView.setText("Plot: " + responsePiecePlot);
                            String responsePieceMoviePoster = responseObject.getString("Poster");
                            Picasso.get().load(responsePieceMoviePoster).into(posterImageView);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                plotTextView.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
