package ba.etf.weatherwatch.ui
import android.content.Context
import android.content.res.Configuration
import java.util.Locale

/**
 * Primjenjuje jezik spremljen u postavkama ("ww_prefs" -> "jezik").
 * Default je "bs" (bosanski). Pozvati iz attachBaseContext svake aktivnosti.
 */
object LocaleHelper {
    fun wrap(context: Context): Context {
        val prefs = context.getSharedPreferences("ww_prefs", Context.MODE_PRIVATE)
        val lang = prefs.getString("jezik", "bs") ?: "bs"
        val locale = Locale.forLanguageTag(lang)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}
