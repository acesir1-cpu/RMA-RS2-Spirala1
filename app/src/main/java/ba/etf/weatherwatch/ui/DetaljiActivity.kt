package ba.etf.weatherwatch.ui
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.weatherwatch.R
import ba.etf.weatherwatch.data.WeatherStaticData

class DetaljiActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeHelper.apply(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalji)

        val naziv = intent.getStringExtra("LOKACIJA") ?: ""
        val prognoza = WeatherStaticData.getPrognozu(naziv)

        val toolbar = findViewById<Toolbar>(R.id.toolbarDetalji)
        toolbar.title = naziv
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        Insets.applyTop(toolbar)
        Insets.applyBottom(findViewById(R.id.detaljiContent))

        val prefs = getSharedPreferences("ww_prefs", Context.MODE_PRIVATE)
        val fahr = prefs.getString("jedinice", "celsius") == "fahrenheit"

        if (prognoza == null) return    // nema podataka — ostavi prazno

        findViewById<ImageView>(R.id.ivMainWeatherIcon)
            .setImageResource(WeatherIkone.zaTip(prognoza.vrijemeTipa))
        findViewById<ImageView>(R.id.ivBgGradient)
            .setImageResource(WeatherIkone.pozadinaZaTip(prognoza.vrijemeTipa))

        fun fmt(c: Float) =
            if (fahr) "${(c*9f/5f+32f).toInt()}°F" else "${c.toInt()}°C"

        findViewById<TextView>(R.id.tvGlavnaTemp).text = fmt(prognoza.temperatura)
        findViewById<TextView>(R.id.tvGlavniOpis).text = prognoza.opisVremena
        findViewById<TextView>(R.id.tvOsjecaj).text =
            "${getString(R.string.osjecaj_prefix)} ${fmt(prognoza.osjecajTemperature)}"
        findViewById<TextView>(R.id.tvMinMaxGlavni).text =
            "${fmt(prognoza.minTemp)} / ${fmt(prognoza.maxTemp)}"

        // --- DETALJI ---
        findViewById<TextView>(R.id.tvVjetar).text =
            "${prognoza.brzinaVjetra.toInt()} km/h ${prognoza.smjerVjetra}"
        findViewById<TextView>(R.id.tvVlaznost).text = "${prognoza.vlaznost}%"
        findViewById<TextView>(R.id.tvUv).text =
            "${prognoza.uvIndeks.toInt()} — ${uvOpis(prognoza.uvIndeks)}"
        findViewById<TextView>(R.id.tvPritisak).text = "${prognoza.pritisak} hPa"
        findViewById<TextView>(R.id.tvVidljivost).text = "${prognoza.vidljivost} km"
        findViewById<TextView>(R.id.tvOblacnost).text = "${prognoza.oblacnost}%"

        // --- PADAVINE ---
        val pad = prognoza.padavine ?: 0f
        findViewById<TextView>(R.id.tvPadavine).text =
            if (pad % 1f == 0f) "${pad.toInt()} mm" else "$pad mm"

        val rvSatna = findViewById<RecyclerView>(R.id.rvSatnaPrognoza)
        rvSatna.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvSatna.adapter = SatnaPrognozeAdapter(prognoza.prognozaPoSatima, fahr)

        val rvDnevna = findViewById<RecyclerView>(R.id.rvDnevnaPrognoza)
        rvDnevna.layoutManager = LinearLayoutManager(this)
        rvDnevna.adapter = DnevnaPrognozeAdapter(prognoza.prognozaDani, fahr)
    }

    private fun uvOpis(uv: Float): String = when {
        uv < 3f  -> "Nizak"
        uv < 6f  -> "Umjeren"
        uv < 8f  -> "Visok"
        uv < 11f -> "Veoma visok"
        else     -> "Ekstreman"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detalji, menu); return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { finish(); true }
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java)); true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
