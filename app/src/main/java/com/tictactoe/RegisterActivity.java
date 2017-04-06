package com.tictactoe;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    //private View mProgressView;
    //private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register);


        Button registerButton = (Button) findViewById(R.id.register_button);
        assert registerButton != null;
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                attemptRegistration();
            }
        });

    }

    protected void attemptRegistration() {

        showProgress(true);

        EditText emailView = (EditText) findViewById(R.id.email);
        String email = emailView.getText().toString();
        EditText nicknameView = (EditText) findViewById(R.id.nickname);
        assert nicknameView != null;
        String nickname = nicknameView.getText().toString();
        EditText passwordView = (EditText) findViewById(R.id.password);
        assert passwordView != null;
        String password = passwordView.getText().toString();
        assert emailView != null;


        // Reset errors.
        emailView.setError(null);
        nicknameView.setError(null);
        passwordView.setError(null);

        //emailView.setError();

        //View emailFocusView = null;
        //View nicknameFocusView = null;
        //View passwordFocusView = null;
        boolean cancel = false;

        if(TextUtils.isEmpty(email) || !email.contains("@") || !email.contains(".") || email.length() > 50) {
            emailView.setError("Invalid email");
            cancel = true;
        }
        if(TextUtils.isEmpty(nickname) || nickname.length() > 16) {
            nicknameView.setError("Invalid nickname");
            cancel = true;
        }
        if(TextUtils.isEmpty(password) || password.length() <= 4 || password.length() > 16) {
            passwordView.setError("Invalid password");
            cancel = true;
        }

        if(cancel) {
            if(passwordView.getError() != null)
                passwordView.requestFocus();
            if(nicknameView.getError() != null)
                nicknameView.requestFocus();
            if(emailView.getError() != null)
                emailView.requestFocus();
            showProgress(false);
            return;
        }

        addToDatabase(email, nickname, password);
        //if(exist[0]) {
        //    emailView.setError("Email already used");
        //    cancel = true;
        //}

        //if(exist[1]) {
        //    nicknameView.setError("Nickname already used");
        //    cancel = true;
        //}

        //if(cancel) {
            //if(nicknameView.getError() != null)
                //nicknameView.requestFocus();
            //if(emailView.getError() != null)
         //      emailView.requestFocus();
        //    showProgress(false);
        //    return;
       // }

        //Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
        //automaticLogin();
        //focusView.requestFocus();
    }

    protected void automaticLogin() {

        // ToDo:trzeba dodac bundle z login = true, nickem i hasle i w loginActivity to obsluzyć i dodać login = false w MainActivity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    protected void addToDatabase(final String email, final String nickname, final String password) {
        Response.Listener<String> responselistener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonobject = new JSONObject(response);
                    final boolean success = jsonobject.getBoolean("success");
                    final boolean[] exist = new boolean[2];
                    exist[0] = jsonobject.getBoolean("email");
                    exist[1] = jsonobject.getBoolean("login");
                    Toast.makeText(RegisterActivity.this, Boolean.toString(success), Toast.LENGTH_LONG).show();
                    if(success) {
                        automaticLogin();
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        if(exist[0] && !exist[1])
                            builder.setMessage("Email already used").setNegativeButton("Retry", null).create().show();
                        if(exist[1] && !exist[0])
                            builder.setMessage("Nickname already used").setNegativeButton("Retry", null).create().show();
                        if(exist[1] && exist[0])
                            builder.setMessage("Email and nickname already used").setNegativeButton("Retry", null).create().show();
                        showProgress(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        RegisterRequest registerRequest = new RegisterRequest(email, nickname, password, responselistener);
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(registerRequest);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        final View mLoginFormView = findViewById(R.id.login_form);
        final View mProgressView = findViewById(R.id.login_progress);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        }

        // let the system handle all other key events
        return super.onKeyDown(keyCode, event);
    }
}
