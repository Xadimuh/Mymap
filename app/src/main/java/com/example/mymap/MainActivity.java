package com.example.mymap;
import android.telephony.SmsManager;
import android.view.View;

import android.widget.EditText;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.content.Context;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 101;
    private GoogleMap mMap;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Vérifier si les permissions sont déjà accordées
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Demander les permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Si les permissions sont déjà accordées
            startLocationUpdates();
        }
    }
    public void onMoveToLocation(View view) {
        EditText latitudeInput = findViewById(R.id.latitudeInput);
        EditText longitudeInput = findViewById(R.id.longitudeInput);

        String latitudeString = latitudeInput.getText().toString();
        String longitudeString = longitudeInput.getText().toString();

        if (!latitudeString.isEmpty() && !longitudeString.isEmpty()) {
            double latitude = Double.parseDouble(latitudeString);
            double longitude = Double.parseDouble(longitudeString);

            LatLng newLocation = new LatLng(latitude, longitude);

            // Effacer les marqueurs précédents
            mMap.clear();

            // Ajouter un marqueur à la nouvelle position
            mMap.addMarker(new MarkerOptions().position(newLocation).title("Marker in New Location"));

            // Déplacer la caméra vers le nouveau marqueur
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 15));
        } else {
            // Gérer le cas où l'un des champs est vide
            Toast.makeText(this, "Please enter both latitude and longitude", Toast.LENGTH_SHORT).show();
        }
    }
    public void sendSms(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }

    // Gérer les résultats des demandes de permission
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        // Votre logique de gestion des permissions
//    }


//    public void onMoveToLocation(View view) {
//        EditText latitudeInput = findViewById(R.id.latitudeInput);
//        EditText longitudeInput = findViewById(R.id.longitudeInput);
//
//        String latitudeString = latitudeInput.getText().toString();
//        String longitudeString = longitudeInput.getText().toString();
//
//        if (!latitudeString.isEmpty() && !longitudeString.isEmpty()) {
//            double latitude = Double.parseDouble(latitudeString);
//            double longitude = Double.parseDouble(longitudeString);
//
//            LatLng newLocation = new LatLng(latitude, longitude);
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 15));
//        } else {
//            // Gérer le cas où l'un des champs est vide
//            Toast.makeText(this, "Please enter both latitude and longitude", Toast.LENGTH_SHORT).show();
//        }
//    }


    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                // Afficher un message d'erreur ou gérer le cas où les permissions sont refusées
            }
        }
    }
    public void sendLocationSms(View view) {
        EditText phoneNumberInput = findViewById(R.id.phoneNumberInput);
        EditText latitudeInput = findViewById(R.id.latitudeInput);
        EditText longitudeInput = findViewById(R.id.longitudeInput);

        String phoneNumber = phoneNumberInput.getText().toString();
        String latitude = latitudeInput.getText().toString();
        String longitude = longitudeInput.getText().toString();

        if (!phoneNumber.isEmpty() && !latitude.isEmpty() && !longitude.isEmpty()) {
            String message = "Latitude: " + latitude + ", Longitude: " + longitude;

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);

            Toast.makeText(this, "SMS sent", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
        }
    }

    // Implement other LocationListener methods
}
