package com.colisa.findme

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dialog = createInstructionDialog()
        dialog.show()
    }

    private fun createInstructionDialog(): Dialog {
        return MaterialAlertDialogBuilder(this)
            .setIcon(R.drawable.android)
            .setTitle(R.string.instructions_title)
            .setMessage(R.string.instructions)
            .setPositiveButtonIcon(ContextCompat.getDrawable(this, android.R.drawable.ic_media_play))
            .create()
    }
}
