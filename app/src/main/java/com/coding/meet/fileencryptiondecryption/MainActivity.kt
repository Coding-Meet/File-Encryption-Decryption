package com.coding.meet.fileencryptiondecryption

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val encryptBtn = findViewById<Button>(R.id.encryptBtn)
        val decryptBtn = findViewById<Button>(R.id.decryptBtn)


        val securityManger = SecurityManger(this)

        encryptBtn.setOnClickListener {
//            securityManger.encryptFile(
//                "demo.txt",
//                "encryptedDemo.txt"
//            )

            securityManger.encryptFile(
                "question.json",
                "encryptedQuestion.json"
            )
        }

        decryptBtn.setOnClickListener {
            val decryptionFileToString = securityManger.decryptFile(
                "encryptedQuestion.json"
            )
            Log.d("decryptionFileToString",decryptionFileToString)
        }
    }
}