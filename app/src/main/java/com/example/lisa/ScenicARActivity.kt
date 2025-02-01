package com.example.lisa

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode

class ScenicARActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scenic_ar)

        val arSceneView = findViewById<ARSceneView>(R.id.scenicARSceneView)

        // Retrieve the model URI passed from StressActivity
        val modelPath = intent.getStringExtra("MODEL_URI") ?: "models/scenic_view.glb"

        arSceneView.apply {
            planeRenderer.isVisible = true

            // Load the scenic model using ModelNode
            val modelNode = ModelNode(
                context = this@ScenicARActivity,
                modelFileLocation = modelPath,
                scaleToUnits = 1.0f  // Adjust the scale if needed for scenic views
            ).apply {
                position = Position(0.0f, 0.0f, -2.0f)  // Position the scenic view further back
            }

            addChild(modelNode)  // Add the model to the AR scene
        }
    }
}
