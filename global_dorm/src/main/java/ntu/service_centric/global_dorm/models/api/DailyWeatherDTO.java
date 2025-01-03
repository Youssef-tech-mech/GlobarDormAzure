package ntu.service_centric.global_dorm.models.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DailyWeatherDTO {

    @JsonProperty("dt")
    private long timestamp;

    @JsonProperty("temp")
    private Temp temp;

    @JsonProperty("weather")
    private Weather[] weather;

    @JsonProperty("wind_speed")
    private double windSpeed;

    @JsonProperty("humidity")
    private int humidity;

    // Inner class for temperature details
    public static class Temp {
        @JsonProperty("day")
        private double day;

        @JsonProperty("min")
        private double min;

        @JsonProperty("max")
        private double max;

        public double getDay() {
            return day;
        }

        public void setDay(double day) {
            this.day = day;
        }

        public double getMin() {
            return min;
        }

        public void setMin(double min) {
            this.min = min;
        }

        public double getMax() {
            return max;
        }

        public void setMax(double max) {
            this.max = max;
        }
    }

    public static class Weather {
        @JsonProperty("description")
        private String description;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    // Getters and setters
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Temp getTemp() {
        return temp;
    }

    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}
