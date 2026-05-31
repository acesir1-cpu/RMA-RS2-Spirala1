package ba.etf.weatherwatch.data
import ba.etf.weatherwatch.model.Grad

object GradStaticData {

    private val gradovi = listOf(
        // Bosna i Hercegovina
        Grad("Sarajevo",  "Bosna i Hercegovina", 43.85, 18.39),
        Grad("Mostar",    "Bosna i Hercegovina", 43.34, 17.81),
        Grad("Banja Luka","Bosna i Hercegovina", 44.77, 17.19),
        Grad("Tuzla",     "Bosna i Hercegovina", 44.54, 18.68),
        Grad("Zenica",    "Bosna i Hercegovina", 44.20, 17.91),
        // Srbija
        Grad("Beograd",   "Srbija", 44.79, 20.45),
        Grad("Novi Sad",  "Srbija", 45.26, 19.83),
        Grad("Niš",       "Srbija", 43.32, 21.90),
        // Hrvatska
        Grad("Zagreb",    "Hrvatska", 45.81, 15.98),
        Grad("Split",     "Hrvatska", 43.51, 16.44),
        Grad("Rijeka",    "Hrvatska", 45.33, 14.44),
        // Slovenija
        Grad("Ljubljana", "Slovenija", 46.06, 14.51),
        Grad("Maribor",   "Slovenija", 46.55, 15.65),
        Grad("Celje",     "Slovenija", 46.23, 15.27),
        // Crna Gora
        Grad("Podgorica", "Crna Gora", 42.44, 19.26),
        Grad("Nikšić",    "Crna Gora", 42.77, 18.95),
        Grad("Budva",     "Crna Gora", 42.29, 18.84),
        // Sjeverna Makedonija
        Grad("Skoplje",   "Sjeverna Makedonija", 41.99, 21.43),
        Grad("Bitola",    "Sjeverna Makedonija", 41.03, 21.34),
        Grad("Ohrid",     "Sjeverna Makedonija", 41.12, 20.80)
    )   // 20 gradova ukupno

    fun getGradoviIzDrzave(nazivDrzave: String): List<Grad> =
        gradovi.filter { it.nazivDrzave == nazivDrzave }

    fun getGradoviZaDodavanje(nazivDrzave: String): List<Grad> {
        val vecDodani = WeatherStaticData.getLokacijeKorisnika().map { it.naziv }
        return getGradoviIzDrzave(nazivDrzave).filter { it.naziv !in vecDodani }
    }
}