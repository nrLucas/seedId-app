package com.plcoding.landmarkrecognitiontensorflow

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.plcoding.landmarkrecognitiontensorflow.data.TfLiteLandmarkClassifier
import com.plcoding.landmarkrecognitiontensorflow.domain.Classification
import com.plcoding.landmarkrecognitiontensorflow.presentation.CameraPreview
import com.plcoding.landmarkrecognitiontensorflow.presentation.LandmarkImageAnalyzer
import com.plcoding.landmarkrecognitiontensorflow.ui.theme.GreenJC
import com.plcoding.landmarkrecognitiontensorflow.ui.theme.LandmarkRecognitionTensorflowTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!hasCameraPermission()) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA), 0
            )
        }
        setContent {
            NavDrawer()


        }
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavDrawer(){
    val navigationController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    //val context = LocalContext.current.applicationContext
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = { 
            ModalDrawerSheet {
                Box(modifier = Modifier
                    .background(GreenJC)
                    .fillMaxWidth()
                    .height(150.dp)){
                    Text(text = "")
                }
                Divider()
                NavigationDrawerItem(label = { Text(text = "Identificar Semente", color = GreenJC) }, selected = false, icon = { Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Classificar Sementes", tint = GreenJC
                )}, onClick = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                    navigationController.navigate(Screens.Home.screen){
                        popUpTo(0)
                    }
                })
                NavigationDrawerItem(label = { Text(text = "Enviar Fotos", color = GreenJC) }, selected = false, icon = { Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Enviar Fotos", tint = GreenJC
                )}, onClick = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                    navigationController.navigate(Screens.Photos.screen){
                        popUpTo(0)
                    }
                })
            }
        },
        ) {

        Scaffold(topBar = {
            val coroutineScope  = rememberCoroutineScope()
            TopAppBar(title = { Text(text = "Sementes")},
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = GreenJC,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                   IconButton(onClick = {
                       coroutineScope.launch {
                           drawerState.open()
                       }
                   }) {
                       Icon(
                          Icons.Rounded.Menu, contentDescription = "MenuButton"
                       )
                   }
                },
                )
        }) {
                NavHost(navController = navigationController, startDestination = Screens.Home.screen ){
                    composable(Screens.Home.screen){Home()}
                    composable(Screens.Photos.screen){Photos()}

                }
        }
        
    }
}


