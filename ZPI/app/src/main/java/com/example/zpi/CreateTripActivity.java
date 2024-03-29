package com.example.zpi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zpi.data_handling.BaseConnection;
import com.example.zpi.data_handling.SharedPreferencesHandler;
import com.example.zpi.models.Trip;
import com.example.zpi.models.User;
import com.example.zpi.repositories.TripDao;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateTripActivity extends AppCompatActivity {
    EditText name;
    EditText description;
    EditText begin;
    EditText end;
    User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loggedUser = SharedPreferencesHandler.getLoggedInUser(getApplicationContext());
        name=findViewById(R.id.et_tripName);
        description=findViewById(R.id.et_tripDesc);
        begin=findViewById(R.id.et_tripBegin);
        end=findViewById(R.id.et_tripEnd);

    }

    public void createTrip(View v) throws ParseException {
        String tripname=name.getText().toString();
        String tripdescription=description.getText().toString();
        String sTripBegin=begin.getText().toString();
        String sTripEnd=end.getText().toString();

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dTripBegin=formatter.parse(sTripBegin);
        Date dTripEnd=formatter.parse(sTripEnd);


        if(tripname!=null && tripdescription!=null && sTripBegin!=null && sTripEnd!=null){
            if(dTripBegin.after(new Date())) {
                if(dTripBegin.before(dTripEnd)) {
                    new Thread(() -> {
                        try {
                            TripDao tripDao = new TripDao(BaseConnection.getConnectionSource());
                            Trip currentTrip = new Trip(tripname, tripdescription, dTripBegin, dTripEnd);
                            tripDao.createTrip(currentTrip, loggedUser);
                            Intent intent = new Intent(this, InviteUsersActivity.class);
                            intent.putExtra("CreateTrip", currentTrip);
                            startActivity(intent);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }).start();
                }else{
                    Toast.makeText(this, "Niezgodność dat!", Toast.LENGTH_SHORT).show();
                    end.getText().clear();
                }
            }else{
                Toast.makeText(this, "Zła data rozpoczęcia!", Toast.LENGTH_SHORT).show();
                begin.getText().clear();
                end.getText().clear();
            }
        }else{
            Toast.makeText(this, "Proszę uzupełnić wszystkie pola!", Toast.LENGTH_SHORT).show();
        }

    }
}