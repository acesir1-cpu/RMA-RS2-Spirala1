package ba.etf.weatherwatch.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import ba.etf.weatherwatch.R
import ba.etf.weatherwatch.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private val vm: MainViewModel by viewModels()
    private lateinit var adapter: LokacijaAdapter

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeHelper.apply(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        postaviRecycler()
        postaviFilterSpinner()
        postaviDrzavaSpinner()
        postaviGradSpinner()
        postaviTipSpinner()
        postaviDugmad()
        posmatraj()
    }

    private fun postaviRecycler() {
        adapter = LokacijaAdapter { lok ->
            val i = Intent(this, DetaljiActivity::class.java)
            i.putExtra("LOKACIJA", lok.naziv)
            startActivity(i)
        }
        val rv = findViewById<RecyclerView>(R.id.recyclerLokacije)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

    private fun postaviFilterSpinner() {
        val sp = findViewById<Spinner>(R.id.filterLokacija)
        val a = ArrayAdapter(this, R.layout.item_spinner_selected, vm.filterOpcije)
        a.setDropDownViewResource(R.layout.item_spinner_dropdown)
        sp.adapter = a
        sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                vm.postaviFilter(vm.filterOpcije[pos])
            }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }
    }

    private lateinit var drzavaSpinner: Spinner
    private fun postaviDrzavaSpinner() {
        drzavaSpinner = findViewById(R.id.odabirDrzave)
        val nazivi = listOf("Odaberi državu") + vm.sveDrzave.map { it.naziv }
        val a = ArrayAdapter(this, R.layout.item_spinner_selected, nazivi)
        a.setDropDownViewResource(R.layout.item_spinner_dropdown)
        drzavaSpinner.adapter = a
        drzavaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                if (pos == 0) vm.odaberiDrzavu(null)
                else vm.odaberiDrzavu(vm.sveDrzave[pos - 1])    // -1 zbog placeholdera
            }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }
    }

    private lateinit var gradSpinner: Spinner
    private var trenutniGradovi: List<ba.etf.weatherwatch.model.Grad> = emptyList()
    private fun postaviGradSpinner() {
        gradSpinner = findViewById(R.id.odabirGrada)
        gradSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                if (pos == 0) vm.odaberiGrad(null)
                else vm.odaberiGrad(trenutniGradovi[pos - 1])
            }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }
    }

    private fun postaviTipSpinner() {
        val sp = findViewById<Spinner>(R.id.odabirTipaPrikaza)
        val opcije = listOf("Odaberi tip") + vm.tipOpcije
        val a = ArrayAdapter(this, R.layout.item_spinner_selected, opcije)
        a.setDropDownViewResource(R.layout.item_spinner_dropdown)
        sp.adapter = a
        sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                if (pos == 0) vm.odaberiTip(null) else vm.odaberiTip(vm.tipOpcije[pos - 1])
            }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }
    }

    private fun postaviDugmad() {
        findViewById<MaterialButton>(R.id.dodajLokacijuDugme).setOnClickListener {
            vm.dodajLokaciju()
            // reset sva tri spinnera na poziciju 0
            drzavaSpinner.setSelection(0)
            gradSpinner.setSelection(0)
            findViewById<Spinner>(R.id.odabirTipaPrikaza).setSelection(0)
            Toast.makeText(this, R.string.dodano, Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.prikaziPrognozuDugme).setOnClickListener {
            val i = Intent(this, PrognozaActivity::class.java)
            i.putExtra("FILTER", vm.odabraniFilter.value)
            startActivity(i)
        }
    }

    private fun posmatraj() {
        vm.filterovaneLokacije.observe(this) { lista ->
            adapter.submitList(lista)
            findViewById<TextView>(R.id.brojLokacija).text =
                getString(R.string.pronadeno, lista.size)     // "Pronađeno je N lokacija"
        }
        vm.gradoviZaDrzavu.observe(this) { gradovi ->
            trenutniGradovi = gradovi
            val nazivi = listOf("Odaberi grad") + gradovi.map { it.naziv }
            val a = ArrayAdapter(this, R.layout.item_spinner_selected, nazivi)
            a.setDropDownViewResource(R.layout.item_spinner_dropdown)
            gradSpinner.adapter = a
        }
        vm.dugmeEnabled.observe(this) { enabled ->
            findViewById<MaterialButton>(R.id.dodajLokacijuDugme).isEnabled = enabled
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu); return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            startActivity(Intent(this, SettingsActivity::class.java)); return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences("ww_prefs", Context.MODE_PRIVATE)
        val fahr = prefs.getString("jedinice", "celsius") == "fahrenheit"
        if (fahr != adapter.fahrenheit) {
            adapter.fahrenheit = fahr
            adapter.notifyDataSetChanged()
        }
    }
}