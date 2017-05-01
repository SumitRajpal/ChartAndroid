package com.example.sumit.chartandroid;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Radar extends AppCompatActivity {
    ArrayList<Entry> entries;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar);
        pd = new ProgressDialog(Radar.this);
        pd.setMessage("loading");

        entries = new ArrayList<>();
        load_data_from_server();
    }
    public void load_data_from_server() {
        pd.show();
        String url = "http://192.168.1.14/freebieslearning/radar.php";



        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("string",response);

                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);


                                String target = jsonobject.getString("target").trim();
                                String passing = jsonobject.getString("passing").trim();
                                String skills = jsonobject.getString("skills").trim();
                                String dribbling = jsonobject.getString("dribbling").trim();
                                String penalty = jsonobject.getString("penalty").trim();
                                entries.add(new Entry(Float.valueOf(target), 0));
                                entries.add(new Entry(Float.valueOf(passing), 1));
                                entries.add(new Entry(Float.valueOf(skills), 2));
                                entries.add(new Entry(Float.valueOf(dribbling), 3));
                                entries.add(new Entry(Float.valueOf(penalty), 4));




                            }
                        } catch (JSONException e) {
                            e.printStackTrace();


                        }

                        RadarChart chart = (RadarChart) findViewById(R.id.chartr);







                        RadarDataSet dataset_comp1 = new RadarDataSet(entries, "Lionel Messi");



                        dataset_comp1.setColor(Color.BLUE);
                        dataset_comp1.setDrawFilled(true);




                        ArrayList<RadarDataSet> dataSets = new ArrayList<RadarDataSet>();
                        dataSets.add(dataset_comp1);


                        ArrayList<String> labels = new ArrayList<String>();
                        labels.add("Target");
                        labels.add("Passing");
                        labels.add("Skills");
                        labels.add("Dribbling");
                        labels.add("Penalty");





                        RadarData data = new RadarData(labels, dataSets);
                        chart.setData(data);
                       String description = "Showing Lionel Messi's Skill Analysis (scale of 1-5)";
                        chart.setDescription(description);
                       //chart.setWebLineWidthInner(0.5f);




                        chart.invalidate();
                        chart.animate();
                        pd.hide();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){

                            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                            pd.hide();
                        }
                    }
                }

        );

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }
}
