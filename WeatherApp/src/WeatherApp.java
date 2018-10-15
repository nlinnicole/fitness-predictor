import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;

public class WeatherApp {

    public static void main(String[] args)
            throws APIException {
    	System.out.println("Compiled and runned");
        // declaring object of "OWM" class
        OWM owm = new OWM("0eb401a45beb278ea46f2016fb20d3be");
        // it was in kelvin by default, change to metric.
        owm.setUnit(OWM.Unit.METRIC);
        // getting current weather data for the "London" city
        CurrentWeather cwd = owm.currentWeatherByCityName("Montreal,ca");     

        //printing city name from the retrieved data
        System.out.println("City: " + cwd.getCityName());

        // printing the max./min. temperature
        System.out.println("Max/Min: " + (cwd.getMainData().getTempMax())
                            + "/" + (cwd.getMainData().getTempMin()) + "\'C");     
        System.out.println("Current Temperature: " + cwd.getMainData().getTemp() + "\'C");
        System.out.println("Wind: " + cwd.getWindData());
    }
}