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
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class MyWeatherAPI extends WeatherAPI {

    public static ArrayList<Period> getForecast(List<String> list_loc ) {

        list_loc.clear();

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
                list_loc.add( data[5]);
                list_loc.add( data[3]);
                latitude = data[7];
                longitude = data[8];

                System.out.println("\n\n\n" + longitude + latitude + "\n\n\n");
            }
        } catch (Exception e) {
            System.err.println("Failed to retrieve location data: " + e.getMessage());
        }

        String url_to_get_the_url = "https://api.weather.gov/points/"+latitude+","+longitude;
        System.out.println("\n\n\n" + url_to_get_the_url + "\n\n\n");

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








    public static ArrayList<Period> getForecast(List<String> list_loc, String cityName , List<String> res) {
        String latitude = "";
        String longitude = "";
        cityName = cityName.replace(" ", "%20");

        try {
            // Use Nominatim API to get latitude & longitude for a city
            String apiUrl = "https://nominatim.openstreetmap.org/search?city=" + cityName.toLowerCase() + "&format=json";
            cityName = cityName.replace("%20"," ");
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            System.out.println("\n\n\n\n");
            while ((line = reader.readLine()) != null) {
                response.append(line);
                System.out.println(line);
            }
            reader.close();

            // Parse JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.toString());

            if (rootNode.isArray() && rootNode.size() > 0) {
                JsonNode location = rootNode.get(0);
                latitude = location.get("lat").asText();
                longitude = location.get("lon").asText();
                list_loc.clear();
                list_loc.add(location.get("display_name").asText());
                latitude = latitude.substring(0, latitude.length() - 3);
                longitude = longitude.substring(0, longitude.length() - 3);
                if (latitude.endsWith("0")) {
                    latitude = latitude.substring(0, latitude.length() - 1) + "1";
                }
                if (longitude.endsWith("0")) {
                    longitude = longitude.substring(0, longitude.length() - 1) + "1";
                }


                res.add("Weather for: " + cityName);
                System.err.println(latitude +"\n"+ longitude);
            } else {

                System.err.println("City not found: " + cityName);
                res.add("OH pls : " + cityName);
                return getForecast(list_loc);
            }
        } catch (Exception e) {
            System.err.println("Failed to retrieve coordinates for city " + cityName + ": " + e.getMessage());
            //res.add("Failed to retrieve coordinates for city " + cityName + ": " + e.getMessage());
            res.clear();
            res.add("pls try again ");
            return getForecast(list_loc);
        }

        String url_to_get_the_url = "https://api.weather.gov/points/"+latitude+","+longitude;
        System.out.println("\n\n\n" + url_to_get_the_url + "\n\n\n");
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
            res.clear();
            res.add("pls try again ");
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
            res.clear();
            res.add("pls try again ");
            e.printStackTrace();
        }
        Root r = getObject(response.body());
        if(r == null){
            System.err.println("Failed to parse JSon while getting the weather data ");
            res.clear();
            res.add("pls try again ");
            return null;
        }

        return r.properties.periods;
    }

}