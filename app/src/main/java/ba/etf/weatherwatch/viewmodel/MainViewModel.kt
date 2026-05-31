package ba.etf.weatherwatch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ba.etf.weatherwatch.data.DrzavaStaticData
import ba.etf.weatherwatch.data.GradStaticData
import ba.etf.weatherwatch.data.WeatherStaticData
import ba.etf.weatherwatch.model.Drzava
import ba.etf.weatherwatch.model.Grad
import ba.etf.weatherwatch.model.Lokacija

class MainViewModel : ViewModel() {
    val filterOpcije = listOf("Sve moje lokacije", "Sve lokacije", "Vedro", "Padavine", "Ekstremne temperature")
    val tipOpcije = listOf("Po satu", "Po danu", "Sedmično")
    val sveDrzave: List<Drzava> = DrzavaStaticData.getAll()

    private val _filterovaneLokacije = MutableLiveData<List<Lokacija>>()
    val filterovaneLokacije: LiveData<List<Lokacija>> = _filterovaneLokacije

    private val _gradoviZaDrzavu = MutableLiveData<List<Grad>>(emptyList())
    val gradoviZaDrzavu: LiveData<List<Grad>> = _gradoviZaDrzavu

    private val _dugmeEnabled = MutableLiveData(false)
    val dugmeEnabled: LiveData<Boolean> = _dugmeEnabled

    private val _odabranaDrzava = MutableLiveData<Drzava?>(null)
    val odabranaDrzava: LiveData<Drzava?> = _odabranaDrzava

    private val _odabraniGrad = MutableLiveData<Grad?>(null)
    val odabraniGrad: LiveData<Grad?> = _odabraniGrad

    private val _odabraniTip = MutableLiveData<String?>(null)
    val odabraniTip: LiveData<String?> = _odabraniTip

    // čuvamo i trenutni filter da ga proslijedimo PrognozaActivity
    private val _odabraniFilter = MutableLiveData("Sve moje lokacije")
    val odabraniFilter: LiveData<String> = _odabraniFilter

    init {
        postaviFilter("Sve moje lokacije")
    }

    fun postaviFilter(filter: String) {
        _odabraniFilter.value = filter
        _filterovaneLokacije.value = when (filter) {
            "Sve moje lokacije" -> WeatherStaticData.getLokacijeKorisnika()
            "Sve lokacije"      -> WeatherStaticData.getSveLokacije()
            "Vedro"             -> WeatherStaticData.getLokacijeKorisnika()
                .filter { val s = WeatherStaticData.getStatus(it.naziv); s == "Vedro" || s == "Toplo" }
            "Padavine"          -> WeatherStaticData.getLokacijeKorisnika()
                .filter { val s = WeatherStaticData.getStatus(it.naziv); s == "Padavine" || s == "Oluja" }
            "Ekstremne temperature" -> WeatherStaticData.getLokacijeKorisnika()
                .filter {
                    val p = WeatherStaticData.getPrognozu(it.naziv)
                    p != null && (p.temperatura < 0 || p.temperatura > 35)
                }
            else -> WeatherStaticData.getLokacijeKorisnika()
        }
    }

    fun odaberiDrzavu(drzava: Drzava?) {
        _odabranaDrzava.value = drzava
        _odabraniGrad.value = null                       // reset grada
        _gradoviZaDrzavu.value = if (drzava == null) emptyList()
        else GradStaticData.getGradoviZaDodavanje(drzava.naziv)
        provjeriDugme()
    }

    fun odaberiGrad(grad: Grad?) {
        _odabraniGrad.value = grad
        provjeriDugme()
    }

    fun odaberiTip(tip: String?) {
        _odabraniTip.value = tip
        provjeriDugme()
    }

    private fun provjeriDugme() {
        _dugmeEnabled.value =
            _odabranaDrzava.value != null &&
                    _odabraniGrad.value != null &&
                    _odabraniTip.value != null
    }

    fun dodajLokaciju() {
        val grad = _odabraniGrad.value ?: return         // ako grad null → izađi
        val tip = _odabraniTip.value ?: return           // ako tip null → izađi

        val novaLokacija = Lokacija(
            naziv = grad.naziv,
            drzava = grad.nazivDrzave,
            latitude = grad.lat,
            longitude = grad.lon,
            tipPrikaza = tip,
            korisnikUpisan = true
        )
        WeatherStaticData.dodajLokaciju(novaLokacija)

        // reset stanja forme
        _odabranaDrzava.value = null
        _odabraniGrad.value = null
        _odabraniTip.value = null
        _gradoviZaDrzavu.value = emptyList()
        _dugmeEnabled.value = false

        // osvježi listu prema trenutnom filteru
        postaviFilter(_odabraniFilter.value ?: "Sve moje lokacije")
    }
}


