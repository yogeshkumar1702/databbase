package com.example.databaseapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Editor extends AppCompatActivity {

    private final int UPDATE_MODE = 6162;
    int MODE = 0;
    EditText Name;
    EditText PhoneNumber;
    Button Submit;
    User CurrentUser;
    int current_uid;

    AppDatabase appDatabase ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        Intent i = getIntent();
        Name = findViewById(R.id.edittext_name);
        PhoneNumber = findViewById(R.id.editext_phonenumber);
        Submit = findViewById(R.id.button_create_contact);
        appDatabase = AppDatabase.getDatabase(this);

        if(i.getIntExtra("MODE",-1) == UPDATE_MODE){
            MODE = UPDATE_MODE;
            current_uid = i.getIntExtra("CURRENT_USER",-1);
            CurrentUser = getCurrentUser(current_uid);
            Name.setText(CurrentUser.firstName);
            PhoneNumber.setText(CurrentUser.phone_number);
            Submit.setText(R.string.Button_Update_Text);
        }

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFieldsEmpty()){
                    if(MODE == UPDATE_MODE){
                        // UPDATE the User.
                        User user = getCurrentUser(current_uid);
                        User UpdatedUser = getEnteredUser();
                        user.firstName = UpdatedUser.firstName;
                        user.phone_number = UpdatedUser.phone_number;
                        appDatabase.userDao().update(user);
                    }else{
                        //Insert the New USER
                        User user = getEnteredUser();
                        appDatabase.userDao().insertAll(user);
                    }
                    showToastMessage("Action Successful !!");
                    setResult(RESULT_OK);
                    finish();
                }else{
                    showToastMessage("The Fields Can't Be Empty");
                }
            }
        });


    }

    private User getCurrentUser(int uid){
        User user;
        user = appDatabase.userDao().getUserById(uid);
        return user;
    }

    private User getEnteredUser(){
        User user = new User();
        user.firstName = Name.getText().toString();
        user.phone_number = PhoneNumber.getText().toString();

        return user;
    }

    private boolean isFieldsEmpty(){
        return Name.getText().toString().isEmpty() || PhoneNumber.getText().toString().isEmpty() ;
    }

    private void showToastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
        finish();
    }
}
