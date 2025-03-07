import weather.Period;
import weather.Root;
import weather.WeatherAPI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class MyWeatherAPI extends WeatherAPI {

    public static ArrayList<Period> getForecast() {

        String latitude = "";
        String longitude = "";

        try {
            String apiUrl = "http://ip-api.com/csv/";
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");

            // Read the API response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();  // the response from the server is a csv
            reader.close();

            String[] data = response.split(",");

            if (Objects.equals(data[0], "fail")){
                System.err.println("your IP address couldn't found because : IP is " + data[1]);
            }else{
                latitude = data[7];
                longitude = data[8];

            }
        } catch (Exception e) {
            System.err.println("Failed to retrieve location data: " + e.getMessage());
        }

        String url_to_get_the_url = "https://api.weather.gov/points/"+latitude+","+longitude;

        String located_url = ""; //

        HttpRequest request_url = HttpRequest.newBuilder()
                .uri(URI.create(url_to_get_the_url))
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request_url, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.body());
            located_url = rootNode.path("properties").path("forecastGridData").asText();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed get the url for current location ");
        }



        System.out.println(located_url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(located_url+"/forecast"))
                //.method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Root r = getObject(response.body());
        if(r == null){
            System.err.println("Failed to parse JSon while getting the weather data ");
            return null;
        }
        return r.properties.periods;
    }
}