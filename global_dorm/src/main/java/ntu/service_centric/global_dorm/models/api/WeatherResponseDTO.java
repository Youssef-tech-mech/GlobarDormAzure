package ntu.service_centric.global_dorm.models.api;

import com.google.gson.annotations.SerializedName;

public class WeatherResponseDTO {

    @SerializedName("name")
    private String cityName;

    @SerializedName("main")
    private Main main;

    @SerializedName("wind")
    private Wind wind;

    @SerializedName("weather")
    private Weather[] weather;

    public static class Main {
        @SerializedName("temp")
        private double currentTemperature;

        @SerializedName("temp_max")
        private double maxTemperature;

        @SerializedName("temp_min")
        private double minTemperature;

        @SerializedName("humidity")
        private int humidity;

        // Getters and setters
        public double getCurrentTemperature() {
            return currentTemperature;
        }

        public void setCurrentTemperature(double currentTemperature) {
            this.currentTemperature = currentTemperature;
        }

        public double getMaxTemperature() {
            return maxTemperature;
        }

        public void setMaxTemperature(double maxTemperature) {
            this.maxTemperature = maxTemperature;
        }

        public double getMinTemperature() {
            return minTemperature;
        }

        public void setMinTemperature(double minTemperature) {
            this.minTemperature = minTemperature;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }
    }

    public static class Wind {
        @SerializedName("speed")
        private double speed;

        // Getters and setters
        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }
    }

    public static class Weather {
        @SerializedName("description")
        private String description;

        // Getters and setters
        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    // Getters and setters for the root object
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }
}
