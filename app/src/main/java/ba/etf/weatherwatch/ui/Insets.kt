package ba.etf.weatherwatch.ui

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

/**
 * Pomoć za edge-to-edge (Android 15+ / targetSdk 35+), gdje se sadržaj crta ispod
 * statusne i navigacijske trake. Dodaje odgovarajući system-bar inset kao padding
 * da toolbar ne završi ispod sata/baterije i da se može kliknuti.
 */
object Insets {

    /** Gornji (status bar) inset kao top padding */
    fun applyTop(view: View) {
        val base = view.paddingTop
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
            v.updatePadding(top = base + top)
            insets
        }
    }

    /** Donji (navigacijska traka) inset kao bottom padding. */
    fun applyBottom(view: View) {
        val base = view.paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            v.updatePadding(bottom = base + bottom)
            insets
        }
    }
}
