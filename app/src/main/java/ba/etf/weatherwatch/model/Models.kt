package ba.etf.weatherwatch.model

data class Lokacija(
    val naziv: String,
    val drzava: String,
    val latitude: Double,
    val longitude: Double,
    val tipPrikaza: String, // "Po satu", "Po danu", "Sedmično"
    val korisnikUpisan: Boolean = false
)
data class Prognoza(
    val nazivLokacije: String,
    val temperatura: Float,
    val osjecajTemperature: Float,
    val opisVremena: String,
    val brzinaVjetra: Float,
    val smjerVjetra: String,
    val uvIndeks: Float,
    val padavine: Float?, // null znači nema padavina
    val vlaznost: Int,
    val pritisak: Int, // u hPa
    val vidljivost: Int, // u km
    val oblacnost: Int, // u %
    val minTemp: Float,
    val maxTemp: Float,
    val vrijemeTipa: String, // "sunny","cloudy","rainy","snowy","stormy",
    val prognozaPoSatima: List<SatnaPrognoza>,
    val prognozaDani: List<DnevnaPrognoza>
)
data class SatnaPrognoza(
    val sat: String, // format "14:00"
    val temperatura: Float,
    val vrijemeTipa: String,
    val padavinePostotak: Int // vjerovatnoća padavina 0-100
)
data class DnevnaPrognoza(
    val dan: String,
    // "Pon","Uto","Sri","Cet","Pet","Sub","Ned"
    val minTemp: Float,
    val maxTemp: Float,
    val vrijemeTipa: String,
    val padavinePostotak: Int
)

data class AppPostavke(
    val tema: String = "auto", // "light", "dark", "auto"
    val jezik: String = "bs", // "bs", "en"
    val jedinice: String = "celsius", // "celsius", "fahrenheit"
    val notifikacije: Boolean = true,
    val notifikacijeOluja: Boolean = true
)

data class Drzava(val naziv: String, val kod: String)
// primjer: Drzava("Bosna i Hercegovina", "BA")
data class Grad(val naziv: String, val nazivDrzave: String, val lat: Double, val lon:Double)
// primjer: Grad("Sarajevo", "Bosna i Hercegovina", 43.85, 18.39)