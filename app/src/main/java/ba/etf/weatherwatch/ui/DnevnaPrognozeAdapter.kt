package ba.etf.weatherwatch.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ba.etf.weatherwatch.R
import ba.etf.weatherwatch.model.DnevnaPrognoza

class DnevnaPrognozeAdapter(
    private val stavke: List<DnevnaPrognoza>,
    private val fahrenheit: Boolean
) : RecyclerView.Adapter<DnevnaPrognozeAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val dan: TextView = v.findViewById(R.id.tvDan)
        val ikona: ImageView = v.findViewById(R.id.ivDnevnaIkona)
        val pad: TextView = v.findViewById(R.id.tvDnevnaPadavine)
        val min: TextView = v.findViewById(R.id.tvDnevnaMin)
        val max: TextView = v.findViewById(R.id.tvDnevnaMax)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_dnevna, parent, false)
        return VH(v)
    }
    override fun getItemCount() = stavke.size
    override fun onBindViewHolder(h: VH, position: Int) {
        val d = stavke[position]
        h.dan.text = d.dan
        h.ikona.setImageResource(WeatherIkone.zaTip(d.vrijemeTipa))
        h.pad.text = if (d.padavinePostotak > 0) "${d.padavinePostotak}%" else ""
        val mn = if (fahrenheit) (d.minTemp*9f/5f+32f).toInt() else d.minTemp.toInt()
        val mx = if (fahrenheit) (d.maxTemp*9f/5f+32f).toInt() else d.maxTemp.toInt()
        h.min.text = "$mn°"
        h.max.text = "$mx°"
    }
}