package com.androidchatapp;

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

import androidx.appcompat.app.AppCompatActivity;

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

public class Register extends AppCompatActivity {
    EditText userid,name, password,contact,address;
    Button registerButton;
    String user, pass,id,no,add,p;//p add
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.name);
        userid=(EditText) findViewById(R.id.userid);
        password = (EditText) findViewById(R.id.password);
        address = (EditText) findViewById(R.id.address);
        registerButton = (Button) findViewById(R.id.registerButton);
        login = (TextView) findViewById(R.id.login);
        contact=  (EditText) findViewById(R.id.contact);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user=name.getText().toString();
                id = userid.getText().toString();
                pass = password.getText().toString();
                no   = contact.getText().toString();
                add =address.getText().toString();

                if (user.equals("")) {
                    name.setError("can't be blank");
                }else if (id.equals("")) {
                    userid.setError("can't be blank");
                } else if (pass.equals("")) {
                    password.setError("can't be blank");
                } else if (no.equals("")) {
                    contact.setError("can't be blank");
                } else if (add.equals("")) {
                    address.setError("can't be blank");
                }else if (!user.matches("[A-Za-z0-9]+")) {
                    name.setError("only alphabet or number allowed");
                } else if (user.length()>7) {
                    name.setError("at least 7 characters long");
                }else if (!(id.startsWith("C1812"))){
                    userid.setError("not valid");
                }else if (id.length()<7) {
                    userid.setError("at least 7 characters long");
                }else if (!(no.startsWith("018")||no.startsWith("019")||no.startsWith("017")||no.startsWith("013")||no.startsWith("016")||no.startsWith("015"))) {
                    contact.setError("invalid number");
                }else if (no.length() !=11) {
                    contact.setError("at least 11 characters long");
                } else {
                    final ProgressDialog pd = new ProgressDialog(Register.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    String url = "https://androidchatappmain.firebaseio.com/users.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Firebase reference = new Firebase("https://androidchatappmain.firebaseio.com//users");

                            if (s.equals("null")) {
                                reference.child(user).child("password").setValue(pass);
                                reference.child(user).child("contact").setValue(no);
                                reference.child(user).child("matric no").setValue(id);
                                reference.child(user).child("Address").setValue(add);
                                reference.child(user).child("permission").setValue("0");//new added

                                Toast.makeText(Register.this, "registration is pending", Toast.LENGTH_LONG).show();
                            } else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has(user)) {
                                        reference.child(user).child("password").setValue(pass);
                                        reference.child(user).child("contact").setValue(no);
                                        reference.child(user).child("matric no").setValue(id);
                                        reference.child(user).child("Address").setValue(add);
                                        reference.child(user).child("permission").setValue("0");//new added

                                        Toast.makeText(Register.this, "Registration is pending", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(Register.this, "User already exists", Toast.LENGTH_LONG).show();
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

                    RequestQueue rQueue = Volley.newRequestQueue(Register.this);
                    rQueue.add(request);
                }

            }
        });


    }
}