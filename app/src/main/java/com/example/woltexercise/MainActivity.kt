package com.example.woltexercise

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.woltexercise.data.Results
import com.example.woltexercise.data.UIResponse
import com.example.woltexercise.data.Venues
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this,
            MainViewModelFactory()
        ).get(MainViewModel::class.java)

        invokeLocationAction()
    }

    private fun getVenues(){
        viewModel.getVenues().observe(this, Observer { response ->
            when(response) {
                is UIResponse.Loading -> {
                    progressBar.show()
                }
                is UIResponse.Data<Venues> -> {
                    progressBar.setGone()
                    showVenues(response.data)
                }
                is UIResponse.Error -> {
                    progressBar.setGone()
                }
            }
        })
    }

    private fun showVenues(venues: Venues){
        //TODO: Refresh the adapter here instead of making new one every time
        val places = venues.results
        val adapter = object : GenericAdapter<Results>(places) {
            override fun getLayoutId(position: Int, obj: Results): Int {
                return R.layout.venue_list_item
            }
            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                return VenuesViewHolder(view)
            }
        }
        venuesRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        venuesRv.adapter = adapter
    }

    private fun invokeLocationAction() {
        when {
            isPermissionsGranted() -> getVenues()
            shouldShowRequestPermissionRationale() -> showPermissionsRequiredMessage()
            else -> requestLocationPermission()
        }
    }

    private fun requestLocationPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_REQUEST
        )
    }

    private fun isPermissionsGranted() =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED


    private fun shouldShowRequestPermissionRationale(): Boolean{
        val rationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) || ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (!rationale && viewModel.getFirstTimePermissionRequested()){
            viewModel.saveFirstTimePermissionRequested()
        } else if(!rationale && !viewModel.getFirstTimePermissionRequested()){
            return true
        }
        return false
    }


    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                invokeLocationAction()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == APP_SETTINGS_REQUEST)
            invokeLocationAction()
    }

    companion object {
        const val LOCATION_REQUEST = 100
        const val APP_SETTINGS_REQUEST = 101
    }

    private fun launchApplicationSettingsPage(){
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent,
            APP_SETTINGS_REQUEST
        )
    }

    private fun showPermissionsRequiredMessage(){
        MaterialAlertDialogBuilder(this)
            .setMessage(getString(R.string.location_permission_required_message))
            .setPositiveButton(getString(R.string.grant_permission)){ dialog, _ ->
                dialog.dismiss()
                launchApplicationSettingsPage()
            }
            .setNegativeButton(getString(android.R.string.cancel)){ dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }
}
