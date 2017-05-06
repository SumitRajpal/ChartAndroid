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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog pd;

    ArrayList<BarDataSet> yAxis;
    ArrayList<BarEntry> yValues;
    ArrayList<String> xAxis1;
    BarEntry values ;
    BarChart chart;


    BarData data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("loading");


       // Log.d("array",Arrays.toString(fullData));
        chart = (BarChart) findViewById(R.id.chart);
        load_data_from_server();

    }

    public void load_data_from_server() {
        pd.show();
        String url = "http://vga.ramstertech.com/freebieslearning/chart.php";
        xAxis1 = new ArrayList<>();
        yAxis = null;
        yValues = new ArrayList<>();


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


                                String score = jsonobject.getString("score").trim();
                                String name = jsonobject.getString("name").trim();

                                xAxis1.add(name);

                                values = new BarEntry(Float.valueOf(score),i);
                                yValues.add(values);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();


                        }





                        BarDataSet barDataSet1 = new BarDataSet(yValues, "Goals LaLiga 16/17");
                        barDataSet1.setColor(Color.rgb(0, 82, 159));



                        yAxis = new ArrayList<>();
                        yAxis.add(barDataSet1);
                        String names[]= xAxis1.toArray(new String[xAxis1.size()]);
                        data = new BarData(names,yAxis);
                        chart.setData(data);
                        chart.setDescription("");
                        chart.animateXY(2000, 2000);
                        chart.invalidate();
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
