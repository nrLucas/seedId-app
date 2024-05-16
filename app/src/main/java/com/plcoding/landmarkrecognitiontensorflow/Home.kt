package com.plcoding.landmarkrecognitiontensorflow

import android.util.Log
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.plcoding.landmarkrecognitiontensorflow.data.TfLiteLandmarkClassifier
import com.plcoding.landmarkrecognitiontensorflow.domain.Classification
import com.plcoding.landmarkrecognitiontensorflow.presentation.CameraPreview
import com.plcoding.landmarkrecognitiontensorflow.presentation.LandmarkImageAnalyzer
import com.plcoding.landmarkrecognitiontensorflow.ui.theme.LandmarkRecognitionTensorflowTheme

@Composable
fun Home() {
    val applicationContext = LocalContext.current
    var usePopularModel by remember { mutableStateOf(false) }
    val modelType = if (usePopularModel) "popular" else "científico"

    var classifications by remember { mutableStateOf(emptyList<Classification>()) }
    var analyzer by remember { mutableStateOf<LandmarkImageAnalyzer?>(null) }

    LaunchedEffect(modelType) {
        analyzer = LandmarkImageAnalyzer(
            classifier = TfLiteLandmarkClassifier(
                context = applicationContext,
                modelType = modelType
            ),
            onResults = {
                classifications = it
                Log.d("Teste Sementes", "Classifications: $it")
            }
        )
    }

    val controller = remember {
        LifecycleCameraController(applicationContext).apply {
            setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
        }
    }

    LaunchedEffect(analyzer) {
        analyzer?.let {
            controller.setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(applicationContext),
                it
            )
        }
    }

    LandmarkRecognitionTensorflowTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CameraPreview(controller, Modifier.fillMaxSize())

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                classifications.forEach {
                    val formattedScore = String.format("%.1f%%", it.score * 100)

                    Text(
                        text = "${it.name} - Acurácia: $formattedScore",
                        style = TextStyle(
                            // fontStyle = FontStyle.Italic,  // Aplica itálico
                            fontSize = 16.sp  // Define o tamanho da fonte, ajuste conforme necessário
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(8.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Button(onClick = { usePopularModel = !usePopularModel }) {
                    Text(text = if (usePopularModel) "Nome Popular" else "Nome Científico")
                }
            }
        }
    }
}
