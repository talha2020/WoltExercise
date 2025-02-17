package com.example.woltexercise.venues

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
import com.example.woltexercise.*
import com.example.woltexercise.data.Place
import com.example.woltexercise.data.UIResponse
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: GenericAdapter<Place>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = getString(R.string.venues_nearby)

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
                is UIResponse.Data<List<Place>> -> {
                    progressBar.setGone()
                    showVenues(response.data)
                }
                is UIResponse.Error -> {
                    progressBar.setGone()
                    response.error.message?.let { showError(it) }
                }
            }
        })
    }

    private fun showVenues(places: List<Place>){
        if (::adapter.isInitialized){
            adapter.setItems(places)
        } else {
            adapter = object : GenericAdapter<Place>(places) {
                override fun getLayoutId(position: Int, obj: Place): Int {
                    return R.layout.venue_list_item
                }
                override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                    return VenuesViewHolder(view,
                        onFavoriteClicked = { position ->
                            adapter.getItem(position).apply { favourite = !favourite }
                            adapter.notifyItemChanged(position)
                            viewModel.setFavorite(adapter.getItem(position))
                        })
                }
            }
            venuesRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            venuesRv.adapter = adapter
        }
    }

    /* This is a bit of over engineering for the assignment as handling location permission was not really needed.
    *  But i wanted to play around with permission handling so implemented it here to mimic the real world scenario.
    *  Even if we are asking for permissions here, for the purpose of this assignment, we are using a mock array provided
    *  with the assignment.
    * */

    /* Also choose a very aggressive strategy here for asking permission. Assumed the scenario where
    *  the app cannot work without permission. Depending upon the use case we can handle it differently.
    * */
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
