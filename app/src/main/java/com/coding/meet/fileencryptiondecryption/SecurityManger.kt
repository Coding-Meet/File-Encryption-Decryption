package com.coding.meet.fileencryptiondecryption

import android.content.Context
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class SecurityManger(private val context: Context) {

//    companion object{
//        private const val KEYPASSWORD = ""
//        private const val IVPASSWORD = ""
//        private const val ALGORITHM = ""
//        private const val TRANSFORMATION = ""
//    }


    private val secretKeySpec = generateKey()

    private fun generateKey() : SecretKeySpec {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = BuildConfig.KEYPASSWORD.toByteArray()
        digest.update(bytes,0,bytes.size)
        return SecretKeySpec(digest.digest(),BuildConfig.ALGORITHM)
    }

    fun encryptFile(fileName: String, encryptFileName:String){
        val readFileToString = context.loadFromAsset(fileName)

        val encryptFile = File(context.filesDir,encryptFileName)
        if (encryptFile.exists()){
            encryptFile.delete()
        }
        encryptFile.createNewFile()

        val encryptCipher = Cipher.getInstance(BuildConfig.TRANSFORMATION).apply {
            init(
                Cipher.ENCRYPT_MODE,
                secretKeySpec,
                IvParameterSpec(BuildConfig.IVPASSWORD.toByteArray())
            )
        }
        val fos = FileOutputStream(encryptFile)
        val cos = CipherOutputStream(fos,encryptCipher)
        cos.write(readFileToString.encodeToByteArray())
        cos.flush()
        cos.close()
        Log.d("status","Encryption File Successfully")
    }


    fun decryptFile(encryptFileName : String) :String{

        // Load Encrypt File From Asset
        val fis = context.assets.open(encryptFileName)


//        val encryptedFile = File(context.filesDir,encryptFileName)
//        val fis = FileInputStream(encryptedFile)


        val decryptCipher = Cipher.getInstance(BuildConfig.TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE,secretKeySpec,IvParameterSpec(BuildConfig.IVPASSWORD.toByteArray()))
        }

        val inputStream = CipherInputStream(fis,decryptCipher)

        val byteArrayOutputStream = ByteArrayOutputStream()
        var nextBytes = inputStream.read()
        while (nextBytes != -1){
            byteArrayOutputStream.write(nextBytes)
            nextBytes = inputStream.read()
        }

        Log.d("status","Decryption File Successfully")

        return byteArrayOutputStream.toByteArray().decodeToString().trim()
    }

    private fun Context.loadFromAsset(fileName: String):String{
        val inputStream = assets.open(fileName)
        val size = inputStream.available()
        val byteArray = ByteArray(size)
        inputStream.read(byteArray)
        inputStream.close()
        return String(byteArray,Charsets.UTF_8)
    }

}