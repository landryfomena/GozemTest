package com.example.kycapp.api

import android.net.Uri
import com.example.kycapp.MainActivity
import com.example.kycapp.MainActivity.Companion.storage
import com.example.kycapp.entites.Agent
import java.io.File

class AgentApi {
    val db = MainActivity.db
    fun listAgent(onSuccess: (agents: List<Agent>) -> Unit, onFailure: (message: String) -> Unit) {
        db.collection("Agents").get().addOnSuccessListener {
            if (it.isEmpty) {

            } else {
                try {
                    var data = it.toObjects(Agent::class.java)
                    onSuccess(data)
                }catch (e:Exception){
                    if (e.message==null){
                        onFailure("failed to get the agents")
                    }else{
                        onFailure(e.message!!)
                    }

                }

            }
        }
    }

    fun createAgent(agent: Agent, onSuccess: () -> Unit, onFailure: (message: String) -> Unit) {
        db.collection("Agents").document().set(agent).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener {
            if (it.message == null) {
                onFailure("failed to save the agent try again later")
            } else {
                onFailure("failed to save the agent: ${it.message}")

            }

        }

    }

    fun uploadFile(imageUri: Uri,onSuccess: (path:String) -> Unit,onFailure: (message: String) -> Unit){
        // Create a storage reference from our app
        val storageRef = storage.reference
        var file = imageUri
        val riversRef = storageRef.child("images/${file.lastPathSegment}")
      var   uploadTask = riversRef.putFile(file)
// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            onFailure(it.message!!)
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            riversRef.downloadUrl.addOnSuccessListener {url->
                onSuccess(url.toString())
            }
        }

    }


}