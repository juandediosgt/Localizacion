package com.jdl.localizacion;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.jdl.localizacion.databinding.ActivityMapsBinding;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    Button btnGPS;
    private double slatitud;
    private double slongitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // btn
        btnGPS=(Button)findViewById(R.id.btnobtLocation);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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


        btnGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crea un objeto de tipo LocationMangaer con un location Listener, que cuando cambie el tatus, la ubicación,cuando el proveedor esté habilitado o desabilitado
                // Acquire a reference to the system Location Manager
                LocationManager locationManager = (LocationManager) MapsActivity.this.getSystemService(Context.LOCATION_SERVICE);

                // Define a listener that responds to location updates
                LocationListener locationListener = new LocationListener() {
                    //ubicación
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void onLocationChanged(Location location) {
                        // Called when a new location is found by the network location provider.
                        Connection conn = null;
                        PreparedStatement pst = null;

                        slatitud = location.getLatitude();
                        slongitud = location.getLongitude();
                        Antut(googleMap);
                        //txtVUbicacion.setText(""+latitud+""+longitud);
                        // AQUI COLOCAREMOS DONDE SE GUARDA LA UBICACIÓ

                        String sql = "insert into ubicacion (latitud, longitud) values('"+ slatitud +"', '" +slongitud+"')";

                        try {
                            Conexion conexion = new Conexion();
                            conn = conexion.connect();

                            pst = conn.prepareStatement(sql);
                            pst.executeUpdate();
                            Toast.makeText(getApplicationContext(),"Registro Exitoso",Toast.LENGTH_SHORT).show();

                        }catch (Exception e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                        finally
                        {
                            try
                            {
                                if (conn!=null) {
                                    conn.close();
                                }
                                if (pst!=null) {
                                    pst.close();
                                }
                            } catch (SQLException e)
                            {
                                e.printStackTrace();

                            }
                        }
                    }

                    //Estatus
                    public void onStatusChanged(String provider, int status, Bundle extras) {}
                    //Proveedor Habilitado
                    public void onProviderEnabled(String provider) {}
                    //Proveedor Deshabilitado
                    public void onProviderDisabled(String provider) {}
                };
                int permissionCheck  = ContextCompat.checkSelfPermission
                        (MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

            }
        });

        // Leemos el permiso
        //Creamos una variable entera que leerá si tenemos o no permiso validando si está aprobado o denegado.
        int permissionCheck  = ContextCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION);
        //Si el permiso está denegado entonces pregunte si requiere mostrar un mensaje aparte para solicitud del servicio
        if(permissionCheck== PackageManager.PERMISSION_DENIED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION))
            {

            }
            else{
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        1 );
            }
        }



    }

    public void Antut (GoogleMap googleMap){
        mMap = googleMap;

        final LatLng punto1 = new LatLng(slatitud, slongitud);

        mMap.addMarker(new MarkerOptions().position(punto1).title("Ubicacion 1").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

    }
}