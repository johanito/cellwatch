package com.example.niden.cellwatchsharing.adapters;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.niden.cellwatchsharing.R;
import com.example.niden.cellwatchsharing.database.FirebaseUserEntity;
import com.example.niden.cellwatchsharing.database.LocationDatabase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import static com.example.niden.cellwatchsharing.activities.TechnicianActivity.mUserKey;
import static com.example.niden.cellwatchsharing.fragments.MapFragment.mapFrag;

/**
 * Created by niden on 07-Feb-18.
 */

public class GoogleMapAdapter extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap= googleMap;


        final DatabaseReference locationRef= rootRef.child("location");
        locationRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final LocationDatabase locationDatabase = dataSnapshot.getValue(LocationDatabase.class );
                DatabaseReference usersRef = rootRef.child("users");
                usersRef.orderByChild("id").equalTo(locationDatabase.getEachUserID()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        FirebaseUserEntity userDatabase = dataSnapshot.getValue(FirebaseUserEntity.class );
                        String imageUrl = userDatabase.getProfileUrl();
                        final String nameOfTechnician = userDatabase.getName();
                        Glide.with(mapFrag).asBitmap().load(imageUrl).into(new SimpleTarget<Bitmap>(55,55) {
                            @Override
                            public void onResourceReady(@NonNull Bitmap bmpProfileImage, @Nullable Transition<? super Bitmap> transition) {
                                LatLng targetLocation = new LatLng(locationDatabase.getLatitude(), locationDatabase.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(targetLocation)
                                        .snippet("Technician: "+nameOfTechnician)
                                        .icon(BitmapDescriptorFactory.fromBitmap(bmpProfileImage))
                                        .title(locationDatabase.getAddress()));
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(targetLocation)
                                        .zoom(16)
                                        .bearing(10)
                                        .tilt(80)
                                        .build();
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            }
                        });
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
