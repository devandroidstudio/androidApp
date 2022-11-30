package com.example.appanimals.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.appanimals.R;
import com.example.appanimals.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private ArrayList<LatLng> arrayList = new ArrayList<LatLng>();
    LatLng sydney = new LatLng(10.855764455400067, 106.7850597823739);
    LatLng vincom3mouth2 = new LatLng(10.801930512508385, 106.71442990431132);
    LatLng SaigonZoo = new LatLng(10.809861430656875, 106.71497408237383);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.MATCH_PARENT);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        arrayList.add(sydney);
        arrayList.add(vincom3mouth2);
        arrayList.add(SaigonZoo);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        for (int i = 0; i < arrayList.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(arrayList.get(i)).title("Marker"));
            CameraPosition cameraPosition = new CameraPosition.Builder().target(arrayList.get(i)).zoom(12).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mMap.setTrafficEnabled(true);

        }
    }
}