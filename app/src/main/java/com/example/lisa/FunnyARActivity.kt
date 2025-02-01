package com.example.lisa

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode

class FunnyARActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funny_ar)

        val arSceneView = findViewById<ARSceneView>(R.id.funnyARSceneView)

        // Retrieve the model URI passed from StressActivity
        val modelPath = intent.getStringExtra("MODEL_URI") ?: "models/funny_character.glb"

        arSceneView.apply {
            planeRenderer.isVisible = true

            // Correctly load the model using ModelNode
            val modelNode = ModelNode(
                context = this@FunnyARActivity,
                modelFileLocation = modelPath,
                scaleToUnits = 0.5f
            ).apply {
                position = Position(0.0f, 0.0f, -1.0f)  // Positioning the model
            }

            addChild(modelNode)  // Adding the model to the AR scene
        }
    }
}
