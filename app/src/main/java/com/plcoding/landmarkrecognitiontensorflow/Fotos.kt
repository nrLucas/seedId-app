package com.plcoding.landmarkrecognitiontensorflow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.plcoding.landmarkrecognitiontensorflow.ui.theme.GreenJC

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.plcoding.landmarkrecognitiontensorflow.util.StorageUtil

/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Photos(){

    val database = Firebase.database
    val myRef = database.getReference("message")

    myRef.setValue("Hello, World");

    var text by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp),
            verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

            TextField(value = text, onValueChange = {newText -> text = newText},
                label = { Text(text = "Nome da Semente")}
            )
            Button(onClick = { myRef.setValue(text) },
                modifier = Modifier.padding(16.dp)) {
                Text(text = "Enviar Foto", fontSize = 30.sp, color = GreenJC)

            }
        }
    }
}*/




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Photos(){
    val context = LocalContext.current

    var class_user by remember { mutableStateOf("") }
    var subclass_user by remember { mutableStateOf("") }


    var imageUris by remember{
        mutableStateOf<List<Uri>>(emptyList())
    }

    val multiplePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = {
            imageUris = it
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 100.dp),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){

        TextField(value = class_user, onValueChange = {newText -> class_user = newText},
            label = { Text(text = "Classificação 1 da Semente")}
        )
        TextField(value = subclass_user, onValueChange = {newText -> subclass_user = newText},
            label = { Text(text = "Classificação 2 da Semente")}
        )

        Button(onClick = {
            imageUris.forEachIndexed { index, uri ->
                uri?.let{
                    StorageUtil.uploadToStorage(uri=it, context=context, type="image", index=index)
                }
            }

            class_user = ""
            subclass_user = ""
            imageUris = emptyList()

        }){
            Text("Enviar")
        }

            LazyColumn{
                item{
                    Button(
                        onClick = {
                            multiplePhotoPicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxSize().padding(horizontal = 50.dp) // Alinha o botão horizontalmente no centro
                    ) {

                        Text("Escolher Imagens")
                    }
                }


                    items(imageUris){ uri ->
                        AsyncImage(model = uri, contentDescription = null, modifier = Modifier
                            .size(248.dp)
                            .padding(top = 20.dp))

                    }



            }




    }


}


