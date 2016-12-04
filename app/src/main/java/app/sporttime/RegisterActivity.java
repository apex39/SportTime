package app.sporttime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

        private static final String REGISTER_URL = "http://102657.panda5.pl/raw/register.php";

        public static final String KEY_USERNAME = "username";
        public static final String KEY_PASSWORD = "password";


        private EditText editTextUsername;
        private EditText editTextPassword1;
        private EditText editTextPassword2;

        private Button buttonRegister;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);

            editTextUsername = (EditText) findViewById(R.id.editTextUsername);
            editTextPassword1 = (EditText) findViewById(R.id.editTextPassword1);
            editTextPassword2 = (EditText) findViewById(R.id.editTextPassword2);

            buttonRegister = (Button) findViewById(R.id.buttonRegister);

            buttonRegister.setOnClickListener(this);
        }


    private void registerUser(final String username, final String password){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("1")) {
                                Toast.makeText(app.sporttime.RegisterActivity.this,"User created!",Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (response.equals("-1")){
                                Toast.makeText(app.sporttime.RegisterActivity.this,"User not created. (Duplicate username?)",Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(app.sporttime.RegisterActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<>();
                    params.put(KEY_USERNAME, username);
                    params.put(KEY_PASSWORD, password);
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

        String username;
        String password;
        @Override
        public void onClick(View v) {
            if(v == buttonRegister){
                if (!editTextUsername.getText().toString().isEmpty() &&
                        !editTextPassword1.getText().toString().isEmpty() &&
                        !editTextPassword2.getText().toString().isEmpty()) {
                    if(editTextPassword1.getText().toString().equals(editTextPassword2.getText().toString())){ //sprawdźmy czy hasła w polach się zgadzają
                        password = editTextPassword1.getText().toString().trim();
                        username = editTextUsername.getText().toString().trim();
                        registerUser(username, password); //jeśli hasła się zgadzają, wysyłamy zapytanie o rejestrację
                    } else {
                        Toast.makeText(RegisterActivity.this,"Passwords do not match!",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this,"Fields cannot be empty!",Toast.LENGTH_SHORT).show();
                }
            }
        }

        public void openRegisterView(View view){
            startActivity(new Intent(this, RegisterActivity.class));
        }
    
}
