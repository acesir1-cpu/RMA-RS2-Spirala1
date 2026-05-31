package ba.etf.weatherwatch.data
import ba.etf.weatherwatch.model.Drzava

object DrzavaStaticData {
    private val drzave = listOf(
        Drzava("Bosna i Hercegovina", "BA"),
        Drzava("Srbija", "RS"),
        Drzava("Hrvatska", "HR"),
        Drzava("Slovenija", "SI"),
        Drzava("Crna Gora", "ME"),
        Drzava("Sjeverna Makedonija", "MK")
    )
    fun getAll(): List<Drzava> = drzave
}