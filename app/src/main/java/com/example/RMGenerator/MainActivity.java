package com.example.RMGenerator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView characterNameTV;
    ImageView posterImageView;
    EditText searchbar;
    Spinner monthSelection;
    Spinner daySelection;
    ListView sqListView;

    String responsePieceCharacterImage;
    String responsePieceCharName;

    Integer[] months  = new Integer[]{ 1,2,3,4,5,6,7,8,9,10,11,12};
    Integer[] days  = new Integer[]{ 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31 };

    FirebaseDatabase database;
    DatabaseReference squad;
    DatabaseReference dataUserName;
    DatabaseReference dataCharacterName;
    DatabaseReference dataCharacterImage;

    ArrayList<String> squadMembers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        monthSelection = findViewById(R.id.select_M);
        daySelection = findViewById(R.id.select_D);
        characterNameTV = findViewById(R.id.plot_tv);
        posterImageView = findViewById(R.id.poster_iv);
        searchbar = findViewById(R.id.search_bar);
        sqListView = findViewById(R.id.squadLV);


        ArrayAdapter<String> squadAdapter = new ArrayAdapter<>(this, android.R.layout.simple_gallery_item, squadMembers);
        monthSelection.setAdapter(squadAdapter);

        ArrayAdapter<Integer> adapterM = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, months);
        monthSelection.setAdapter(adapterM);
        ArrayAdapter<Integer> adapterD = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, days);
        daySelection.setAdapter(adapterD);

        //Instantiate firebase database reference
        database = FirebaseDatabase.getInstance();




    }

    public void fetchData(View view) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        String searchVal = String.valueOf(calculateSearchValue());

        //String searchItem = String.valueOf(searchbar.getText());
        String url ="https://rickandmortyapi.com/api/character/" + searchVal;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject responseObject = new JSONObject(response);
                            responsePieceCharName = responseObject.getString("name");
                            characterNameTV.setText("You are: " + responsePieceCharName);
                            responsePieceCharacterImage = responseObject.getString("image");
                            Picasso.get().load(responsePieceCharacterImage).into(posterImageView);

                            addToFirebase();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                characterNameTV.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);


    }

    public void addToFirebase() {
        String userName = String.valueOf(searchbar.getText());
        squad = database.getReference().child("squad members");
        dataUserName = squad.child(userName);
        dataCharacterName = dataUserName.child("name");
        dataCharacterImage = dataUserName.child("image");
        dataCharacterName.push().setValue(responsePieceCharName);
        dataCharacterImage.push().setValue(responsePieceCharacterImage);


    }


    public int calculateSearchValue(){
        int[] cumulatives = new int[]{0,31,60,91,121,152,182,213,244,274,305,335};

        int searchNum = Integer.parseInt(monthSelection.getSelectedItem().toString());
        int searchNum2 = Integer.parseInt(daySelection.getSelectedItem().toString());

        return cumulatives[searchNum-1] + searchNum2;

    }


}
