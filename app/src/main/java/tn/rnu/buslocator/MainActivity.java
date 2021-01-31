package tn.rnu.buslocator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {
    DBHelper db;
    EditText user,pass;
    Button login,register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new DBHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user=findViewById(R.id.Username);
        pass=findViewById(R.id.Password);
        login=findViewById(R.id.Login);
        register=findViewById(R.id.register);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String us,ps;
                us=user.getText().toString();
                ps=pass.getText().toString();
                if (us.equals("")) {
                    Toast.makeText(MainActivity.this, "Username Required", Toast.LENGTH_SHORT).show();
                }else if (ps.equals("")) {
                    Toast.makeText(MainActivity.this, "Password Required", Toast.LENGTH_SHORT).show();
                }
                else {
                    Boolean checkuserpass=db.checkpassword(us,ps);
                    if(checkuserpass==true){
                        Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(getApplicationContext(),MapActivity.class);
                        startActivity(i);

                    }else{
                        Toast.makeText(MainActivity.this, "Check your infos", Toast.LENGTH_SHORT).show();

                    }
                }
            }});
        register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this,Singup_Form.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
