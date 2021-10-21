package project8;

import java.io.IOException;

public interface WeatherModel {
    void getWeather(String selectedCity, Variant period) throws IOException;

    void getSavedWeather();
}