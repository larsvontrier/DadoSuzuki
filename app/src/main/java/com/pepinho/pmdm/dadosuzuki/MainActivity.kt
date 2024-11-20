package com.pepinho.pmdm.dadosuzuki

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.ViewPropertyAnimator
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pepinho.pmdm.dadosuzuki.databinding.ActivityMainBinding
import com.pepinho.pmdm.dadosuzuki.model.DadoSuzuki
import com.pepinho.pmdm.dadosuzuki.model.Ritmo
import kotlin.time.Duration

class MainActivity : AppCompatActivity() {

    /*
     * Al crear arrays de objetos permite que se puedan añadir más objetos sin tener que modificar
     * el código de la actividad y facilita la asignación de un valor entre el número aleatorio
     * obtenido y el objeto correspondiente.
     */
    companion object {
        val RITMOS = arrayOf(Ritmo("Una vaca loca", R.drawable.icona1),
            Ritmo("Mickey Mouse", R.drawable.icona2),
            Ritmo("Tren rojo, tren verde", R.drawable.icona3),
            Ritmo("Plátano, plátano", R.drawable.icona4),
            Ritmo("Chocolate, caramelo", R.drawable.icona5),
            Ritmo("Tema", R.drawable.icona6)
        )

        val IV_DADO = arrayOf(R.drawable.dadosuzuki1, R.drawable.dadosuzuki2, R.drawable.dadosuzuki3,
            R.drawable.dadosuzuki4, R.drawable.dadosuzuki5, R.drawable.dadosuzuki6)
    }


    private val dado = DadoSuzuki()
    private lateinit var ivDado: ImageView
    private lateinit var btLanzar: Button
    private lateinit var ivIcono: ImageView
    lateinit var binding: ActivityMainBinding

    private var mediaAudio: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btLanzar = findViewById(R.id.btLanzar)
        btLanzar.setOnClickListener { lanzarDado() }

        ivDado = findViewById(R.id.ivDado)
        ivDado.setOnClickListener { lanzarDado() }

        ivIcono = findViewById(R.id.ivLogo)
        mediaAudio = MediaPlayer.create(this, R.raw.roll)

    }


    private fun lanzarDado() {
        // Lanzamos el dado
        val valorDado = dado.lanzar()

        // Muestro la animación del dado y el logo
        mostrarDado(valorDado)
    }


    private fun mostrarDado(valorDado: Int) {

        Log.d("Valor dado", "$valorDado")

        // Actualizo la vista para el cado correcto
        ivDado.setImageResource( IV_DADO[valorDado - 1]
//            when (valorDado) {
//                1 -> R.drawable.dadosuzuki1
//                2 -> R.drawable.dadosuzuki2
//                3 -> R.drawable.dadosuzuki3
//                4 -> R.drawable.dadosuzuki4
//                5 -> R.drawable.dadosuzuki5
//                else -> R.drawable.dadosuzuki6
//            }
        )

        // Animo
        ivDado.animate()
            .rotationBy(720f) // Gira 360 grados
            .setDuration(2000) // Duración de la animación en milisegundos
            .setInterpolator(DecelerateInterpolator()) // Interpolador para desacelerar
            // En kotlin object es un objeto anónimo que implementa AnimatorListenerAdapter:
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    super.onAnimationStart(animation)
                    btLanzar.isEnabled = false // Deshabilito el botón
                    ivIcono.fade(false, 1000).start() // Hace un fade out para que el icono desaparezca
                    // Pongo la imagen a null para que no se muestre
//                    ivIcono.setImageResource(0) // Hace que el logo esté invisible
                    mediaAudio?.start() // Suena el dado
                }

                override fun onAnimationEnd(animation: Animator) {
                    ivIcono.apply {
//                        alpha = 0f // Hace que el logo esté invisible
                        setImageResource(RITMOS[valorDado - 1].idIcono)
                        fade(true).start() // Hace un fade in para que el icono aparezca
                    }
                    btLanzar.isEnabled = true // Se habilita el botón
                }
            })
            .start()
        // Actualizo la descripcion del cantenido
        ivDado.contentDescription = RITMOS[valorDado - 1].nombre
    }
}

/**
 * Función de extensión para ImageView que permite realizar una animación de fade in o fade out.
 * Devuelve un objeto ViewPropertyAnimator que permite encadenar animaciones.
 */
fun ImageView.fade(fadeIn: Boolean = true, duration: Long = 1000L): ViewPropertyAnimator {
    return this.animate()
        .alpha(if (fadeIn) 1f else 0f)
        .setDuration(duration)
        .setInterpolator(DecelerateInterpolator())
}