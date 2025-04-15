import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.al_quran.Ayah
import com.example.al_quran.R

class AyahAdapter : ListAdapter<Ayah, AyahAdapter.AyahViewHolder>(AyahDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AyahViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ayah, parent, false)
        return AyahViewHolder(view)
    }

    override fun onBindViewHolder(holder: AyahViewHolder, position: Int) {
        val ayah = getItem(position)
        holder.bind(ayah)
    }

    class AyahViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val arabicText: TextView = itemView.findViewById(R.id.textArabic)
        private val ayahNumberText: TextView = itemView.findViewById(R.id.textNumber)
        private val translationText: TextView = itemView.findViewById(R.id.textTranslation)
        private val playButton: ImageButton = itemView.findViewById(R.id.buttonPlay)

        private var mediaPlayer: MediaPlayer? = null
        private var isPlaying = false
        private var currentAudioUrl: String? = null

        fun bind(ayah: Ayah) {
            val arabicNumber = convertToArabicNumber(ayah.numberInSurah)
            ayahNumberText.text = arabicNumber
            arabicText.text = ayah.arabicText
            translationText.text = ayah.translationText

            playButton.setImageResource(android.R.drawable.ic_media_play)

            playButton.setOnClickListener {
                if (isPlaying && currentAudioUrl == ayah.audioUrl) {
                    mediaPlayer?.pause()
                    isPlaying = false
                    playButton.setImageResource(android.R.drawable.ic_media_play)
                } else {
                    if (currentAudioUrl != ayah.audioUrl) {
                        mediaPlayer?.release()
                        mediaPlayer = null
                    }

                    mediaPlayer = MediaPlayer().apply {
                        setDataSource(ayah.audioUrl)
                        prepareAsync()
                        setOnPreparedListener {
                            start()
                            this@AyahViewHolder.isPlaying = true
                            this@AyahViewHolder.currentAudioUrl = ayah.audioUrl
                            playButton.setImageResource(android.R.drawable.ic_media_pause)
                        }
                        setOnCompletionListener {
                            this@AyahViewHolder.isPlaying = false
                            playButton.setImageResource(android.R.drawable.ic_media_play)
                            release()
                            this@AyahViewHolder.mediaPlayer = null
                            this@AyahViewHolder.currentAudioUrl = null
                        }
                    }
                }
            }
        }
        private fun convertToArabicNumber(number: Int): String {
            val arabicDigits = arrayOf('٠','١','٢','٣','٤','٥','٦','٧','٨','٩')
            return number.toString().map { arabicDigits[it.toString().toInt()] }.joinToString("")
        }
    }

    class AyahDiffCallback : DiffUtil.ItemCallback<Ayah>() {
        override fun areItemsTheSame(oldItem: Ayah, newItem: Ayah): Boolean {
            return oldItem.number == newItem.number
        }

        override fun areContentsTheSame(oldItem: Ayah, newItem: Ayah): Boolean {
            return oldItem == newItem
        }
    }
}
