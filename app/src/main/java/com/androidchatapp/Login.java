package com.androidchatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    TextView requests,admin;
    EditText  etPassword,name;
    Button loginButton;
    String user, pass,p;
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.contactadmin,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.Help){
                startActivity(new Intent(this, Admin.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        requests = (TextView) findViewById(R.id.requests);
        //admin = (TextView) findViewById(R.id.admin);//
        name = (EditText) findViewById(R.id.name);
        etPassword = (EditText) findViewById(R.id.etPassword);
        loginButton = (Button) findViewById(R.id.loginButton);
        Firebase.setAndroidContext(this);

        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login.this, Register.class));


            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = name.getText().toString();
                pass = etPassword.getText().toString();
                if (user.equals("")) {
                    name.setError("can't be blank");
                } else if (pass.equals("")) {
                    etPassword.setError("can't be blank");
                } else {
                    String url = "https://androidchatappmain.firebaseio.com/users.json";
                    final ProgressDialog pd = new ProgressDialog(Login.this);
                    pd.setMessage("Loading...");
                    pd.show();
                   // final Firebase reference = new Firebase("https://androidchatappmain.firebaseio.com//users");//newly added
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            if (s.equals("null")) {
                                Toast.makeText(Login.this, "user not found", Toast.LENGTH_LONG).show();
                            } else {
                                try {
                                    JSONObject obj = new JSONObject(s);
                                    if (!obj.has(user)) {
                                        Toast.makeText(Login.this, "user not found", Toast.LENGTH_LONG).show();
                                    }else  if (!obj.getJSONObject(user).getString("password").equals(pass))
                                    {
                                        Toast.makeText(Login.this, "incorrect password", Toast.LENGTH_LONG).show();
                                    } else if(obj.getJSONObject(user).getString("permission").equals("1")) {
                                        if (obj.getJSONObject(user).getString("password").equals(pass)) {
                                            UserDetails.name = user;//new added
                                            UserDetails.password = pass;
                                            //  UserDetails.permission="1";//newly added
                                            startActivity(new Intent(Login.this, Users.class));
                                        }
                                    }
                                     else {
                                        Toast.makeText(Login.this, "Your Registration is still pending", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            pd.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError);
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(Login.this);
                    rQueue.add(request);
                }

            }
        });

    }


}



