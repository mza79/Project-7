package it.familiyparking.app.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;

import it.familiyparking.app.MainActivity;
import it.familiyparking.app.R;
import it.familiyparking.app.serverClass.Car;
import it.familiyparking.app.utility.Tools;

/**
 * Created by francesco on 02/01/15.
 */
public class CustomAdapterCar extends ArrayAdapter<Car> {

    private MainActivity activity;
    private View.OnClickListener listener;

    public CustomAdapterCar(Activity activity, ArrayList<Car> list) {
        super(activity.getApplicationContext(), 0, list);
        this.activity = (MainActivity) activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.car_item, parent, false);
        }

        final Car car = getItem(position);

        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setCarDetail(car);
            }
        };

        setRoot(convertView,car);
        setBrand(convertView,car);
        setName(convertView,car);
        setRegister(convertView,car);
        setPark(convertView,car);
        setBluetooth(convertView,car);
        setContactList(convertView,car);
        setDetailButton(convertView,car);
        setMarkerColor(convertView,car);

        return convertView;
    }

    private void setRoot(View convertView, Car car){
        RelativeLayout rootview = (RelativeLayout) convertView.findViewById(R.id.car_item_root);
        rootview.setOnClickListener(listener);
    }

    private void setBrand(View convertView, Car car){
        ImageView brand = (ImageView) convertView.findViewById(R.id.car_brand_iv);
        brand.setBackgroundDrawable(activity.getResources().getDrawable(activity.getResources().getIdentifier(car.getBrand(), "drawable", activity.getPackageName())));
        brand.setOnClickListener(listener);
    }

    private void setName(View convertView, Car car){
        TextView name = (TextView) convertView.findViewById(R.id.car_name_tv);
        name.setText(car.getName());
        name.setOnClickListener(listener);
    }

    private void setRegister(View convertView, Car car) {
        TextView register = (TextView) convertView.findViewById(R.id.car_register_tv);
        if(car.getRegister() != null) {
            register.setText(car.getRegister());
            register.setVisibility(View.VISIBLE);
            register.setOnClickListener(listener);
        }
        else{
            register.setVisibility(View.GONE);
        }
    }

    private void setBluetooth(View convertView, Car car) {
        if(car.getBluetoothMac() != null) {
            View signal = convertView.findViewById(R.id.bluetooth_circle_ok);
            signal.setVisibility(View.VISIBLE);
            signal.setOnClickListener(listener);

            convertView.findViewById(R.id.bluetooth_circle_ko).setVisibility(View.GONE);
        }
        else {
            convertView.findViewById(R.id.bluetooth_circle_ok).setVisibility(View.GONE);

            View signal = convertView.findViewById(R.id.bluetooth_circle_ko);
            signal.setVisibility(View.VISIBLE);
            signal.setOnClickListener(listener);
        }
    }

    private void setPark(View convertView, Car car) {
        if(car.isParked()) {
            View signal = convertView.findViewById(R.id.park_circle_ok);
            signal.setVisibility(View.VISIBLE);
            signal.setOnClickListener(listener);

            convertView.findViewById(R.id.park_circle_ko).setVisibility(View.GONE);
        }
        else {
            convertView.findViewById(R.id.park_circle_ok).setVisibility(View.GONE);

            View signal = convertView.findViewById(R.id.park_circle_ko);
            signal.setVisibility(View.VISIBLE);
            signal.setOnClickListener(listener);
        }
    }

    private void setContactList(View convertView, final Car car) {
        TwoWayView contact_list = (TwoWayView) convertView.findViewById(R.id.group_list);
        CustomHorizontalAdapter_4CarItem customHorizontalAdapter = new CustomHorizontalAdapter_4CarItem(activity,car.getUsers());
        contact_list.setAdapter(customHorizontalAdapter);

        contact_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activity.setCarDetail(car);
            }
        });
    }

    private void setDetailButton(View convertView, final Car car){
        Button details_button = (Button) convertView.findViewById(R.id.car_arrow_iv);
        details_button.setOnClickListener(listener);
    }

    private void setMarkerColor(View convertView, Car car){
        RelativeLayout marker_color_rl = (RelativeLayout)convertView.findViewById(R.id.marker_color_rl);
        marker_color_rl.setVisibility(View.VISIBLE);
        marker_color_rl.setBackgroundColor(Tools.convertMarkerColor(car.getMarkerColor().floatValue()));
    }
}