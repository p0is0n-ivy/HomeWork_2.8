package project8;
import com.fasterxml.jackson.databind.ObjectMapper;
import project8.entity.Weather;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class AccuweatherModel implements WeatherModel {
    private static final String PROTOCOL = "http";
    private static final String BASE_HOST = "dataservice.accuweather.com";
    private static final String CURRENT_CONDITIONS = "currentconditions";
    private static final String VERSION = "v1";
    private static final String LOCATIONS = "locations";
    private static final String CITIES = "cities";
    private static final String AUTOCOMPLETE = "autocomplete";
    private static final String FORECASTS = "forecasts";
    private static final String DAILY = "daily";
    private static final String PERIOD = "5day";

    private static final String API_KEY = "51dgsMw00aRATCckcv1nWilvgBV86M62";//"pXJd8MokcZCdrd2MsoGl2DBZAyCa0zvv";
    static OkHttpClient okHttpClient = new OkHttpClient();
    static ObjectMapper objectMapper = new ObjectMapper();
    DataBaseRepository dataBaseRepository = new DataBaseRepository();

    public void getWeather(String selectedCity, Variant period) throws IOException {
        if (period == Variant.NOW) {
            HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme(PROTOCOL)
                    .host(BASE_HOST)
                    .addPathSegment(CURRENT_CONDITIONS)
                    .addPathSegment(VERSION)
                    .addPathSegment(getCityKey(selectedCity))
                    .addQueryParameter("apikey", API_KEY)
                    .build();

            Request request = new Request.Builder()
                    .url(httpUrl)
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            String responseString = response.body().string();
            String weatherText = objectMapper.readTree(responseString).get(0).at("/WeatherText").asText();
            Integer degrees = objectMapper.readTree(responseString).get(0).at("/Temperature/Metric/Value").asInt();
            Weather weather = new Weather(selectedCity, weatherText, degrees);
            System.out.println(weather);
            dataBaseRepository.saveWeather(weather);
            //TODO: сделать красивый вывод в консоль
        }

        if (period == Variant.FIVE_DAYS) {
            // TODO*: реализовать вывод прогноза погоды на 5 дней
            HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme("http")
                    .host(BASE_HOST)
                    .addPathSegment(FORECASTS)
                    .addPathSegment(VERSION)
                    .addPathSegment(DAILY)
                    .addPathSegment(PERIOD)
                    .addPathSegment(getCityKey(selectedCity))
                    .addQueryParameter("apikey", API_KEY)
                    .addQueryParameter("language", "ru-ru")
                    .build();

            Request request = new Request.Builder()
                    .url(httpUrl)
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            String responseString = response.body().string();

            for (int i = 0; i <= 4; i++){
                String date = objectMapper.readTree(responseString).at("/DailyForecasts").get(i).at("/Date").asText();
                String weatherText = objectMapper.readTree(responseString).at("/DailyForecasts").get(i).at("/Day/IconPhrase").asText();
                Integer temperature = objectMapper.readTree(responseString).at("/DailyForecasts").get(i).at("/Temperature/Minimum/Value").asInt();
                System.out.println("В городе " + selectedCity + " " + "на дату " + date + " ожидается " + weatherText + ", температура " + temperature);
            }
        }
    }

    public void getSavedWeather() {
        dataBaseRepository.getSavedWeather();
    }

    public String getCityKey(String city) throws IOException {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme(PROTOCOL)
                .host(BASE_HOST)
                .addPathSegments(LOCATIONS)
                .addPathSegments(VERSION)
                .addPathSegments(CITIES)
                .addPathSegments(AUTOCOMPLETE)
                .addQueryParameter("apikey", API_KEY)
                .addQueryParameter("q", city)
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        String responseBody = response.body().string();

        String cityKey = objectMapper.readTree(responseBody).get(0).at("/Key").asText();
        return cityKey;
    }
}