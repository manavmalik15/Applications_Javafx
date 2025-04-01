import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import weather.Period;
import weather.WeatherAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JavaFX extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("I'm a professional Weather App!");
		List<String> list_loc = new ArrayList<>();




		final ArrayList<Period>[] forecast = new ArrayList[]{MyWeatherAPI.getForecast(list_loc)};
		final String[] city = {list_loc.get(0)};
		final String[] state = {list_loc.get(1)};
		if (forecast[0] == null) {
			throw new RuntimeException("Forecast did not load");
		}



		TextField locati = new TextField( city[0] + "," +state[0]);
		locati.setStyle("-fx-background-color: rgba(0, 0, 0, 0); -fx-border-color: transparent; -fx-alignment: center;");
		locati.setFont(new Font(20));

		TextField unit = new TextField( " °F");
		unit.setStyle("-fx-background-color: rgba(0, 0, 0, 0); -fx-border-color: transparent; ");
		unit.setFont(new Font("Black 900 italic",30));

		TextField temperature = new TextField( forecast[0].get(0).temperature +"");
		temperature.setStyle("-fx-background-color: rgba(0, 0, 0, 0); -fx-border-color: transparent; ");
		temperature.setMaxWidth(250);
		temperature.setFont(new Font("Black 900 italic",60));

		ImageView som = new ImageView(new Image(getWeatherIconUrl(forecast[0].get(0))));

		HBox maindetail = new HBox(som, temperature,unit);
		maindetail.setStyle("-fx-alignment: center; -fx-spacing: 0px; -fx-padding: 0px 0px 0px 180px;");
		TextField weather = new TextField(forecast[0].get(0).shortForecast);
		weather.setStyle("-fx-background-color: rgba(0, 0, 0, 0); -fx-border-color: transparent; -fx-alignment: center;");
		weather.setFont(new Font(20));



		Button rainButton = new Button("Check Rain Chance " );
		Button tempButton = new Button("Check Temperature");
		tempButton.setStyle("-fx-alignment: center;");
		Button forecast_up = new Button("forecast for upcoming days");
		forecast_up.setStyle("-fx-alignment: center;");
		tempButton.setStyle("-fx-alignment: center;");
		Button windButton = new Button("Check Wind Conditions");
		Button backButton  = new Button("Back");
		Button backButton2 = new Button("Back");
		Button backButton3 = new Button("Back");
		Button backButton4 = new Button("Back");
		Button clear = new Button("Clear");

		String st = "-fx-background-color: rgba(0, 0, 0, 0); -fx-border-color: transparent; -fx-alignment: center; ";

		TextField rain =new TextField( forecast[0].get(0).probabilityOfPrecipitation.value + "%");
		TextField wind =new TextField( forecast[0].get(0).windSpeed + " , " + forecast[0].get(0).windDirection );

		rain.setMaxWidth(300);

		rain.setFont(new Font(30));
		wind.setFont(new Font(30));

		rain.setStyle(st);
		wind.setStyle(st);

		rain.setMaxWidth(150);
		wind.setMaxWidth(250);

		VBox rainnnn = new VBox(rain , rainButton);
		rainnnn.setStyle(st);
		VBox windddd = new VBox(wind , windButton);
		windddd.setStyle(st);

		VBox tempe = new VBox(tempButton , forecast_up);
		tempe.setStyle(st+"-fx-padding: 30px 0px 0px 30px;");

		HBox Det = new HBox(30,rainnnn , tempe , windddd);
		Det.setStyle(st+"-fx-padding: 45px 0px 0px 30px;");

		BackgroundImage back_for_pages = getBackgroundImage("back_for_pages.png");
		BackgroundImage background = getBackgroundImage("projectbackground.jpeg");


		String box_s = "-fx-alignment: center; -fx-spacing: 85px; -fx-padding: 45px 70px 70px 70px;";







// rain
		ImageView precipitationIcon = new ImageView(new Image("prep_image.png"));
		precipitationIcon.setFitWidth(300);
		precipitationIcon.setFitHeight(80);
		HBox precipitationHeader = new HBox(30, backButton2, precipitationIcon);
		precipitationHeader.setMaxWidth(375);

// Rain boxes for next 3 days
		TextField t1 = new TextField("TODAY            ");
		t1.setStyle("-fx-background-color: rgba(0, 0, 0, 0); -fx-border-color: transparent; -fx-alignment: center;");
		t1.setFont(new Font(16));
		t1.setEditable(false);
		TextField t2 = new TextField("TOMORROW         ");
		t2.setStyle("-fx-background-color: rgba(0, 0, 0, 0); -fx-border-color: transparent; -fx-alignment: center;");
		t2.setFont(new Font(16));
		t2.setEditable(false);
		TextField t3 = new TextField("DAY AFTER TOMORROW");
		t3.setPrefWidth(200);
		t3.setStyle("-fx-background-color: rgba(0, 0, 0, 0); -fx-border-color: transparent; -fx-alignment: center;");
		t3.setFont(new Font(16));
		t3.setEditable(false);

// Default style for precipitation fields
		String rainFieldStyle = "-fx-background-color: rgba(0, 0, 0, 0); -fx-font-size: 18px; -fx-alignment: center;";

// Precipitation probability for each day
		TextField p1 = new TextField(String.valueOf(forecast[0].get(0).probabilityOfPrecipitation.value) + "%");
		TextField p2 = new TextField(String.valueOf(forecast[0].get(2).probabilityOfPrecipitation.value) + "%");
		TextField p3 = new TextField(String.valueOf(forecast[0].get(4).probabilityOfPrecipitation.value) + "%");

		p1.setStyle(rainFieldStyle);
		p2.setStyle(rainFieldStyle);
		p3.setStyle(rainFieldStyle);

		p1.setEditable(false);
		p2.setEditable(false);
		p3.setEditable(false);

		VBox rainBox1 = new VBox(5, p1);
		VBox rainBox2 = new VBox(5, p2);
		VBox rainBox3 = new VBox(5, p3);

// Aligning the rain day with the probability in the HBox structure
		HBox tBox1 = new HBox(10, new ImageView(new Image(getWeatherIconUrl(forecast[0].get(0)))), t1, rainBox1);
		HBox tBox2 = new HBox(10, new ImageView(new Image(getWeatherIconUrl(forecast[0].get(2)))), t2, rainBox2);
		HBox tBox3 = new HBox(10, new ImageView(new Image(getWeatherIconUrl(forecast[0].get(4)))), t3, rainBox3);

// Container for the rain page
		VBox rainContainer = new VBox(precipitationHeader, tBox1, tBox2, tBox3);
		rainContainer.setStyle(box_s);

		BorderPane rainLayout = new BorderPane();
		rainLayout.setLeft(backButton2);
		rainLayout.setCenter(rainContainer);
		rainLayout.setBackground(new Background(back_for_pages));







		// Temperature



		ImageView temperatureIcon = new ImageView(new Image("temp_header.png"));
		temperatureIcon.setFitWidth(300);
		temperatureIcon.setFitHeight(80);
		HBox temperatureHeader = new HBox(30, backButton3, temperatureIcon);
		temperatureHeader.setMaxWidth(375);

// Temperature boxes for next 3 days
		TextField day1 = new TextField("TODAY             ");
		day1.setStyle("-fx-background-color: rgba(0, 0, 0, 0); -fx-border-color: transparent; -fx-alignment: center;");
		day1.setFont(new Font(16));
		day1.setEditable(false);

		TextField day2 = new TextField("TOMORROW          ");
		day2.setStyle("-fx-background-color: rgba(0, 0, 0, 0); -fx-border-color: transparent; -fx-alignment: center;");
		day2.setFont(new Font(16));
		day2.setEditable(false);

		TextField day3 = new TextField("DAY AFTER TOMORROW");
		day3.setPrefWidth(200);
		day3.setStyle("-fx-background-color: rgba(0, 0, 0, 0); -fx-border-color: transparent; -fx-alignment: center;");
		day3.setFont(new Font(16));
		day3.setEditable(false);

// Default style for temperature fields
		String tempFieldStyle = "-fx-background-color: rgba(0, 0, 0, 0); -fx-font-size: 18px; -fx-alignment: center;";






// High and low temperatures for each day
		List<String> highLowTemps = getHighLow(forecast[0]);
		TextField highTemp1 = new TextField("High: " + highLowTemps.get(0) + "°F");
		TextField lowTemp1 = new TextField("Low: " + highLowTemps.get(1) + "°F");
		TextField highTemp2 = new TextField("High: " + highLowTemps.get(2) + "°F");
		TextField lowTemp2 = new TextField("Low: " + highLowTemps.get(3) + "°F");
		TextField highTemp3 = new TextField("High: " + highLowTemps.get(4) + "°F");

		System.out.println(" got till here 1\n");
		System.out.println(highLowTemps);


		TextField lowTemp3 = new TextField("Low: " + highLowTemps.get(5) + "°F");



		highTemp1.setStyle(tempFieldStyle);
		lowTemp1.setStyle(tempFieldStyle);
		highTemp2.setStyle(tempFieldStyle);
		lowTemp2.setStyle(tempFieldStyle);
		highTemp3.setStyle(tempFieldStyle);
		lowTemp3.setStyle(tempFieldStyle);

		highTemp1.setEditable(false);
		lowTemp1.setEditable(false);
		highTemp2.setEditable(false);
		lowTemp2.setEditable(false);
		highTemp3.setEditable(false);
		lowTemp3.setEditable(false);

		VBox tempBox1 = new VBox(5, highTemp1, lowTemp1);
		VBox tempBox2 = new VBox(5, highTemp2, lowTemp2);
		VBox tempBox3 = new VBox(5, highTemp3, lowTemp3);

// Add weather icons and the corresponding temperature data in HBoxes
		HBox tempDay1 = new HBox(10, new ImageView(new Image(getWeatherIconUrl(forecast[0].get(0)))), day1, tempBox1);
		HBox tempDay2 = new HBox(10, new ImageView(new Image(getWeatherIconUrl(forecast[0].get(2)))), day2, tempBox2);
		HBox tempDay3 = new HBox(10, new ImageView(new Image(getWeatherIconUrl(forecast[0].get(4)))), day3, tempBox3);

// Create the container for the temperature data
		VBox tempContainer = new VBox(15, temperatureHeader, tempDay1, tempDay2, tempDay3);
		tempContainer.setStyle(box_s);

// Set up the layout for the temperature page
		BorderPane tempLayout = new BorderPane();
		tempLayout.setCenter(tempContainer);
		tempLayout.setBackground(new Background(back_for_pages));







		System.out.println(" got till here 2\n");



// upcoming ddays



		ImageView dayIcon = new ImageView(new Image("upcoming.png"));
		dayIcon.setFitWidth(300);
		dayIcon.setFitHeight(80);
		HBox dayeratureHeader = new HBox(30, backButton4, dayIcon);
		dayeratureHeader.setMaxWidth(375);


		String r_for_days11 = String.valueOf(forecast[0].get(0).probabilityOfPrecipitation.value);
		String r_for_days12 = String.valueOf(forecast[0].get(1).probabilityOfPrecipitation.value);
		String r_for_days21 = String.valueOf(forecast[0].get(2).probabilityOfPrecipitation.value);
		String r_for_days22 = String.valueOf(forecast[0].get(3).probabilityOfPrecipitation.value);
		String r_for_days31 = String.valueOf(forecast[0].get(4).probabilityOfPrecipitation.value);
		String r_for_days32 = String.valueOf(forecast[0].get(5).probabilityOfPrecipitation.value);


		String sty = "-fx-background-color: rgba(0, 0, 0, 0); -fx-border-color: transparent; -fx-alignment: center;";

// Temperature boxes for next 3 days
		TextField day11 = new TextField("  TODAY " );
		TextField day11day = new TextField("    day   " );
		TextField day11night = new TextField("    night   " );

		TextField day22 = new TextField("  TOMORROW " );
		TextField day22day = new TextField("    day   " );
		TextField day22night = new TextField("    night   " );

		TextField day33 = new TextField("  DAY AFTER " );
		TextField day33day = new TextField("    day   " );
		TextField day33night = new TextField("    night   " );


		TextField day11temp = new TextField("  TEMPERATURE " );
		TextField day11daytemp = new TextField(highLowTemps.get(0) + " - " + highLowTemps.get(1) + "°F"  );
		TextField day11nighttemp = new TextField(highLowTemps.get(0) + " - " + highLowTemps.get(1) + "°F"  );

		TextField day22temp = new TextField("  TEMPERATURE " );
		TextField day22daytemp = new TextField(highLowTemps.get(2) + " - " + highLowTemps.get(3) + "°F"  );
		TextField day22nighttemp = new TextField(highLowTemps.get(2) + " - " + highLowTemps.get(3) + "°F" );

		TextField day33temp = new TextField("  TEMPERATURE " );
		TextField day33daytemp = new TextField(highLowTemps.get(4) + " - " + highLowTemps.get(5) + "°F"  );
		TextField day33nighttemp = new TextField(highLowTemps.get(4) + " - " + highLowTemps.get(5) + "°F"  );

		TextField day11rain = new TextField("  PREPARATION " );
		TextField day11dayrain = new TextField( r_for_days11 +"%");
		TextField day11nightrain = new TextField(r_for_days12 +"%" );

		TextField day22rain = new TextField("  PREPARATION " );
		TextField day22dayrain = new TextField(r_for_days21 +"%");
		TextField day22nightrain = new TextField(r_for_days22 +"%");

		TextField day33rain = new TextField("  PREPARATION " );
		TextField day33dayrain = new TextField(r_for_days31 +"%" );
		TextField day33nightrain = new TextField( r_for_days32 +"%");

		day11.setStyle(sty);
		day11day.setStyle(sty);
		day11night.setStyle(sty);
		day22.setStyle(sty);
		day22day.setStyle(sty);
		day22night.setStyle(sty);
		day33.setStyle(sty);
		day33day.setStyle(sty);
		day33night.setStyle(sty);

		day11temp.setStyle(sty);
		day11daytemp.setStyle(sty);
		day11nighttemp.setStyle(sty);
		day22temp.setStyle(sty);
		day22daytemp.setStyle(sty);
		day22nighttemp.setStyle(sty);
		day33temp.setStyle(sty);
		day33daytemp.setStyle(sty);
		day33nighttemp.setStyle(sty);

		day11rain.setStyle(sty);
		day11dayrain.setStyle(sty);
		day11nightrain.setStyle(sty);
		day22rain.setStyle(sty);
		day22dayrain.setStyle(sty);
		day22nightrain.setStyle(sty);
		day33rain.setStyle(sty);
		day33dayrain.setStyle(sty);
		day33nightrain.setStyle(sty);





		VBox dayBox11 = new VBox(day11,day11day,day11night);
		VBox dayBox22 = new VBox(day22,day22day,day22night);
		VBox dayBox33 = new VBox(day33,day33day,day33night);

		VBox dayBox11temp = new VBox(day11temp,day11daytemp,day11nighttemp);
		VBox dayBox22temp = new VBox(day22temp,day22daytemp,day22nighttemp);
		VBox dayBox33temp = new VBox(day33temp,day33daytemp,day33nighttemp);

		VBox dayBox11rain = new VBox(day11rain,day11dayrain,day11nightrain);
		VBox dayBox22rain = new VBox(day22rain,day22dayrain,day22nightrain);
		VBox dayBox33rain = new VBox(day33rain,day33dayrain,day33nightrain);


// Add weather icons and the corresponding temperature data in HBoxes
		HBox dayDay11 = new HBox(10, new ImageView(new Image(getWeatherIconUrl(forecast[0].get(0)))), dayBox11,dayBox11temp,dayBox11rain);
		HBox dayDay22 = new HBox(10, new ImageView(new Image(getWeatherIconUrl(forecast[0].get(2)))), dayBox22,dayBox22temp,dayBox22rain);
		HBox dayDay33 = new HBox(10, new ImageView(new Image(getWeatherIconUrl(forecast[0].get(4)))), dayBox33,dayBox33temp,dayBox33rain);

		dayDay11.setMaxWidth(2000);
		dayDay22.setMaxWidth(2000);
		dayDay33.setMaxWidth(2000);


// Create the container for the dayerature data
		VBox dayContainer = new VBox( dayeratureHeader ,dayDay11, dayDay22, dayDay33);
		dayContainer.setStyle("-fx-alignment: center; -fx-spacing: 80px; -fx-padding: 55px 70px 70px 70px;");

// Set up the layout for the dayerature page
		BorderPane dayLayout = new BorderPane();
		dayLayout.setCenter(dayContainer);
		dayLayout.setBackground(new Background(back_for_pages));











		// Wind Page Implementation
		ImageView windIcon = new ImageView(new Image("wind_image.jpeg"));
		windIcon.setFitWidth(300);
		windIcon.setFitHeight(80);
		HBox windHeader = new HBox(30,backButton,windIcon);

		windHeader.setMaxWidth(375);

		// Wind boxes for next 3 days
		TextField windDay1 = new TextField("TODAY            .");
		windDay1.setStyle("-fx-background-color: rgba(0, 0, 0, 0); -fx-border-color: transparent; -fx-alignment: center;");
		windDay1.setFont(new Font( 16));
		windDay1.setEditable(false);
		TextField windDay2 = new TextField("TOMORROW         .");
		windDay2.setStyle("-fx-background-color: rgba(0, 0, 0, 0); -fx-border-color: transparent; -fx-alignment: center;");
		windDay2.setFont(new Font(  16));
		windDay2.setEditable(false);
		TextField windDay3 = new TextField("DAY AFTER TOMORROW");
		windDay3.setPrefWidth(200);
		windDay3.setStyle("-fx-background-color: rgba(0, 0, 0, 0); -fx-border-color: transparent; -fx-alignment: center;");
		windDay3.setFont(new Font(  16));
		windDay3.setEditable(false);

		// Default style for wind fields
		String windFieldStyle = "-fx-background-color: rgba(0, 0, 0, 0); -fx-font-size: 18px; -fx-alignment: center;";

		// Wind speed and direction for each day
		TextField windSpeed1 = new TextField("Speed: " + forecast[0].get(0).windSpeed);
		TextField windDirection1 = new TextField("Direction: " + forecast[0].get(0).windDirection);
		TextField windSpeed2 = new TextField("Speed: " + forecast[0].get(2).windSpeed);
		TextField windDirection2 = new TextField("Direction: " + forecast[0].get(2).windDirection);
		TextField windSpeed3 = new TextField("Speed: " + forecast[0].get(4).windSpeed);
		TextField windDirection3 = new TextField("Direction: " + forecast[0].get(4).windDirection);

		windSpeed1.setStyle(windFieldStyle);
		windDirection1.setStyle(windFieldStyle);
		windSpeed2.setStyle(windFieldStyle);
		windDirection2.setStyle(windFieldStyle);
		windSpeed3.setStyle(windFieldStyle);
		windDirection3.setStyle(windFieldStyle);

		windSpeed1.setEditable(false);
		windDirection1.setEditable(false);
		windSpeed2.setEditable(false);
		windDirection2.setEditable(false);
		windSpeed3.setEditable(false);
		windDirection3.setEditable(false);

		VBox windBox1 = new VBox(5, windSpeed1, windDirection1);
		VBox windBox2 = new VBox(5, windSpeed2, windDirection2);
		VBox windBox3 = new VBox(5, windSpeed3, windDirection3);

		HBox windDayBox1 = new HBox(10, new ImageView(new Image(getWeatherIconUrl(forecast[0].get(0)))), windDay1, windBox1);
		HBox windDayBox2 = new HBox(10, new ImageView(new Image(getWeatherIconUrl(forecast[0].get(2)))), windDay2, windBox2);
		HBox windDayBox3 = new HBox(10, new ImageView(new Image(getWeatherIconUrl(forecast[0].get(4)))), windDay3, windBox3);

		VBox windContainer = new VBox(windHeader, windDayBox1, windDayBox2, windDayBox3);
		windContainer.setStyle(box_s);

		BorderPane windLayout = new BorderPane();
 		windLayout.setCenter(windContainer);
		windLayout.setBackground(new Background(back_for_pages));








// serch

		Button get_W_serch = new Button("Search ->");
		Button get_W_myloc = new Button("Your Location");
		TextField serch_box = new TextField();
		serch_box.setPromptText("Enter a city within America");

		HBox serch = new HBox(10, get_W_myloc,get_W_serch, serch_box ,clear);
		serch.setStyle("-fx-background-color: rgba(0, 0, 0, 0); -fx-border-color: transparent; -fx-alignment: center; -fx-padding: 10px 10px 70px 10px;");


		get_W_serch.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String locat = serch_box.getText();
				List<String> er_message = new ArrayList<>();
				try {
					forecast[0] = MyWeatherAPI.getForecast(list_loc, locat, er_message);

					//som = new ImageView(new Image(getWeatherIconUrl(forecast[0].get(0))));

					city[0] = list_loc.get(0);
					//state[0] = list_loc.get(1);

					locati.setText(city[0] + "," + state[0]);

					serch_box.setText(er_message.get(0));
				}catch (Exception exception){
					serch_box.setText("somthing went wrong");
					forecast[0] = MyWeatherAPI.getForecast(list_loc);
				}

				List<String> highLowTemps = getHighLow(forecast[0]);




				 day11daytemp.setText(highLowTemps.get(0) + " - " + highLowTemps.get(1) + "°F"  );
				 day11nighttemp.setText(highLowTemps.get(0) + " - " + highLowTemps.get(1) + "°F"  );

				day22daytemp.setText(highLowTemps.get(2) + " - " + highLowTemps.get(3) + "°F"  );
				day22nighttemp.setText(highLowTemps.get(2) + " - " + highLowTemps.get(3) + "°F" );

				day33daytemp.setText(highLowTemps.get(4) + " - " + highLowTemps.get(5) + "°F"  );
				day33nighttemp.setText(highLowTemps.get(4) + " - " + highLowTemps.get(5) + "°F"  );

				day11dayrain.setText( String.valueOf(forecast[0].get(0).probabilityOfPrecipitation.value) +"%");
				day11nightrain.setText(String.valueOf(forecast[0].get(1).probabilityOfPrecipitation.value) +"%" );

				day22dayrain.setText(String.valueOf(forecast[0].get(2).probabilityOfPrecipitation.value) +"%");
				day22nightrain.setText(String.valueOf(forecast[0].get(3).probabilityOfPrecipitation.value) +"%");

				day33dayrain.setText(String.valueOf(forecast[0].get(4).probabilityOfPrecipitation.value) +"%" );
				day33nightrain.setText( String.valueOf(forecast[0].get(5).probabilityOfPrecipitation.value) +"%");










				// Update text fields with new data
				temperature.setText(forecast[0].get(0).temperature + "");
				weather.setText(forecast[0].get(0).shortForecast);
				rainButton.setText("Check Rain Chance " + forecast[0].get(0).probabilityOfPrecipitation.value + "%");

				// Update precipitation values
				p1.setText(forecast[0].get(0).probabilityOfPrecipitation.value + "%");
				p2.setText(forecast[0].get(2).probabilityOfPrecipitation.value + "%");
				p3.setText(forecast[0].get(4).probabilityOfPrecipitation.value + "%");

				// Update high and low temperatures

				highTemp1.setText("High: " + highLowTemps.get(0) + "°F");
				lowTemp1.setText("Low: " + highLowTemps.get(1) + "°F");
				highTemp2.setText("High: " + highLowTemps.get(2) + "°F");
				lowTemp2.setText("Low: " + highLowTemps.get(3) + "°F");
				highTemp3.setText("High: " + highLowTemps.get(4) + "°F");
				lowTemp3.setText("Low: " + highLowTemps.get(5) + "°F");

				// updated wind
				windSpeed1.setText("Speed: " + forecast[0].get(0).windSpeed);
				windDirection1.setText("Direction: " + forecast[0].get(0).windDirection);
				windSpeed2.setText("Speed: " + forecast[0].get(2).windSpeed);
				windDirection2.setText("Direction: " + forecast[0].get(2).windDirection);
				windSpeed3.setText("Speed: " + forecast[0].get(4).windSpeed);
				windDirection3.setText("Direction: " + forecast[0].get(4).windDirection);

				rain.setText( forecast[0].get(0).probabilityOfPrecipitation.value + "%");
				wind.setText( forecast[0].get(0).windSpeed + " , " + forecast[0].get(0).windDirection );

				// Update weather icons
				((ImageView) rainBox1.getChildren().get(0)).setImage(new Image(getWeatherIconUrl(forecast[0].get(0))));
				((ImageView) rainBox2.getChildren().get(0)).setImage(new Image(getWeatherIconUrl(forecast[0].get(2))));
				((ImageView) rainBox3.getChildren().get(0)).setImage(new Image(getWeatherIconUrl(forecast[0].get(4))));

				((ImageView) tempDay1.getChildren().get(0)).setImage(new Image(getWeatherIconUrl(forecast[0].get(0))));
				((ImageView) tempDay2.getChildren().get(0)).setImage(new Image(getWeatherIconUrl(forecast[0].get(2))));
				((ImageView) tempDay3.getChildren().get(0)).setImage(new Image(getWeatherIconUrl(forecast[0].get(4))));

			}
		});

		get_W_myloc.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				forecast[0].clear();
				list_loc.clear();
				forecast[0] = MyWeatherAPI.getForecast(list_loc);
				city[0] = list_loc.get(0);
				state[0] = list_loc.get(1);
				serch_box.setText(city[0]);

				locati.setText(city[0] + "," +state[0]);

				// Update text fields with new data
				temperature.setText( forecast[0].get(0).temperature+"");
				weather.setText(forecast[0].get(0).shortForecast);
				rainButton.setText("Check Rain Chance " + forecast[0].get(0).probabilityOfPrecipitation.value + "%");


				List<String> highLowTemps = getHighLow(forecast[0]);




				day11daytemp.setText(highLowTemps.get(0) + " - " + highLowTemps.get(1) + "°F"  );
				day11nighttemp.setText(highLowTemps.get(0) + " - " + highLowTemps.get(1) + "°F"  );

				day22daytemp.setText(highLowTemps.get(2) + " - " + highLowTemps.get(3) + "°F"  );
				day22nighttemp.setText(highLowTemps.get(2) + " - " + highLowTemps.get(3) + "°F" );

				day33daytemp.setText(highLowTemps.get(4) + " - " + highLowTemps.get(5) + "°F"  );
				day33nighttemp.setText(highLowTemps.get(4) + " - " + highLowTemps.get(5) + "°F"  );

				day11dayrain.setText( String.valueOf(forecast[0].get(0).probabilityOfPrecipitation.value) +"%");
				day11nightrain.setText(String.valueOf(forecast[0].get(1).probabilityOfPrecipitation.value) +"%" );

				day22dayrain.setText(String.valueOf(forecast[0].get(2).probabilityOfPrecipitation.value) +"%");
				day22nightrain.setText(String.valueOf(forecast[0].get(3).probabilityOfPrecipitation.value) +"%");

				day33dayrain.setText(String.valueOf(forecast[0].get(4).probabilityOfPrecipitation.value) +"%" );
				day33nightrain.setText( String.valueOf(forecast[0].get(5).probabilityOfPrecipitation.value) +"%");

				// Update precipitation values
				p1.setText(forecast[0].get(0).probabilityOfPrecipitation.value + "%");
				p2.setText(forecast[0].get(2).probabilityOfPrecipitation.value + "%");
				p3.setText(forecast[0].get(4).probabilityOfPrecipitation.value + "%");

				// Update high and low temperatures
				highTemp1.setText("High: " + highLowTemps.get(0) + "°F");
				lowTemp1.setText("Low: " + highLowTemps.get(1) + "°F");
				highTemp2.setText("High: " + highLowTemps.get(2) + "°F");
				lowTemp2.setText("Low: " + highLowTemps.get(3) + "°F");
				highTemp3.setText("High: " + highLowTemps.get(4) + "°F");
				lowTemp3.setText("Low: " + highLowTemps.get(5) + "°F");

				// updated wind
				windSpeed1.setText("Speed: " + forecast[0].get(0).windSpeed);
				windDirection1.setText("Direction: " + forecast[0].get(0).windDirection);
				windSpeed2.setText("Speed: " + forecast[0].get(2).windSpeed);
				windDirection2.setText("Direction: " + forecast[0].get(2).windDirection);
				windSpeed3.setText("Speed: " + forecast[0].get(4).windSpeed);
				windDirection3.setText("Direction: " + forecast[0].get(4).windDirection);

				rain.setText( forecast[0].get(0).probabilityOfPrecipitation.value + "%");
				wind.setText( forecast[0].get(0).windSpeed + " , " + forecast[0].get(0).windDirection );

				// Update weather icons
				((ImageView) rainBox1.getChildren().get(0)).setImage(new Image(getWeatherIconUrl(forecast[0].get(0))));
				((ImageView) rainBox2.getChildren().get(0)).setImage(new Image(getWeatherIconUrl(forecast[0].get(2))));
				((ImageView) rainBox3.getChildren().get(0)).setImage(new Image(getWeatherIconUrl(forecast[0].get(4))));

				((ImageView) tempDay1.getChildren().get(0)).setImage(new Image(getWeatherIconUrl(forecast[0].get(0))));
				((ImageView) tempDay2.getChildren().get(0)).setImage(new Image(getWeatherIconUrl(forecast[0].get(2))));
				((ImageView) tempDay3.getChildren().get(0)).setImage(new Image(getWeatherIconUrl(forecast[0].get(4))));

			}
		});












		//Main Scene Layout
		VBox mainContainer = new VBox(10,serch,locati, maindetail, weather, Det);
		mainContainer.setStyle("-fx-alignment: center; ");
		BorderPane mainLayout = new BorderPane();
		mainLayout.setCenter(mainContainer);
		mainLayout.setBackground(new Background(background));

		// Create scenes in the correct order
		Scene mainScene = new Scene(mainLayout, 700, 700);
		Scene rainScene = new Scene(rainLayout, 700, 700);
		Scene tempScene = new Scene(tempLayout, 700, 700);
		Scene windScene = new Scene(windLayout, 700, 700);
		Scene dayScene = new Scene(dayLayout, 700, 700);

		// Set button actions after all scenes are created
		rainButton.setOnAction(e -> primaryStage.setScene(rainScene));
		tempButton.setOnAction(e -> primaryStage.setScene(tempScene));
		windButton.setOnAction(e -> primaryStage.setScene(windScene));
		forecast_up.setOnAction(e -> primaryStage.setScene(dayScene));
		backButton.setOnAction(e -> primaryStage.setScene(mainScene));
		backButton2.setOnAction(e -> primaryStage.setScene(mainScene));
		backButton3.setOnAction(e -> primaryStage.setScene(mainScene));
		backButton4.setOnAction(e -> primaryStage.setScene(mainScene));
		clear.setOnAction(e -> serch_box.clear());

		primaryStage.setResizable(false);
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}















	private BackgroundImage getBackgroundImage(String imageUrl) {
		Image bgImage = new Image(imageUrl, 700, 700, false, true);
		return new BackgroundImage(
				bgImage,
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER,
				new BackgroundSize(700, 700, false, false, false, false)
		);
	}

	public static String getWeatherIconUrl(Period period) {
		String baseUrl = "https://openweathermap.org/img/wn/";
		String iconCode = "01d"; // Default to clear sky day icon
		String weatherCondition = period.shortForecast.toLowerCase();
		boolean isDaytime = period.isDaytime;

		if (weatherCondition.contains("cloudy")) {
			iconCode = isDaytime ? "02d" : "02n";
		}
		if (weatherCondition.contains("clouds")) {
			iconCode = isDaytime ? "04d" : "04n";
		}
		if (weatherCondition.contains(" rain")) {
			iconCode = isDaytime ? "09d" : "09n";
		}
		if (weatherCondition.contains("fog")) {
			iconCode = isDaytime ? "50d" : "50n";
		}
		if (weatherCondition.contains("sunny")) {
			iconCode = isDaytime ? "01d" : "01n";
		}
		if (weatherCondition.contains("rain")) {
			iconCode = isDaytime ? "10d" : "10n";
		}
		if (weatherCondition.contains("thunderstorms")) {
			iconCode = isDaytime ? "11d" : "11n";
		}
		if (weatherCondition.contains("snow")) {
			iconCode = isDaytime ? "13d" : "13n";
		}
//		return baseUrl + iconCode + "@2x.png";
		return period.icon;
	}

	private List<String> getHighLow(ArrayList<Period> forecast) {
		List<String> highlow = new ArrayList<>();
		Pattern pattern1 = Pattern.compile("high near (\\d+)[.,\\s]?");
		Pattern pattern2 = Pattern.compile("low around (\\d+)[.,\\s]?");
		int j = 0;
		if (forecast.get(0).name.equals("Tonight")) {
			highlow.add(String.valueOf(forecast.get(0).temperature));
			Matcher matcher = pattern2.matcher(forecast.get(0).detailedForecast);
			if (matcher.find()) {
				highlow.add(matcher.group(1)); // Ensure find() succeeds before calling group(1)
			}
			j = 1;
		}
		for (int i = j; i < 14; i++) {
			Matcher matcher = pattern1.matcher(forecast.get(i).detailedForecast);
			if (matcher.find()) {
				highlow.add(matcher.group(1));
			} else {
				matcher = pattern2.matcher(forecast.get(i).detailedForecast);
				if (matcher.find()) {
					highlow.add(matcher.group(1)); // Ensure find() succeeds before calling group(1)
				}else{
					highlow.add("0");
				}
			}
		}
		return highlow;
	}
}