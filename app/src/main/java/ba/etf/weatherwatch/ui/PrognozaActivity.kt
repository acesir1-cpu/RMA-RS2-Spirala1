package ba.etf.weatherwatch.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.weatherwatch.R
import ba.etf.weatherwatch.viewmodel.PrognozaViewModel

class PrognozaActivity : AppCompatActivity() {
    private val vm: PrognozaViewModel by viewModels()
    private lateinit var adapter: LokacijaAdapter

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeHelper.apply(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prognoza)

        val filter = intent.getStringExtra("FILTER") ?: "Sve moje lokacije"

        val toolbar = findViewById<Toolbar>(R.id.toolbarPrognoza)
        toolbar.title = filter
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
        Insets.applyTop(toolbar)

        adapter = LokacijaAdapter { lok ->
            val i = Intent(this, DetaljiActivity::class.java)
            i.putExtra("LOKACIJA", lok.naziv)
            startActivity(i)
        }
        val rv = findViewById<RecyclerView>(R.id.recyclerLokacije)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
        Insets.applyBottom(rv)

        vm.lokacije.observe(this) { adapter.submitList(it) }
        vm.ucitajLokacije(filter)
    }
}