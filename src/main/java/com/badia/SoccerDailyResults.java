package com.badia;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

public class SoccerDailyResults {

    private final HttpClient httpClient= HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .build();

    public static void main(String[] args) throws Exception {
        SoccerDailyResults obj = new SoccerDailyResults();
        System.out.println("Testing - Send HTTP GET");
        obj.getResults();
    }

    public void getResults() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String date = simpleDateFormat.format(new Date());

        URI uri = URI.create("https://api.sportradar.us/soccer-t3/eu/en/schedules/"+date+"/schedule.json?api_key=swzg3xucqhkgnt69wzffcqct");
        
        System.out.println(uri.toASCIIString());

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        HttpResponse response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject schedule = new JSONObject(response.body().toString());

        // create array object of today's games
        JSONArray games = schedule.getJSONArray("sport_events");
        

        // loop through and get team and time of the game
        for(int i =0; i < games.length(); i++){
            String time = games.getJSONObject(i).getString("scheduled");
            JSONArray teams = games.getJSONObject(i).getJSONArray("competitors");
            String teamOne = teams.getJSONObject(0).getString("name");
            String teamTwo = teams.getJSONObject(1).getString("name");
           
            String match = teamOne + " VS " + teamTwo;

            //todo: remove block of code to a method
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(time);
            Date dt = Date.from(Instant.from(offsetDateTime));
            SimpleDateFormat timeInHours = new SimpleDateFormat("hh:mm:ss a");

            System.out.println(match + " playing at: " + timeInHours.format(dt));
        }

    }

    
    
}
