package tn.rnu.buslocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnSuccessListener;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
public class Singup_Form extends AppCompatActivity {
    private static final String TAG = Singup_Form.class.getSimpleName();

    EditText np,user,pass,email,number,entreprise,ligne;
    Button login;
    DBHelper db;
    CheckBox checkbox;
    GoogleApiClient googleApiClient;
    String SITE_KEY = "6LdxOEMaAAAAAKraJwwUXrELnLA-JA1iJv8XNKqr";
    String SECRET_KEY = "6LdxOEMaAAAAABXVamn491dGrCyu97q8-eOCONn-";
    String userResponseToken;
    Boolean Verified=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup__form);
        np=findViewById(R.id.np);
        user=findViewById(R.id.username);
        pass=findViewById(R.id.mdp);
        login=findViewById(R.id.button);
        email=findViewById(R.id.email);
        entreprise=findViewById(R.id.entreprise);
        ligne=findViewById(R.id.ligne);
        number=findViewById(R.id.numtel);
        checkbox=findViewById(R.id.recaptcha);

        db = new DBHelper(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String us,ps,em,num,lig,entre;
                us=user.getText().toString();
                ps=pass.getText().toString();
                num=number.getText().toString();
                lig=ligne.getText().toString();
                entre=entreprise.getText().toString();
                em=email.getText().toString();
                if (us.equals("")) {
                    Toast.makeText(Singup_Form.this, "Username Required", Toast.LENGTH_SHORT).show();
                }else if (ps.equals("")) {
                    Toast.makeText(Singup_Form.this, "Password Required", Toast.LENGTH_SHORT).show();
                }
                else if (em.equals("")) {
                    Toast.makeText(Singup_Form.this, "email Required", Toast.LENGTH_SHORT).show();
                }else if (entre.equals("")) {
                    Toast.makeText(Singup_Form.this, "Entreprise Required", Toast.LENGTH_SHORT).show();
                }
                else if (lig.equals("")) {
                    Toast.makeText(Singup_Form.this, "Ligne Required", Toast.LENGTH_SHORT).show();
                }else if (num.equals("")) {
                    Toast.makeText(Singup_Form.this, "Number Required", Toast.LENGTH_SHORT).show();
                }
                else {
                    Boolean checkuser = db.checkusername(us);
                    if(checkuser==false){
                        Boolean insert = db.insertData(us,ps);
                        if(insert=true && Verified==true){
                            Toast.makeText(Singup_Form.this, "Registration Complete", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(i);
                        }else{
                            Toast.makeText(Singup_Form.this, "Registration failed", Toast.LENGTH_SHORT).show();

                        }
                    }else{
                        Toast.makeText(Singup_Form.this, "User Already exists", Toast.LENGTH_SHORT).show();
                    }
                }
            }});

    }

    public void recaptchaClick(View v) {
        SafetyNet.getClient(this).verifyWithRecaptcha(SITE_KEY)
                .addOnSuccessListener(this,
                        new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                            @Override
                            public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                                // Indicates communication with reCAPTCHA service was
                                // successful.
                                userResponseToken = response.getTokenResult();
                                if (!userResponseToken.isEmpty()) {
                                    // Validate the user response token using the
                                    // reCAPTCHA siteverify API.
                                    // new SendPostRequest().execute();
                                    sendRequest();
                                }
                            }
                        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            // An error occurred when communicating with the
                            // reCAPTCHA service. Refer to the status code to
                            // handle the error appropriately.
                            ApiException apiException = (ApiException) e;
                            int statusCode = apiException.getStatusCode();
                            Log.d(TAG, "Error: " + CommonStatusCodes
                                    .getStatusCodeString(statusCode));
                        } else {
                            // A different, unknown type of error occurred.
                            Log.d(TAG, "Error: " + e.getMessage());
                        }
                    }
                });
    }
    public void sendRequest()  {

        String url = "https://www.google.com/recaptcha/api/siteverify";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            //Toast.makeText(MainActivity.this, obj.getString("success"), Toast.LENGTH_LONG).show();
                            if (obj.getString("success").equals("true")){
                                moveNewActivity();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Singup_Form.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("secret", SECRET_KEY);
                params.put("response", userResponseToken);
                return params;
            }
        };
        AppController.getInstance(this).addToRequestQueue(stringRequest);

    }
    public void moveNewActivity(){
        Verified=true;
    }
}