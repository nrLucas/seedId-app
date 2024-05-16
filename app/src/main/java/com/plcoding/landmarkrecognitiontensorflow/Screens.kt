package com.plcoding.landmarkrecognitiontensorflow

sealed class Screens (val screen: String){
     object Home: Screens("Home")
     object Photos: Screens("Photos")

}