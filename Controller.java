package project8;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Controller {
    private WeatherModel weatherModel = new AccuweatherModel();

    private Map<Integer, Variant> variantResults = new HashMap<>();

    public Controller() {
        variantResults.put(1, Variant.NOW);
        variantResults.put(0, Variant.FIVE_DAYS);
        variantResults.put(2, Variant.SAVED);
    }

    public void getWeather(String commandInput, String selectedCity) throws IOException {
        Integer integerCommand = Integer.parseInt(commandInput);


        switch (variantResults.get(integerCommand)) {
            case NOW:
                weatherModel.getWeather(selectedCity, Variant.NOW);
                break;
            case FIVE_DAYS:
                weatherModel.getWeather(selectedCity, Variant.FIVE_DAYS);
                break;
            case SAVED:
                weatherModel.getSavedWeather();
        }
    }
}