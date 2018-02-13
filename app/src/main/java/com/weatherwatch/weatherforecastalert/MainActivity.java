package com.weatherwatch.weatherforecastalert;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
/*
*	Author: Torrey Sorensen
 *	Tools used: Volley  https://github.com/google/volley
*
*/
public class MainActivity extends AppCompatActivity {

	//Default url api variables
	final String yahooApiCoreUrl = "https://query.yahooapis.com/v1/public/yql?q=";
	final String chicagoTestQuery = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20%28select%20woeid%20from%20geo.places%281%29%20where%20text%3D%22Chicago%22%29&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
	int notificationID = 001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// The text views for each day of the forecast.
		final TextView txtHeader = (TextView)findViewById(R.id.txtHeader);
		final TextView txtDay0 = (TextView)findViewById(R.id.txtDay0);
		final TextView txtDay1 = (TextView)findViewById(R.id.txtDay1);
		final TextView txtDay2 = (TextView)findViewById(R.id.txtDay2);
		final TextView txtDay3 = (TextView)findViewById(R.id.txtDay3);
		final TextView txtDay4 = (TextView)findViewById(R.id.txtDay4);
		final TextView txtDay5 = (TextView)findViewById(R.id.txtDay5);
		final TextView txtDay6 = (TextView)findViewById(R.id.txtDay6);
		final TextView txtDay7 = (TextView)findViewById(R.id.txtDay7);
		final TextView txtDay8 = (TextView)findViewById(R.id.txtDay8);
		final TextView txtDay9 = (TextView)findViewById(R.id.txtDay9);

		// init the Volley request queue and begin the request.
		RequestQueue queue = Volley.newRequestQueue(this);
		JsonObjectRequest forecastRequest = new JsonObjectRequest(Request.Method.GET, chicagoTestQuery, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					//Parse out the json objects to get the forecast array.
					JSONObject query = response.getJSONObject("query");
					JSONObject results = query.getJSONObject("results");
					JSONObject channel = results.getJSONObject("channel");
					JSONObject item = channel.getJSONObject("item");

					//Set the title to the header
					txtHeader.setText(item.getString("title"));

					//Break down the forecast into a week array.
					JSONArray forecastWeek = item.getJSONArray("forecast");

					//Looping over the forecast and activating alerts when needed.
					for(int i = 0; i < forecastWeek.length(); i++){
						JSONObject forecastDay = forecastWeek.getJSONObject(i);
						checkAlerts(forecastDay);
					}
					//Setting the forecast items to the screen for visual aid.
					JSONObject forecastDay0 = forecastWeek.getJSONObject(0);
					txtDay0.setText("Date: "+forecastDay0.getString("date") + ", High: "+forecastDay0.getString("high")+", Low: "+forecastDay0.getString("low") + ", Condition: " + forecastDay0.getString("text"));

					JSONObject forecastDay1 = forecastWeek.getJSONObject(1);
					txtDay1.setText("\nDate: "+forecastDay1.getString("date") + ", High: "+forecastDay1.getString("high")+", Low: "+forecastDay1.getString("low") + ", Condition: " + forecastDay1.getString("text"));

					JSONObject forecastDay2 = forecastWeek.getJSONObject(2);
					txtDay2.setText("\nDate: "+forecastDay2.getString("date") + ", High: "+forecastDay2.getString("high")+", Low: "+forecastDay2.getString("low") + ", Condition: " + forecastDay2.getString("text"));

					JSONObject forecastDay3 = forecastWeek.getJSONObject(3);
					txtDay3.setText("\nDate: "+forecastDay3.getString("date") + ", High: "+forecastDay3.getString("high")+", Low: "+forecastDay3.getString("low") + ", Condition: " + forecastDay3.getString("text"));

					JSONObject forecastDay4 = forecastWeek.getJSONObject(4);
					txtDay4.setText("\nDate: "+forecastDay4.getString("date") + ", High: "+forecastDay4.getString("high")+", Low: "+forecastDay4.getString("low") + ", Condition: " + forecastDay4.getString("text"));

					JSONObject forecastDay5 = forecastWeek.getJSONObject(5);
					txtDay5.setText("\nDate: "+forecastDay5.getString("date") + ", High: "+forecastDay5.getString("high")+", Low: "+forecastDay5.getString("low") + ", Condition: " + forecastDay5.getString("text"));

					JSONObject forecastDay6 = forecastWeek.getJSONObject(6);
					txtDay6.setText("\nDate: "+forecastDay6.getString("date") + ", High: "+forecastDay6.getString("high")+", Low: "+forecastDay6.getString("low") + ", Condition: " + forecastDay6.getString("text"));

					JSONObject forecastDay7 = forecastWeek.getJSONObject(7);
					txtDay7.setText("\nDate: "+forecastDay7.getString("date") + ", High: "+forecastDay7.getString("high")+", Low: "+forecastDay7.getString("low") + ", Condition: " + forecastDay7.getString("text"));

					JSONObject forecastDay8 = forecastWeek.getJSONObject(8);
					txtDay8.setText("\nDate: "+forecastDay8.getString("date") + ", High: "+forecastDay8.getString("high")+", Low: "+forecastDay8.getString("low") + ", Condition: " + forecastDay8.getString("text"));

					JSONObject forecastDay9 = forecastWeek.getJSONObject(9);
					txtDay9.setText("\nDate: "+forecastDay9.getString("date") + ", High: "+forecastDay9.getString("high")+", Low: "+forecastDay9.getString("low") + ", Condition: " + forecastDay9.getString("text"));


				}catch(JSONException e){
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
			}
		});
		queue.add(forecastRequest);

	}

	// Check each alert condition.
	public void checkAlerts(JSONObject object){
		String advisory = "";
		try {
			if (object.getString("text").contains("Rain")) {
				advisory += "Rain advisory for " + object.getString("day") +", "+object.getString("date") + ". \n";
			}
			if(object.getString("text").contains("Thunderstorms")){
				advisory += "Thunderstorm advisory for " + object.getString("day") +", "+object.getString("date") + ". \n";
			}
			if(object.getString("text").contains("Snow")){
				advisory += "Snow advisory for " + object.getString("day") +", "+object.getString("date") + ". \n";
			}
			if(object.getString("text").contains("Ice")){
				advisory += "Ice advisory for " + object.getString("day") +", "+object.getString("date") + ". \n";
			}
			if(Integer.parseInt(object.getString("high")) > 85 ){
				advisory += "Heat advisory for " + object.getString("day") +", "+object.getString("date") + ". \n";
			}
			if(Integer.parseInt(object.getString("low")) < 32){
				advisory += "Freezing advisory for " + object.getString("day") +", "+object.getString("date") + ". \n";
			}
		}catch (JSONException e){
			e.printStackTrace();
		}

		if(advisory.length() > 1){
			notifyAlert(advisory, notificationID);
			notificationID += 1;
		}
	}

	// Set notification to the top.
	public void notifyAlert(String alert, int notificationID){
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MainActivity.this).setSmallIcon(R.drawable.ic_launcher_background).setContentTitle("Weather Alert").setContentText(alert);
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(notificationID, notificationBuilder.build());
	}
}
