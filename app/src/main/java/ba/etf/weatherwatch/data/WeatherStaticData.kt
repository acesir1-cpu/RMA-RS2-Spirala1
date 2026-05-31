package ba.etf.weatherwatch.data
import ba.etf.weatherwatch.model.*

object WeatherStaticData {

    private val sveLokacije: MutableList<Lokacija> = mutableListOf(
        Lokacija("Sarajevo",  "Bosna i Hercegovina", 43.85, 18.39, "Po danu",   true),
        Lokacija("Mostar",    "Bosna i Hercegovina", 43.34, 17.81, "Po satu",   true),
        Lokacija("Banja Luka","Bosna i Hercegovina", 44.77, 17.19, "Sedmično",  true),
        Lokacija("Tuzla",     "Bosna i Hercegovina", 44.54, 18.68, "Po satu"),
        Lokacija("Zenica",    "Bosna i Hercegovina", 44.20, 17.91, "Po danu"),
        Lokacija("Beograd",   "Srbija",              44.79, 20.45, "Po danu"),
        Lokacija("Zagreb",    "Hrvatska",            45.81, 15.98, "Sedmično"),
        Lokacija("Ljubljana", "Slovenija",           46.06, 14.51, "Sedmično"),
        Lokacija("Podgorica", "Crna Gora",           42.44, 19.26, "Po danu"),
        Lokacija("Skoplje",   "Sjeverna Makedonija", 41.99, 21.43, "Po satu")
    )

    private fun satne(baza: Float, tip: String): List<SatnaPrognoza> =
        (9..18).map { h ->                       // sati 09:00 do 18:00 = 10 stavki
            SatnaPrognoza(
                sat = "%02d:00".format(h),
                temperatura = baza + (h - 9),
                vrijemeTipa = tip,
                padavinePostotak = (h * 3) % 100
            )
        }

    private fun dnevne(min: Float, max: Float, tip: String): List<DnevnaPrognoza> {
        val dani = listOf("Pon","Uto","Sri","Čet","Pet","Sub","Ned")   // 7 stavki
        return dani.mapIndexed { i, d ->
            DnevnaPrognoza(
                dan = d,
                minTemp = min + i,
                maxTemp = max + i,
                vrijemeTipa = tip,
                padavinePostotak = (i * 12) % 100
            )
        }
    }

    private val prognoze: MutableMap<String, Prognoza> = mutableMapOf(
        "Sarajevo" to Prognoza("Sarajevo", 8f, 5f, "Oblačno", 12f, "SZ", 3f,
            null, 70, 1015, 12, 80, 3f, 11f, "cloudy",
            satne(8f, "cloudy"), dnevne(3f, 11f, "cloudy")),

        "Mostar" to Prognoza("Mostar", 32f, 36f, "Sunčano i vruće", 8f, "J", 9f,
            null, 40, 1018, 25, 5, 24f, 35f, "sunny",
            satne(28f, "sunny"), dnevne(24f, 35f, "sunny")),

        "Banja Luka" to Prognoza("Banja Luka", 18f, 18f, "Djelimično oblačno", 10f, "Z", 4f,
            null, 55, 1012, 18, 40, 12f, 20f, "partly_cloudy",
            satne(16f, "partly_cloudy"), dnevne(12f, 20f, "partly_cloudy")),

        "Tuzla" to Prognoza("Tuzla", -3f, -6f, "Snijeg", 15f, "S", 1f,
            2.5f, 85, 1008, 6, 95, -5f, 0f, "snowy",
            satne(-4f, "snowy"), dnevne(-5f, 0f, "snowy")),

        "Zenica" to Prognoza("Zenica", 14f, 13f, "Magla", 5f, "SI", 2f,
            null, 90, 1013, 3, 70, 8f, 16f, "foggy",
            satne(12f, "foggy"), dnevne(8f, 16f, "foggy")),

        "Beograd" to Prognoza("Beograd", 24f, 24f, "Sunčano", 9f, "JZ", 6f,
            null, 45, 1016, 20, 10, 16f, 26f, "sunny",
            satne(22f, "sunny"), dnevne(16f, 26f, "sunny")),

        "Zagreb" to Prognoza("Zagreb", 19f, 19f, "Kiša", 55f, "Z", 3f,
            6f, 80, 1005, 8, 90, 14f, 21f, "rainy",
            satne(17f, "rainy"), dnevne(14f, 21f, "rainy")),

        "Ljubljana" to Prognoza("Ljubljana", 10f, 9f, "Magla", 4f, "S", 2f,
            null, 88, 1011, 6, 60, 6f, 12f, "foggy",
            satne(9f, "foggy"), dnevne(6f, 12f, "foggy")),

        "Podgorica" to Prognoza("Podgorica", 28f, 29f, "Vedro", 7f, "J", 8f,
            null, 35, 1017, 30, 5, 18f, 30f, "sunny",
            satne(25f, "sunny"), dnevne(18f, 30f, "sunny")),

        "Skoplje" to Prognoza("Skoplje", 21f, 21f, "Oluja", 60f, "I", 5f,
            8f, 75, 1003, 10, 85, 15f, 23f, "stormy",
            satne(19f, "stormy"), dnevne(15f, 23f, "stormy"))
    )

    fun getLokacijeKorisnika(): List<Lokacija> =
        sveLokacije.filter { it.korisnikUpisan }.sortedBy { it.naziv }

    fun getSveLokacije(): List<Lokacija> =
        sveLokacije.sortedBy { it.naziv }

    fun getLokacijePoStatusu(status: String): List<Lokacija> =
        getLokacijeKorisnika().filter { getStatus(it.naziv) == status }

    fun getPrognozu(nazivLokacije: String): Prognoza? =
        prognoze[nazivLokacije]

    fun getStatus(nazivLokacije: String): String {
        val p = prognoze[nazivLokacije] ?: return "Vedro"   // nema prognoze → Vedro
        return when {
            p.padavine != null && p.brzinaVjetra > 50  -> "Oluja"
            p.padavine != null                         -> "Padavine"
            p.temperatura > 30 || p.uvIndeks > 7       -> "Vruce"
            p.temperatura in 20f..30f            -> "Toplo"
            p.temperatura < 0                          -> "Mraz"
            else                                       -> "Vedro"
        }
    }

    fun dodajLokaciju(lokacija: Lokacija) {
        val idx = sveLokacije.indexOfFirst {
            it.naziv == lokacija.naziv && it.drzava == lokacija.drzava
        }
        if (idx >= 0) {
            // postoji → ažuriraj postavljanjem korisnikUpisan = true
            sveLokacije[idx] = sveLokacije[idx].copy(korisnikUpisan = true)
        } else {
            // ne postoji → dodaj novu s korisnikUpisan = true
            sveLokacije.add(lokacija.copy(korisnikUpisan = true))
        }
        // osiguraj da svaki dodani grad ima prognozu (inače bi ekran ostao prazan)
        if (prognoze[lokacija.naziv] == null) {
            prognoze[lokacija.naziv] = generisiPrognozu(lokacija)
        }
    }

    // generiše neutralnu prognozu za grad koji nije u predefinisanoj mapi
    private fun generisiPrognozu(l: Lokacija): Prognoza {
        val baza = 18f
        val tip = "partly_cloudy"
        return Prognoza(
            nazivLokacije = l.naziv,
            temperatura = baza,
            osjecajTemperature = baza,
            opisVremena = "Djelimično oblačno",
            brzinaVjetra = 10f,
            smjerVjetra = "Z",
            uvIndeks = 4f,
            padavine = null,
            vlaznost = 55,
            pritisak = 1013,
            vidljivost = 15,
            oblacnost = 40,
            minTemp = baza - 5f,
            maxTemp = baza + 5f,
            vrijemeTipa = tip,
            prognozaPoSatima = satne(baza - 2f, tip),
            prognozaDani = dnevne(baza - 5f, baza + 5f, tip)
        )
    }
}

