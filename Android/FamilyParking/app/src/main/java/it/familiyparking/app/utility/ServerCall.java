package it.familiyparking.app.utility;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;

import it.familiyparking.app.serverClass.Car;
import it.familiyparking.app.serverClass.Container;
import it.familiyparking.app.serverClass.IpoteticPark;
import it.familiyparking.app.serverClass.Result;
import it.familiyparking.app.serverClass.User;

/**
 * Created by francesco on 20/12/14.
 */
public class ServerCall {

    private static String server_address = Code.SERVER_PATH;

    public static Result signIn(User user){

        try {
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(server_address+"registration");

            Gson gson = new Gson();
            String json = gson.toJson(user);

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() != 200){
                return new Result(false,Double.parseDouble(Integer.toString(httpResponse.getStatusLine().getStatusCode())),"Server not available");
            }

            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());

            Result result = gson.fromJson(reader,Result.class);

            return result;

        } catch(Exception e){
            Log.e("signIn", e.toString() + " - " + e.getLocalizedMessage());
        }

        return null;
    }

    public static Result confirmation(User user){

        try {
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(server_address+"confirmCode");

            Container container = new Container(user);

            Gson gson = new Gson();
            String json = gson.toJson(container);

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() != 200){
                return new Result(false,Double.parseDouble(Integer.toString(httpResponse.getStatusLine().getStatusCode())),"Server not available");
            }

            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());

            Result result = gson.fromJson(reader,Result.class);

            return result;

        } catch(Exception e){
            Log.e("confirmation", e.toString() + " - " + e.getLocalizedMessage());
        }

        return null;
    }

    public static Result updateGoogleCode(User user){

        try {
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(server_address+"updateGoogleCode");

            Container container = new Container(user);

            Gson gson = new Gson();
            String json = gson.toJson(container);

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() != 200){
                return new Result(false,Double.parseDouble(Integer.toString(httpResponse.getStatusLine().getStatusCode())),"Server not available");
            }

            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());

            Result result = gson.fromJson(reader,Result.class);

            return result;

        } catch(Exception e){
            Log.e("signIn", e.toString() + " - " + e.getLocalizedMessage());
        }

        return null;
    }

    public static Result getAllCar(User user){

        try {
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(server_address+"getAllCars");

            Container container = new Container(user);

            Gson gson = new Gson();
            String json = gson.toJson(container);

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() != 200){
                return new Result(false,Double.parseDouble(Integer.toString(httpResponse.getStatusLine().getStatusCode())),"Server not available");
            }

            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());

            Result result = gson.fromJson(reader,Result.class);

            if(result.isFlag()){
                ArrayList<Car> list = new ArrayList<>();

                JSONArray jsonArray = new JSONArray(gson.toJson(result.getObject()));

                for(int i=0; i < jsonArray.length(); i++){
                    list.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Car.class));
                }

                result.setObject(list);
            }

            return result;

        } catch(Exception e){
            Log.e("getAllCar", e.toString() + " - " + e.getLocalizedMessage());
        }

        return null;

    }

    public static Result createCar(User user, Car car){

        try {
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(server_address+"createCar");

            Container container = new Container(user,car);

            Gson gson = new Gson();
            String json = gson.toJson(container);

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() != 200){
                return new Result(false,Double.parseDouble(Integer.toString(httpResponse.getStatusLine().getStatusCode())),"Server not available");
            }

            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());

            Result result = gson.fromJson(reader,Result.class);

            return result;

        } catch(Exception e){
            Log.e("createCar", e.toString() + " - " + e.getLocalizedMessage());
        }

        return null;

    }

    public static Result updateCar(User user,Car car){

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(server_address+"editCar");

            Container container = new Container(user,car);

            Gson gson = new Gson();
            String json = gson.toJson(container);

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                return new Result(false,Double.parseDouble(Integer.toString(httpResponse.getStatusLine().getStatusCode())),"Server not available");
            }

            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());

            Result result = gson.fromJson(reader,Result.class);

            return result;

        } catch(Exception e){
            Log.e("updateCar", e.toString() + " - " + e.getLocalizedMessage());
        }

        return null;
    }

    public static Result addCarUsers(User user,Car car, ArrayList<User> toAdd){

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(server_address+"insertContactCar");

            Car temp_car = new Car();
            temp_car.setId(car.getId());
            temp_car.setName(car.getName());
            temp_car.setUsers(toAdd);

            Container container = new Container(user,temp_car);
            Gson gson = new Gson();
            String json = gson.toJson(container);

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                return new Result(false,Double.parseDouble(Integer.toString(httpResponse.getStatusLine().getStatusCode())),"Server not available");
            }

            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());

            Result result = gson.fromJson(reader,Result.class);

            return result;

        } catch(Exception e){
            Log.e("addCarUsers", e.toString() + " - " + e.getLocalizedMessage());
        }

        return null;
    }

    public static Result removeCarUsers(User user,Car car, ArrayList<User> toRemove){

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(server_address+"removeContactCar");

            Car temp_car = new Car();
            temp_car.setId(car.getId());
            temp_car.setName(car.getName());
            temp_car.setUsers(toRemove);

            Container container = new Container(user,temp_car);
            Gson gson = new Gson();
            String json = gson.toJson(container);

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() != 200){
                return new Result(false,Double.parseDouble(Integer.toString(httpResponse.getStatusLine().getStatusCode())),"Server not available");
            }

            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());

            Result result = gson.fromJson(reader,Result.class);

            return result;

        } catch(Exception e){
            Log.e("removeCarUsers", e.toString() + " - " + e.getLocalizedMessage());
        }

        return null;
    }

    public static Result removeCar(User user,Car car){

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(server_address+"deleteCar");

            Container container = new Container(user,car);
            Gson gson = new Gson();
            String json = gson.toJson(container);

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() != 200){
                return new Result(false,Double.parseDouble(Integer.toString(httpResponse.getStatusLine().getStatusCode())),"Server not available");
            }

            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());

            Result result = gson.fromJson(reader,Result.class);

            return result;

        } catch(Exception e){
            Log.e("removeCar", e.toString() + " - " + e.getLocalizedMessage());
        }

        return null;
    }

    public static Result parkCar(User user,Car car){

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(server_address+"updatePosition");

            Container container = new Container(user,car);
            Gson gson = new Gson();
            String json = gson.toJson(container);

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() != 200){
                return new Result(false,Double.parseDouble(Integer.toString(httpResponse.getStatusLine().getStatusCode())),"Server not available");
            }

            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());

            Result result = gson.fromJson(reader,Result.class);

            return result;

        } catch(Exception e){
            Log.e("parkCar", e.toString() + " - " + e.getLocalizedMessage());
        }

        return null;
    }

    public static Result occupyCar(User user,Car car){

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(server_address+"pickCar");

            Container container = new Container(user,car);
            Gson gson = new Gson();
            String json = gson.toJson(container);

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() != 200){
                return new Result(false,Double.parseDouble(Integer.toString(httpResponse.getStatusLine().getStatusCode())),"Server not available");
            }

            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());

            Result result = gson.fromJson(reader,Result.class);

            return result;

        } catch(Exception e){
            Log.e("occupyCar", e.toString() + " - " + e.getLocalizedMessage());
        }

        return null;
    }

    public static Result isNotification(IpoteticPark ipoteticPark){

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(server_address+"getNotification");

            Gson gson = new Gson();
            String json = gson.toJson(ipoteticPark);

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() != 200){
                return new Result(false,Double.parseDouble(Integer.toString(httpResponse.getStatusLine().getStatusCode())),"Server not available");
            }

            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());

            Result result = gson.fromJson(reader,Result.class);

            return result;

        } catch(Exception e){
            Log.e("isNotification", e.toString() + " - " + e.getLocalizedMessage());
        }

        return null;
    }

    private static Result debug(){
        sleep();
        return new Result(true,"DEBUG","Debug");
    }

    private static void printReturn(InputStreamReader reader){
        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(reader, writer);
        }
        catch (Exception e){

        }
        String theString = writer.toString();
        Log.e("Result from Server",theString);
    }

    private static void sleep(){
        try {
            Thread.sleep(1000);
        }
        catch (Exception e){
            Log.e("Sleep",e.getMessage());
        }
    }

}
