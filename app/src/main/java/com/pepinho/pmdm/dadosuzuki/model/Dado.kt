package com.pepinho.pmdm.dadosuzuki.model

//import kotlin.random.Random

open class Dado(private val numeroLados: Int) {

    fun lanzar(): Int {
//        return Random.nextInt(1, numeroLados + 1)
        return (1..numeroLados).random()
    }
}