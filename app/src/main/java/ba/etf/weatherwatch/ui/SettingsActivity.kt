package ba.etf.weatherwatch.ui
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import ba.etf.weatherwatch.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeHelper.apply(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.toolbarSettings)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
        Insets.applyTop(toolbar)

        prefs = getSharedPreferences("ww_prefs", Context.MODE_PRIVATE)

        val rgTema = findViewById<RadioGroup>(R.id.rgTema)
        val rgJezik = findViewById<RadioGroup>(R.id.rgJezik)
        val rgJedinice = findViewById<RadioGroup>(R.id.rgJedinice)
        val swNotif = findViewById<SwitchMaterial>(R.id.swNotifikacije)
        val swOluja = findViewById<SwitchMaterial>(R.id.swOluja)

        // --- 1) Učitaj sačuvane vrijednosti i postavi UI ---
        when (prefs.getString("tema", "auto")) {
            "light" -> rgTema.check(R.id.rbTemaLight)
            "dark"  -> rgTema.check(R.id.rbTemaDark)
            else    -> rgTema.check(R.id.rbTemaAuto)
        }
        when (prefs.getString("jezik", "bs")) {
            "en" -> rgJezik.check(R.id.rbJezikEn)
            else -> rgJezik.check(R.id.rbJezikBs)
        }
        when (prefs.getString("jedinice", "celsius")) {
            "fahrenheit" -> rgJedinice.check(R.id.rbFahrenheit)
            else         -> rgJedinice.check(R.id.rbCelsius)
        }
        swNotif.isChecked = prefs.getBoolean("notifikacije", true)
        swOluja.isChecked = prefs.getBoolean("notifikacije_oluja", true)

        // --- 2) Svaka promjena odmah piše u SharedPreferences ---
        rgTema.setOnCheckedChangeListener { _, id ->
            val v = when (id) {
                R.id.rbTemaLight -> "light"
                R.id.rbTemaDark  -> "dark"
                else             -> "auto"
            }
            prefs.edit().putString("tema", v).apply()
            ThemeHelper.apply(this)        // odmah primijeni temu
            recreate()                      // precrtaj ekran s novom temom
        }
        rgJezik.setOnCheckedChangeListener { _, id ->
            val v = if (id == R.id.rbJezikEn) "en" else "bs"
            if (v != prefs.getString("jezik", "bs")) {
                prefs.edit().putString("jezik", v).apply()
                recreate()   // odmah primijeni novi jezik
            }
        }
        rgJedinice.setOnCheckedChangeListener { _, id ->
            val v = if (id == R.id.rbFahrenheit) "fahrenheit" else "celsius"
            prefs.edit().putString("jedinice", v).apply()
        }
        swNotif.setOnCheckedChangeListener { _, checked ->
            prefs.edit().putBoolean("notifikacije", checked).apply()
        }
        swOluja.setOnCheckedChangeListener { _, checked ->
            prefs.edit().putBoolean("notifikacije_oluja", checked).apply()
        }
    }
}