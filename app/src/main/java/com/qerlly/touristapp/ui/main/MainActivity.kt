package com.qerlly.touristapp.ui.main

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.material.composethemeadapter.MdcTheme
import com.qerlly.touristapp.R
import com.qerlly.touristapp.services.SettingsService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import android.graphics.Bitmap
import android.location.LocationManager
import java.io.ByteArrayOutputStream
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.google.firebase.storage.UploadTask
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.FirebaseStorage
import com.qerlly.touristapp.repositories.UserSettingsRepository
import com.qerlly.touristapp.services.UserAuthService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    @Inject
    lateinit var settingsService: SettingsService

    @Inject
    lateinit var userSettingsRepository: UserSettingsRepository

    @Inject
    lateinit var firebaseStorage: FirebaseStorage

    @Inject
    lateinit var userAuthService: UserAuthService

    @Inject
    lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainScreen() }
    }

    private fun startRoadmapActivity() {
        startActivity(Intent(this, RoadmapActivity::class.java))
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
           Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            firebaseUploadBitmap(imageBitmap)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun firebaseUploadBitmap(photo: Bitmap) {
        val stream = ByteArrayOutputStream()
        photo.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val data = stream.toByteArray()
        val imageStorage: StorageReference = firebaseStorage.reference
        val imageRef = imageStorage.child(generateNameString())

        imageRef.putBytes(data).continueWithTask { task: Task<UploadTask.TaskSnapshot?> ->
            if (!task.isSuccessful) {
                Toast.makeText(this, task.exception!!.toString(), Toast.LENGTH_SHORT).show()
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task: Task<Uri> ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                runBlocking { settingsService.setPhoto(downloadUri.toString()) }
            }
        }
    }

    @Composable
    fun MainScreen() = MdcTheme {
        val navController = rememberNavController()

        val progressState = remember { mutableStateOf(true)}

        val navState = navController.currentBackStackEntryFlow.collectAsState(null)

        val currentRoute = navState.value?.destination?.route

        Box(Modifier.fillMaxSize()) {
            if (progressState.value) CircularProgressIndicator(Modifier.align(Alignment.Center))
            else {
                val isJoined = settingsService.getTour().collectAsState("")

                val isGid: Boolean = userAuthService.isUserGid()

                Scaffold(
                    topBar = { MainAppBar(navController) },
                    content = { MainNavGraph(navController, isJoined, this@MainActivity::dispatchTakePictureIntent, isGid) },
                    bottomBar = {
                        if (currentRoute != Destinations.USER_SCREEN && currentRoute != Destinations.FAQ_SCREEN)
                            MainBottomBar(navController, this@MainActivity::startRoadmapActivity, isJoined, locationManager)
                    }
                )
            }
        }

        lifecycleScope.launch {
            userAuthService.userId?.let {
                val user = userSettingsRepository.getByUserID(it).first()
                settingsService.setTour(user?.tour ?: "")
                progressState.value = false
            }
        }
    }

    private fun generateNameString(): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..54)
            .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("");
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }
}