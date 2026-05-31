package ba.etf.weatherwatch.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ba.etf.weatherwatch.R
import ba.etf.weatherwatch.data.WeatherStaticData
import ba.etf.weatherwatch.model.Lokacija

class LokacijaAdapter(
    private val onClick: (Lokacija) -> Unit
) : ListAdapter<Lokacija, LokacijaAdapter.ViewHolder>(DIFF) {

    var fahrenheit: Boolean = false       // mijenja MainActivity u onResume

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Lokacija>() {
            override fun areItemsTheSame(o: Lokacija, n: Lokacija) =
                o.naziv == n.naziv && o.drzava == n.drzava
            override fun areContentsTheSame(o: Lokacija, n: Lokacija) = o == n
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val statusIndikator: ImageView = v.findViewById(R.id.statusIndikator)
        val ivWeatherIcon: ImageView = v.findViewById(R.id.ivWeatherIcon)
        val tvNaziv: TextView = v.findViewById(R.id.tvNaziv)
        val tvDrzava: TextView = v.findViewById(R.id.tvDrzava)
        val tvOpis: TextView = v.findViewById(R.id.tvOpis)
        val tvTip: TextView = v.findViewById(R.id.tvTip)
        val tvTemperatura: TextView = v.findViewById(R.id.tvTemperatura)
        val tvMinMax: TextView = v.findViewById(R.id.tvMinMax)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lokacija, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lok = getItem(position)
        val prognoza = WeatherStaticData.getPrognozu(lok.naziv)

        holder.tvNaziv.text = lok.naziv
        holder.tvDrzava.text = lok.drzava
        holder.tvTip.text = lok.tipPrikaza

        // --- status tačka ---
        val status = WeatherStaticData.getStatus(lok.naziv)
        val (dot, opisStatusa) = when (status) {
            "Vedro"    -> R.drawable.ic_dot_green  to "Vedro"
            "Toplo"    -> R.drawable.ic_dot_yellow to "Toplo"
            "Vruce"    -> R.drawable.ic_dot_orange to "Vruce"
            "Padavine" -> R.drawable.ic_dot_blue   to "Padavine"
            "Mraz"     -> R.drawable.ic_dot_blue   to "Mraz"
            "Oluja"    -> R.drawable.ic_dot_red    to "Oluja"
            else       -> R.drawable.ic_dot_green  to "Vedro"
        }
        holder.statusIndikator.setImageResource(dot)
        holder.statusIndikator.contentDescription = opisStatusa

        // --- ikona vremena + tekstovi ---
        if (prognoza == null) {
            holder.ivWeatherIcon.setImageResource(R.drawable.ic_weather_cloudy)
            holder.tvOpis.text = ""
            holder.tvMinMax.text = ""
            holder.tvTemperatura.text = if (fahrenheit) "--°F" else "--°"
        } else {
            holder.ivWeatherIcon.setImageResource(ikonaZaTip(prognoza.vrijemeTipa))
            holder.tvOpis.text = prognoza.opisVremena
            holder.tvTemperatura.text = formatTemp(prognoza.temperatura)
            holder.tvMinMax.text = "${formatTemp(prognoza.maxTemp)} / ${formatTemp(prognoza.minTemp)}"
        }

        holder.itemView.setOnClickListener { onClick(lok) }
    }

    private fun ikonaZaTip(tip: String): Int = when (tip) {
        "sunny"          -> R.drawable.ic_weather_sunny
        "partly_cloudy"  -> R.drawable.ic_weather_partly_cloudy
        "cloudy"         -> R.drawable.ic_weather_cloudy
        "rainy"          -> R.drawable.ic_weather_rainy
        "snowy"          -> R.drawable.ic_weather_snowy
        "stormy"         -> R.drawable.ic_weather_stormy
        "foggy"          -> R.drawable.ic_weather_foggy
        else             -> R.drawable.ic_weather_cloudy
    }

    private fun formatTemp(c: Float): String =
        if (fahrenheit) "${(c * 9f / 5f + 32f).toInt()}°F" else "${c.toInt()}°"
}