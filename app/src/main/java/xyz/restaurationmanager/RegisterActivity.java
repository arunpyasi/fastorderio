package xyz.restaurationmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {



    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView1;
    private EditText mPasswordView2;
    private EditText mPhoneView;
    private EditText mFirstNameView;
    private EditText mLastNameView;
    private RadioButton mMasculin;
    private RadioButton mFeminin;

    private View mProgressView;
    private View mRegisterFormView;

    AQuery aq;
    String url = "http://92.243.14.22:1337/person/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("On create RegisterActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set up the register form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.editTextRegisterEmail);
        mPasswordView1 = (EditText) findViewById(R.id.editTextRegisterMotDePasse1);
        mPasswordView2 = (EditText) findViewById(R.id.editTextRegisterMotDePasse2);
        mPhoneView = (EditText) findViewById(R.id.editTextRegisterTelephone);
        mFirstNameView = (EditText) findViewById(R.id.editTextRegisterPrenom);
        mLastNameView = (EditText) findViewById(R.id.editTextRegisterNom);
        mMasculin = (RadioButton) findViewById(R.id.radioButtonRegisterMasculin);
        mFeminin = (RadioButton) findViewById(R.id.radioButtonRegisterFeminin);





        //Button enregistrerInscription = (Button) findViewById(R.id.buttonEnregistrerInscription);
        Button enregistrerInscription = (Button) findViewById(R.id.register_button);
        enregistrerInscription.setOnClickListener(this);


        mRegisterFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    public void onClick(View v) {
        String email = mEmailView.getText().toString();
        String password1 = mPasswordView1.getText().toString();
        String password2 = mPasswordView2.getText().toString();
        String phone = mPhoneView.getText().toString();
        String firstName = mFirstNameView.getText().toString();
        String lastName = mLastNameView.getText().toString();
        String sexType = "";
        String createdBy = "Massa & Moise Ltd.";

        aq = new AQuery(v);

        boolean cancel = false;
        View focusView = null;

        // Check for not null firstName
        if (TextUtils.isEmpty(firstName)) {
            mFirstNameView.setError(getString(R.string.error_field_required));
            focusView = mFirstNameView;
            cancel = true;
        }
        // Check for not null lastName
        if (TextUtils.isEmpty(lastName)) {
            mLastNameView.setError(getString(R.string.error_field_required));
            focusView = mLastNameView;
            cancel = true;
        }
        // Check for not null radio M and F
        if ( !(mMasculin.isChecked()) && !(mFeminin.isChecked())) {
            mFeminin.setError(getString(R.string.error_field_required));
            focusView = mFeminin;
            cancel = true;
        }else if(mMasculin.isChecked()){
            sexType = "M";
        }else if(mFeminin.isChecked()){
            sexType = "F";
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password1) && !isPasswordValid(password1)) {
            mPasswordView1.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView1;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password2) && !isPasswordValid(password2)) {
            mPasswordView2.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView2;
            cancel = true;
        }

        if (!password1.equals(password2)) {
            mPasswordView2.setError(getString(R.string.error_not_same_passwords));
            focusView = mPasswordView2;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.


            GsonTransformer t = new GsonTransformer();

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("nom", lastName);
            params.put("prenom", firstName);
            params.put("telephone",phone);
            params.put("email",email);
            params.put("createdby",createdBy);
            params.put("password",password1);
            params.put("sexe",sexType);
            aq = new AQuery(v);
            aq.transformer(t).ajax(url, params, Account.class, new AjaxCallback<Account>() {
                public void callback(String url, Account account, AjaxStatus status) {
                    Gson gson = new Gson();
                    Log.d("GSON Object:", gson.toJson(account));
                }
            });

            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

}
