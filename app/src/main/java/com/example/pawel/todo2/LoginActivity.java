package com.example.pawel.todo2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login = (Button) findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText name = (EditText) findViewById(R.id.editText);
                String nameValue = name.getText().toString();


                Intent intent = new Intent(LoginActivity.this, MainActivity.class)
                        .putExtra(MainActivity.NAME_STRING, nameValue+",1@wp.pl");
                startActivity(intent);
            }
        });

    }
}
