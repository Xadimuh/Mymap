//package com.example.mymap;
//
//
//import com.google.android.gms.maps.GoogleMap;
//
//public class onMapReady {
//
//    public void onMapReady(GoogleMap googleMap) {
//        GoogleMap mMap = googleMap;
//
//        // Vérifier les permissions avant d'activer la localisation sur la carte
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            mMap.setMyLocationEnabled(true);
//
//            // Demander des mises à jour de localisation
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
//        } else {
//            // Demander les permissions si elles ne sont pas déjà accordées
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
//        }
//    }
//}