package ba.etf.weatherwatch.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ba.etf.weatherwatch.R
import ba.etf.weatherwatch.model.SatnaPrognoza

class SatnaPrognozeAdapter(
    private val stavke: List<SatnaPrognoza>,
    private val fahrenheit: Boolean
) : RecyclerView.Adapter<SatnaPrognozeAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val sat: TextView = v.findViewById(R.id.tvSat)
        val ikona: ImageView = v.findViewById(R.id.ivSatnaIkona)
        val temp: TextView = v.findViewById(R.id.tvSatnaTemp)
        val pad: TextView = v.findViewById(R.id.tvSatnaPadavine)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_satna, parent, false)
        return VH(v)
    }
    override fun getItemCount() = stavke.size
    override fun onBindViewHolder(h: VH, position: Int) {
        val s = stavke[position]
        h.sat.text = s.sat
        h.ikona.setImageResource(WeatherIkone.zaTip(s.vrijemeTipa))
        h.temp.text = if (fahrenheit) "${(s.temperatura*9f/5f+32f).toInt()}°" else "${s.temperatura.toInt()}°"
        h.pad.text = if (s.padavinePostotak > 0) "${s.padavinePostotak}%" else ""
    }
}