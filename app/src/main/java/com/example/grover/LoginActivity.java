package com.example.grover;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity a = this;
        setContentView(R.layout.activity_login);
        findViewById(R.id.progressBar).setVisibility(View.GONE);
        findViewById(R.id.googleLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                startActivity(intent);
                a.finishAfterTransition();
                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            }
        });
    }
}