package it.familiyparking.app.task;

import android.database.sqlite.SQLiteDatabase;
import android.os.Looper;
import android.widget.Toast;

import it.familiyparking.app.FPApplication;
import it.familiyparking.app.MainActivity;
import it.familiyparking.app.R;
import it.familiyparking.app.dao.CarTable;
import it.familiyparking.app.serverClass.Car;
import it.familiyparking.app.serverClass.Result;
import it.familiyparking.app.serverClass.User;
import it.familiyparking.app.utility.ServerCall;
import it.familiyparking.app.utility.Tools;

/**
 * Created by francesco on 02/01/15.
 */
public class DoPark implements Runnable {

    private MainActivity activity;
    private Car car;
    private User user;
    private boolean flag;

    public DoPark(MainActivity activity, User user, Car car) {
        this.activity = activity;
        this.car = car;
        this.user = user;
        this.flag = true;
    }

    @Override
    public void run() {
        Looper.prepare();

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setProgressDialogCircular(activity.getResources().getString(R.string.park_car));
            }
        });

        if(Tools.isOnline(activity)) {

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    car.setLatitude(activity.getLatitude());
                    car.setLongitude(activity.getLongitude());
                    updateFlag(false);
                }
            });

            while (flag) ;

            car.setTimestamp(Tools.getTimestamp());
            car.setLast_driver(user.getEmail());

            final Result result = ServerCall.parkCar(user, car);

            if (result.isFlag()) {

                SQLiteDatabase db = Tools.getDB_Writable(activity);
                car.setParked(true);
                CarTable.updateCar(db,car);
                ((FPApplication) activity.getApplication()).setCars(CarTable.getAllCar(db));
                db.close();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Tools.createToast(activity, activity.getResources().getString(R.string.parked_car), Toast.LENGTH_SHORT);
                        activity.park(car, true);
                        activity.resetProgressDialogCircular(true);
                        activity.resetCarDetail();
                        activity.resetCar();
                    }
                });
            } else {
                Tools.manageServerError(result, activity);
            }

        }
        else{
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.endNoConnection();
                }
            });
        }
    }

    private void updateFlag(boolean f){
        this.flag = f;
    }
}
