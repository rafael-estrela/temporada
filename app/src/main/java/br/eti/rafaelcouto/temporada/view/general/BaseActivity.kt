package br.eti.rafaelcouto.temporada.view.general

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    abstract fun setupRecyclerView()
    abstract fun observe()
}
