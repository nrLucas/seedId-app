package com.plcoding.landmarkrecognitiontensorflow


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.ExperimentalMaterial3Api
import com.google.firebase.database.FirebaseDatabase


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleForm() {
    val textState = remember { mutableStateOf("") }

    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("message")



    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        TextField(
            value = textState.value,
            onValueChange = { textState.value = it },
            label = { Text("Digite algo") }
        )
        Button(onClick = { myRef.setValue(textState) }) {
            Text("Enviar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSimpleForm() {
    SimpleForm()
}