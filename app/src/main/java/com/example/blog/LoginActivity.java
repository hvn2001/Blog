package com.example.blog;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout textUsernameLayout;
    private TextInputLayout textPasswordInput;
    private Button loginButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textUsernameLayout = findViewById(R.id.textUsernameLayout);
        textPasswordInput = findViewById(R.id.textPasswordInput);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.onLoginClicked();
            }
        });
        // loginButton.setOnClickListener(v -> onLoginClicked());

        textUsernameLayout
                .getEditText()
                .addTextChangedListener(createTextWatcher(textUsernameLayout));

        textPasswordInput
                .getEditText()
                .addTextChangedListener(createTextWatcher(textPasswordInput));
    }

    private TextWatcher createTextWatcher(TextInputLayout textPasswordInput) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start, int count, int after) {
                // not needed
            }

            @Override
            public void onTextChanged(CharSequence s,
                                      int start, int before, int count) {
                textPasswordInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // not needed
            }
        };
    }

    private void onLoginClicked() {
        String username = textUsernameLayout.getEditText().getText().toString();
        String password = textPasswordInput.getEditText().getText().toString();
        if (username.isEmpty()) {
            textUsernameLayout.setError("Username must not be empty");
        } else if (password.isEmpty()) {
            textPasswordInput.setError("Password must not be empty");
        }
    }
}
