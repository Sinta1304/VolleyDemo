package com.example.volleydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //Initialize variable
    ListView listView;
    ArrayList<MainData> dataArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign variable
        listView = findViewById(R.id.list_view);

        //Url
        String url = "https://picsum.photos/v2/list";

        //Initialize progress dialog
        final ProgressDialog dialog = new ProgressDialog(this);
        //Set message
        dialog.setMessage("Please Wait ...");
        //Set non cancelable
        dialog.setCancelable(true);
        //Show progress dialog
        dialog.show();

        //Initialize string request
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Check condition
                if (response != null){
                    //When response is not null
                    //Dismiss progress dialog
                    dialog.dismiss();
                    try {
                        //Initialize response json array
                        JSONArray jsonArray = new JSONArray(response);
                        //Parse array
                        parseArray(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Display toast
                Toast.makeText(getApplicationContext(),error.toString()
                        ,Toast.LENGTH_SHORT).show();
            }
        });

        //Initialize request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        //Add request
        queue.add(request);
    }

    private void parseArray(JSONArray jsonArray) {
        //Use for Loop
        for (int i=0; i<jsonArray.length(); i++){
            try {
                //Initialize json object
                JSONObject object = jsonArray.getJSONObject(i);
                //Initialize main data
                MainData data = new MainData();
                //Set name
                data.setName(object.getString("author"));
                //Set image
                data.setImage(object.getString("download_url"));
                //Add data in array list
                dataArrayList.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Set adapter
            listView.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return dataArrayList.size();
                }

                @Override
                public Object getItem(int position) {
                    return null;
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    //Initialize view
                    View view = getLayoutInflater().inflate(
                            R.layout.item_main,null
                    );
                    //Initialize main data
                    MainData data = dataArrayList.get(position);

                    //Initialize and assign variable
                    ImageView imageView = view.findViewById(R.id.image_view);
                    TextView textView = view.findViewById(R.id.text_view);

                    //Set image on image view
                    Glide.with(getApplicationContext())
                            .load(data.getImage())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView);
                    //Set name on text view
                    textView.setText(data.getName());

                    //Return view
                    return view;
                }
            });
        }
    }
}