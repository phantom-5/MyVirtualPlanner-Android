package io.github.teamseven.myvirtualplanner;

/**
 * Created by teamseven
 */

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import at.markushi.ui.CircleButton;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener{

    // Always remember to follow a naming scheme. The global variables should have a prefix, most commonly used one is m
    private DatabaseReference mDataBase; //Firebase Reference
    private icon_Manager mIconManager; // Icon manager object, for adding glyphs
    private Toolbar mToolbar; // Toolbar object for the top toolbar
    private DrawerLayout mDrawerLayout; // Layout object for the navigation menu
    private NavigationView mNavigationView; // The navigation view
    private TextView notice; // The notice text view to show what important notifs we have
    private CircleButton mAddBtn; // Button to add new reminders.
    private String[] reminders_array,reminders_date,reminders_time;
    private int mIndex;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDataBase = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        mToolbar = (Toolbar) findViewById(R.id.topToolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);
        mAddBtn = (CircleButton) findViewById(R.id.addBtn);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer);

        mDrawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        mDrawerLayout.removeDrawerListener(toggle);

        // Compatibility mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setElevation(10.f);
        }
        //notice_board
        mIconManager=new icon_Manager();
        notice=((TextView) findViewById(R.id.notice));
        notice.setTypeface(mIconManager.get_icons(
                "fonts/ionicons.ttf",this
        ));
        char icon=notice.getText().charAt(0);
        String ic=Character.toString(icon);
        String s=ic+"<font color=##FD971F><b> Important Notice</b></font>";
        notice.setText(Html.fromHtml(s));

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("circle", "onClick: Add btn clicked");
                AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                String text_rem=lReminder.getText().toString();
                final DatePicker rem_date=(DatePicker) lview.findViewById(R.id.datePicker4);
                final TimePicker rem_time=(TimePicker)lview.findViewById(R.id.timePicker);
                mBuilder.setView(lview);
                AlertDialog rem_dialog=mBuilder.create();
                rem_dialog.show();
                Button submit_date=(Button) lview.findViewById(R.id.submit_date);
                submit_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int day=0;
                        int month=0;
                        int year=0;
                        int hour=0;
                        int min=0;
                        onDateSet(rem_date,day,month,year);
                        onTimeSet(rem_time,hour,min);
                        Intent i5=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i5);
                    }

                });
                //TODO-Rudra: get text,date,time and store it either firebase or any other form we can retrieve from

            }
        });
        //TODO-Rudra: write and call method to sort strings and its corresponding dates as per importance
        // TODO-Rudra : Add functionality to display required most important/urgent string in notice box and set calender reminder
        // TODO-Rudra : Also after the deadline, the next important task should take its place

    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        i=datePicker.getDayOfMonth();
        i1=datePicker.getMonth()+1;
        i2=datePicker.getYear();
        String date=Integer.toString(i)+"/"+Integer.toString(i1)+"/"+Integer.toString(i2);
        Toast.makeText(MainActivity.this,date,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Calendar calendar=Calendar.getInstance();
        i=timePicker.getCurrentHour();
        i1=timePicker.getCurrentMinute();
        String time=Integer.toString(i)+":"+Integer.toString(i1);
        Toast.makeText(MainActivity.this,time,Toast.LENGTH_LONG).show();

    }

    private void addSub() {
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Menu menu = mNavigationView.getMenu();
        menu.add("Subject 1");
        mDrawerLayout.closeDrawers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        Todo : Add some required functionality here
        int id = item.getItemId();
        switch (id) {
            case R.id.dropDown_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.dropDown_login:
                Toast.makeText(this, "Log in / Sign up", Toast.LENGTH_SHORT).show();
                break;
            case R.id.dropDown_aboutUs:
                Toast.makeText(this, "About us", Toast.LENGTH_SHORT).show();
                break;

//                TODO : A few more items need to be added. There will be no nav bar in the bottom and all of them will be included here.
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add_sub:
//                TODO : Add a subject
                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
                addSub();
                break;
            case R.id.exam:
                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, exams.class));
                break;
            case R.id.ass:
//                TODO : Logic for assignment
                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.personal:
//                TODO : Open a new activity containing all the things related to personal. *hint* Intent
                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.misc:
//                TODO : New activity for all things misc using Intent
                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.sub_1:
////                TODO : open subject page
//                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.sub_2:
////                TODO : open subject page
//                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.sub_3:
////                TODO : open subject page
//                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.sub_4:
////                TODO : open subject page
//                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.sub_5:
////                TODO : open subject page
//                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
//                break;


//                Todo : Add all the other things that are needed in the navigation bar and then implement them here

        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
