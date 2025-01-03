package ntu.service_centric.global_dorm.models.api;

public class WeatherInfo {
    private String cityName;
    private Main main;
    private Wind wind;
    private Weather[] weather;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String value) {
        this.cityName = value;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main value) {
        this.main = value;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind value) {
        this.wind = value;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] value) {
        this.weather = value;
    }


    public class Main {
        private double currentTemperature;
        private double maxTemperature;
        private double minTemperature;
        private long humidity;

        public double getCurrentTemperature() {
            return currentTemperature;
        }

        public void setCurrentTemperature(double value) {
            this.currentTemperature = value;
        }

        public double getMaxTemperature() {
            return maxTemperature;
        }

        public void setMaxTemperature(double value) {
            this.maxTemperature = value;
        }

        public double getMinTemperature() {
            return minTemperature;
        }

        public void setMinTemperature(double value) {
            this.minTemperature = value;
        }

        public long getHumidity() {
            return humidity;
        }

        public void setHumidity(long value) {
            this.humidity = value;
        }

    }

    public class Weather {
        private String description;

        public String getDescription() {
            return description;
        }

        public void setDescription(String value) {
            this.description = value;
        }
    }


// Wind.java


        public class Wind {
            private double speed;

            public double getSpeed() {
                return speed;
            }

            public void setSpeed(double value) {
                this.speed = value;
            }
    }
}
