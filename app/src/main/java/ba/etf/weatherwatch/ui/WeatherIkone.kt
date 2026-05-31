package ba.etf.weatherwatch.ui
import ba.etf.weatherwatch.R

object WeatherIkone {
    fun zaTip(tip: String): Int = when (tip) {
        "sunny"         -> R.drawable.ic_weather_sunny
        "partly_cloudy" -> R.drawable.ic_weather_partly_cloudy
        "cloudy"        -> R.drawable.ic_weather_cloudy
        "rainy"         -> R.drawable.ic_weather_rainy
        "snowy"         -> R.drawable.ic_weather_snowy
        "stormy"        -> R.drawable.ic_weather_stormy
        "foggy"         -> R.drawable.ic_weather_foggy
        else            -> R.drawable.ic_weather_cloudy
    }
    fun pozadinaZaTip(tip: String): Int = when (tip) {
        "sunny"         -> R.drawable.bg_sunny
        "partly_cloudy" -> R.drawable.bg_partly_cloudy
        "cloudy"        -> R.drawable.bg_cloudy
        "rainy"         -> R.drawable.bg_rainy
        "snowy"         -> R.drawable.bg_snowy
        "stormy"        -> R.drawable.bg_stormy
        "foggy"         -> R.drawable.bg_foggy
        else            -> R.drawable.bg_cloudy
    }
}