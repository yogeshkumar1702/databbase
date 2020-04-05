package com.example.databaseapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final int ADD_CONTACT_REQUEST_CODE = 6961;
    private final int UPDATE_MODE = 6162;


    AppDatabase database ;
    Button AddContact;
    ListView ContactList;
    AlertDialog.Builder RemoveContactDialog;
    ContactListAdapter contactListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = AppDatabase.getDatabase(this);
        AddContact = findViewById(R.id.button_add_contact);
        ContactList = findViewById(R.id.listview_contact_list);

        contactListAdapter = new ContactListAdapter(this,getUpdatedContactList());
        ContactList.setAdapter(contactListAdapter);

        RemoveContactDialog = new AlertDialog.Builder(this);
        RemoveContactDialog.setTitle(R.string.action_contact_dialog_title);


        AddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityToAddContact();
            }
        });

        /*ContactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivityToEditContact((int)id);
            }
        });*/

        ContactList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String Message ;
                final User user = (User) contactListAdapter.getItem(position);

                Message = "Do You Want To Remove " + user.firstName +" From Contact List ?";

                RemoveContactDialog.setMessage(Message);
                RemoveContactDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteUser(user);
                    }
                });

                RemoveContactDialog.setNegativeButton(R.string.edit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int _id) {
                        startActivityToEditContact(user.uid);
                    }
                });


                RemoveContactDialog.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ;
                    }
                });

                AlertDialog dialog = RemoveContactDialog.create();
                dialog.show();
                return false;
            }
        });

    }

    private void DeleteUser(User user) {
        database.userDao().delete(user);
        contactListAdapter.ChangeDataSet(getUpdatedContactList());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case ADD_CONTACT_REQUEST_CODE :
                if(resultCode == RESULT_OK){
                    contactListAdapter.ChangeDataSet(getUpdatedContactList());
                    Log.i(getApplication().toString(),"DATA ADDED");
                }else{
                    Log.e(getApplication().toString(),"SOMETHING HAPPENED DURING INSERTION");
                }
                break;
            case UPDATE_MODE :
                if(resultCode == RESULT_OK){
                    contactListAdapter.ChangeDataSet(getUpdatedContactList());
                    Log.i(getApplication().toString(),"DATA UPDATED");
                }else{
                    Log.e(getApplication().toString(),"SOMETHING HAPPENED DURING UPDATION");
                }
                break;

            default:
                Log.e(getApplication().toString(),"SOMETHING WENT WRONG");
        }
    }

    private ArrayList<User> getUpdatedContactList(){
        return new ArrayList<User>(database.userDao().getAll()) ;
    }

    private void startActivityToAddContact(){
        Intent i = new Intent(this,Editor.class);
        i.putExtra("MODE",ADD_CONTACT_REQUEST_CODE);
        startActivityForResult(i,ADD_CONTACT_REQUEST_CODE);
    }

    private void startActivityToEditContact(int uid){
        Intent i = new Intent(this,Editor.class);
        i.putExtra("MODE",UPDATE_MODE );
        i.putExtra("CURRENT_USER",uid);
        startActivityForResult(i,UPDATE_MODE);
    }
}
