package com.plcoding.landmarkrecognitiontensorflow.util


import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.UUID
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2

class StorageUtil{


    companion object {

        fun getNumberOfFilesInDirectory(directory: String, onComplete: (Int) -> Unit) {
            val storageRef = Firebase.storage.reference
            val dirRef = storageRef.child(directory)

            // List all items (files) in the directory
            dirRef.listAll().addOnSuccessListener { listResult ->
                onComplete(listResult.items.size)
            }.addOnFailureListener {
                onComplete(0)  // Assume 0 if there is a failure
            }
        }

        fun uploadToStorage(uri: Uri, context: Context, type: String, index: Int) {
            getNumberOfFilesInDirectory("imagens") { count ->
                val storage = Firebase.storage
                var storageRef = storage.reference
                val fileName = "${count + 1}"  // This will be 11 if there are already 10 files
                val filePath = if (type == "image") "imagens/$fileName.jpg" else "videos/$fileName.mp4"
                val spaceRef = storageRef.child(filePath)

                val byteArray: ByteArray? = context.contentResolver
                    .openInputStream(uri)
                    ?.use { it.readBytes() }

                byteArray?.let {
                    var uploadTask = spaceRef.putBytes(byteArray)
                    uploadTask.addOnFailureListener {
                        Toast.makeText(
                            context,
                            "Houve um erro ao enviar a imagem ${count + 1}",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Handle unsuccessful uploads
                    }.addOnSuccessListener { taskSnapshot ->
                        Toast.makeText(
                            context,
                            "Imagem ${count + 1} enviada com sucesso",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}