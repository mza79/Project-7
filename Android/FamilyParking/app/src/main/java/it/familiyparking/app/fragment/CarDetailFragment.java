package it.familiyparking.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import it.familiyparking.app.FPApplication;
import it.familiyparking.app.MainActivity;
import it.familiyparking.app.R;
import it.familiyparking.app.adapter.CustomAdapterContact;
import it.familiyparking.app.serverClass.Car;
import it.familiyparking.app.serverClass.User;
import it.familiyparking.app.task.AsyncTaskLocationMap;
import it.familiyparking.app.task.DoPark;
import it.familiyparking.app.task.DoUnpark;
import it.familiyparking.app.utility.Tools;


/**
 * Created by francesco on 15/01/15.
 */
public class CarDetailFragment extends Fragment{

    private MainActivity activity;
    private View rootView;
    private User user;
    private Car car;
    private GoogleMap googleMap;

    public CarDetailFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_car_detail, container, false);

        activity = (MainActivity) getActivity();

        user = ((FPApplication) activity.getApplication()).getUser();

        Tools.setUpButtonActionBar(activity);

        setData();

        return rootView;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.car = args.getParcelable("car");
    }

    @Override
    public void onResume() {
        super.onResume();

        activity.hideMyPosition();

        if(activity.setTitleNameCar())
            Tools.setTitleActionBar(activity,car.getName());
    }

    public void updateCarPosition() {
        if((googleMap != null) && (car.isParked())){
            googleMap.clear();
            setMap();
        }
    }

    private void setData(){
        setMap();
        setCarInfo(rootView);
        setParking(rootView);
        setConatcts(rootView);
        setBluetooth(rootView);
        setEditCar(rootView);
        setParkCar(rootView);
        setOccupyCar(rootView);
    }

    private void setMap(){
        if (googleMap == null) {
            googleMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_car)).getMap();
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.getUiSettings().setAllGesturesEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);

            if(car.isParked()) {
                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        activity.setFixPositionMarker(car);
                    }
                });
            }
        }

        if(car.isParked()){
            LatLng carPosition = new LatLng(Double.parseDouble(car.getLatitude()),Double.parseDouble(car.getLongitude()));

            googleMap.addMarker(new MarkerOptions().position(carPosition).title(car.getName()).icon(BitmapDescriptorFactory.defaultMarker(car.getMarkerColor())));

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(carPosition.latitude,carPosition.longitude), 18.0f));
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    activity.resetCarDetail();
                    activity.resetCar();
                    activity.moveCamera(marker.getPosition());
                    return true;
                }
            });
        }
        else{
            googleMap.setMyLocationEnabled(true);
            new AsyncTaskLocationMap().execute(googleMap, getActivity(),true, null);
        }
    }

    private void setCarInfo(View rootView){
        ImageView brand = (ImageView) rootView.findViewById(R.id.car_brand_iv);
        brand.setBackgroundDrawable(activity.getResources().getDrawable(activity.getResources().getIdentifier(car.getBrand(),"drawable",activity.getPackageName())));

        ((TextView) rootView.findViewById(R.id.car_name_tv)).setText(car.getName());
        ((TextView) rootView.findViewById(R.id.car_register_tv)).setText(car.getRegister());


        RelativeLayout marker_color = (RelativeLayout) rootView.findViewById(R.id.marker_color_detail_rl);
        marker_color.setVisibility(View.VISIBLE);
        marker_color.setBackgroundColor(Tools.convertMarkerColor(car.getMarkerColor().floatValue()));
    }

    private void setParking(View rootView){
        TextView nameDriver = (TextView) rootView.findViewById(R.id.last_driver_name_tv);

        if(car.isParked()){
            User lastDriver = car.getLastDriverUser(activity);
            if(lastDriver.equals(user))
                nameDriver.setText(activity.getResources().getString(R.string.you));
            else
                nameDriver.setText(Tools.formatedName(car.getLastDriverUser(activity).getName()));

            ((TextView)rootView.findViewById(R.id.last_driver_time_tv)).setText(Tools.getFormatedData(car.getTimestamp()));
            ((TextView)rootView.findViewById(R.id.last_driver_interval_tv)).setText(Tools.getIntervalDataServer(activity,car.getTimestamp()));
        }
        else{
            (rootView.findViewById(R.id.last_driver_time_tv)).setVisibility(View.GONE);
            (rootView.findViewById(R.id.last_driver_interval_tv)).setVisibility(View.GONE);

            nameDriver.setText(activity.getResources().getString(R.string.car_not_parked));
            nameDriver.setTextAppearance(activity,R.style.normalText);
            nameDriver.setTextColor(activity.getResources().getColor(R.color.dark_red));
        }
    }

    private void setConatcts(View rootView){
        ListView listContacts = ((ListView) rootView.findViewById(R.id.contact_list));

        listContacts.setAdapter(new CustomAdapterContact(getActivity(), car.getUsers()));

        listContacts.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    private void setBluetooth(View rootView){
        if(car.getBluetoothMac() == null) {
            rootView.findViewById(R.id.bluetooth_relative).setVisibility(View.GONE);
        }
        else {
            rootView.findViewById(R.id.bluetooth_relative).setVisibility(View.VISIBLE);
            ((TextView) rootView.findViewById(R.id.bluetooth_name_tv)).setText(car.getBluetoothName());
            ((TextView) rootView.findViewById(R.id.bluetooth_address_tv)).setText(car.getBluetoothMac());
        }
    }

    private void setEditCar(View rootView){
        rootView.findViewById(R.id.edit_rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setModifyCar(car);
            }
        });
    }

    private void setParkCar(View rootView){
        rootView.findViewById(R.id.toPark_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new DoPark(activity,user,car)).start();
            }
        });
    }

    private void setOccupyCar(View rootView){
        Button unpark = (Button) rootView.findViewById(R.id.toUnPark);

        if(car.isParked()) {
            unpark.setVisibility(View.VISIBLE);
            unpark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new DoUnpark(activity, user, car)).start();
                }
            });
        }
        else{
            unpark.setVisibility(View.GONE);
        }
    }

    public void updateCar(Car car){
        this.car = car;
        setData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Tools.setTitleActionBar(activity, R.string.list_car);
    }
}
