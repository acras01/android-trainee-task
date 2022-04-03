package com.example.android_trainee_task

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.android_trainee_task.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var pinsObject: JSONPinsObject

    private var serviceItems = arrayListOf<ServiceItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fab: View = findViewById(R.id.fab)

        val objects = getObjectsFromJson(this)
        pinsObject = JSONPinsObject(objects)
        val services = pinsObject.services.toCollection(ArrayList())
        services.indices.forEach { i ->
            serviceItems.add(ServiceItem(services[i], true))
        }

        fab.setOnClickListener {
            intent = Intent(this.applicationContext, FilterActivity::class.java)
            intent.putParcelableArrayListExtra("services", serviceItems)
            getFilterResult.launch(intent)
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private val getFilterResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                serviceItems = it.data?.extras?.getParcelableArrayList("services")!!
            }
        }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        drawMarkers()
    }

    override fun onResume() {
        super.onResume()
        if (::mMap.isInitialized) {
            mMap.clear()
            drawMarkers()
        }
    }

    private fun drawMarkers() {
        val builder = LatLngBounds.builder()
        val markers: MutableList<Marker> = mutableListOf()
        val serviceCheckedItems = arrayListOf<String>()
        serviceItems.indices.forEach{ i ->
            if (serviceItems[i].checked)
                serviceCheckedItems.add(serviceItems[i].name!!)
        }
        if (serviceCheckedItems.size > 0) {
            // Add markers for pins
            for (pin in pinsObject.pins) {
                if (serviceCheckedItems.contains(pin.service)) {
                    val position = LatLng(pin.coordinates["lat"]!!, pin.coordinates["lng"]!!)
                    val marker = mMap.addMarker(
                        MarkerOptions()
                            .position(position)
                            .title("Marker " + pin.id)
                            .snippet("Service " + pin.service)
                    )!!
                    markers.add(marker)
                    builder.include(marker.position)
                }
            }
            val bounds = builder.build()
            val metrics = screenValue(this)
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, metrics[0], metrics[1], 200)
            mMap.moveCamera(cu)
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10f), 2000, null)
        }
    }
}