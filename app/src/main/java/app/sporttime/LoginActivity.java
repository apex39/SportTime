package app.sporttime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String REGISTER_URL = "http://102657.panda5.pl/raw/login.php";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";


    private EditText editTextUsername;
    private EditText editTextPassword;

    private Button buttonLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(this);
    }

    private void loginUser(){
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("1")) {
                            openMainActivity(response);
                            Toast.makeText(LoginActivity.this,"Login Success!",Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(LoginActivity.this,"Login Failed".toString(),Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put(KEY_USERNAME,username);
                params.put(KEY_PASSWORD,password);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonLogin){
            loginUser();
        }
    }

    public void openRegisterView(View view){
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void openMainActivity(String login){
        Intent mIntent = new Intent(this, MainActivity.class);
        mIntent.putExtra("LOGIN", login);
        startActivity(mIntent);
    }
}
