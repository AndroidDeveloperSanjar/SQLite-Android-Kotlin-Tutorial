package com.example.sqlitekotlin

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sqlitekotlin.Utils.Constants
import com.example.sqlitekotlin.db.DBHelper
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_add_update_record.*

class AddUpdateRecordActivity : AppCompatActivity() {

    private var actionBar: ActionBar? = null

    private lateinit var cameraPermissions: Array<String>
    private lateinit var storagePermissions: Array<String>

    private var imageUri: Uri? = null

    private var name = ""
    private var phone = ""
    private var email = ""
    private var dob = ""
    private var bio = ""

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_update_record)

        init()
    }

    private fun init(){
        initActionBar()
        initPermissions()
        setOnClickListener()
    }

    private fun initActionBar() {
        actionBar = supportActionBar
        actionBar!!.title = "Add Record"
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setDisplayShowCustomEnabled(true)
    }

    private fun initPermissions(){
        cameraPermissions = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        storagePermissions = arrayOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    private fun setOnClickListener(){
        ivProfile.setOnClickListener {
            imagePickDialog()
        }

        btnSave.setOnClickListener {
            inputData()
        }
    }

    private fun inputData(){
        name += etName.text?.trim()
        phone += etPhone.text?.trim()
        email += etEmail.text?.trim()
        dob += etDob.text?.trim()
        bio += etBio.text?.trim()

        dbHelper = DBHelper(this)

        val timeStamp = System.currentTimeMillis()

        dbHelper.insertRecord(
            name = name,
            phone = phone,
            email = email,
            dob = dob,
            bio = bio,
            image = "$imageUri",
            addedTime = "$timeStamp",
            updatedTime = "$timeStamp"

        )

        Toast.makeText(this,"Record added against ID ${Constants.C_ID}",Toast.LENGTH_SHORT).show()
    }

    private fun imagePickDialog(){
        val options = arrayOf("Camera", "Gallery")
        val builder =  AlertDialog.Builder(this)
        builder.setTitle("Pick Image From")
        builder.setItems(options){dialog, which ->
            when(which){
                0 -> {
                    if (!checkCameraPermissions()){
                        requestCameraPermission()
                    }else {
                        pickFromCamera()
                    }
                }
                1 -> {
                    if (!checkStoragePermissions()){
                        requestStoragePermission()
                    }else {
                        pickFromGallery()
                    }
                }
            }
        }
        builder.show()
    }

    private fun checkCameraPermissions(): Boolean{
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission(){
        ActivityCompat.requestPermissions(
            this,
            cameraPermissions,
            Constants.CAMERA_REQUEST_CODE
        )
    }

    private fun pickFromCamera(){
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE,"Image Title")
        values.put(MediaStore.Images.Media.DESCRIPTION,"Image Description")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
        startActivityForResult(
            cameraIntent,
            Constants.IMAGE_PICK_CAMERA_CODE
        )
    }

    private fun checkStoragePermissions(): Boolean{
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission(){
        ActivityCompat.requestPermissions(
            this,
            storagePermissions,
            Constants.STORAGE_REQUEST_CODE
        )
    }

    private fun pickFromGallery(){
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(
            galleryIntent,
            Constants.IMAGE_PICK_GALLERY_CODE
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            Constants.CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()){
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if (cameraAccepted && storageAccepted)
                        pickFromCamera()
                    else
                        Toast.makeText(this,"Camera and Storage permissions are required",Toast.LENGTH_SHORT).show()
                }
            }
            Constants.STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()){
                    val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (storageAccepted)
                        pickFromGallery()
                    else
                        Toast.makeText(this,"Storage permission is required",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK){
            if (requestCode == Constants.IMAGE_PICK_GALLERY_CODE){
                CropImage.activity(data!!.data)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this)
            }else if (requestCode == Constants.IMAGE_PICK_CAMERA_CODE){
                CropImage.activity(data!!.data)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this)
            }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK){
                    val resultUri = result.uri
                    imageUri = resultUri
                    ivProfile.setImageURI(resultUri)
                }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    val  error = result.error
                    Toast.makeText(this,"$error",Toast.LENGTH_SHORT).show()
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
