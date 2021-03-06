package ru.alinadorozhkina.gbweatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import ru.alinadorozhkina.gbweatherapp.fragments.FragmentAboutApp;
import ru.alinadorozhkina.gbweatherapp.fragments.FragmentSendingEmail;
import ru.alinadorozhkina.gbweatherapp.fragments.LoginFragment;
import ru.alinadorozhkina.gbweatherapp.helper.AllCity;
import ru.alinadorozhkina.gbweatherapp.helper.Keys;
import ru.alinadorozhkina.gbweatherapp.screens.weather.WeatherDescription;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LoginFragment.OnLoginFragmentDataListener {
    private static final String NotificationID="2";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int SETTINGS_CODE = 1;
    private LoginFragment loginFragment;
    private MaterialAutoCompleteTextView textInput_enter_city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean(Keys.THEME, true)) {
            setTheme(R.style.AppDarkTheme);
        }
        setContentView(R.layout.activity_main);
        initView();
        Toolbar toolbar = initToolbar();
        initDrawer(toolbar);
        initAutoCompleteText();
        initNotificationChannel();
        initFirebase();
    }
    private void initFirebase(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            // Не удалось получить токен, произошла ошибка
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                        Log.v(TAG, "токен "+ token);
                    }
                });
    }

    private void initView() {
        textInput_enter_city = findViewById(R.id.textInput_enter_city);
        Button button_show = findViewById(R.id.button_show);
        button_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherDescription.class);
                String value = textInput_enter_city.getText().toString().trim();
                if (!value.isEmpty()) {
                    Log.d(TAG, "передача бандла " + value);
                    intent.putExtra(Keys.CITY, value);
                    startActivity(intent);
                } else {
                    Snackbar.make(v, R.string.enter_city, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    private void initAutoCompleteText() {
        JsonFileReader reader = new JsonFileReader(this);
        reader.execute("city.list.json");
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initDrawer(Toolbar toolbar) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(NotificationID, "name", importance);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.v(TAG, " вызов метода onNavigationItemSelected");
        int id = item.getItemId();
        switch (id) {
            case R.id.write_to_developer:
                FragmentSendingEmail fragment_email = new FragmentSendingEmail();
                fragment_email.show(getSupportFragmentManager(), "fragment");
                break;
            case R.id.about_app:
                FragmentAboutApp fragmentAboutApp = new FragmentAboutApp();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_for_extra, fragmentAboutApp).addToBackStack(null).commit();
                break;
            case R.id.go_to_map:
                Intent intent = new Intent(MainActivity.this, WeatherMapsActivity.class);
                startActivity(intent);
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.settings1:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(intent, SETTINGS_CODE);
                return true;
            case R.id.loginPassword:
                loginFragment = new LoginFragment();
                loginFragment.show(getSupportFragmentManager(), "fragment");
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_for_extra, loginFragment).addToBackStack(null).commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void sendName(String name) {
        Log.v(TAG, " получено " + name);
        if (loginFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(loginFragment).commit();
            correctHeader(name);
        }
    }

    private void correctHeader(String name) {
        TextView header_instruction = findViewById(R.id.header_instruction);
        ImageView imageView_header = findViewById(R.id.imageView_header);
        imageView_header.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_tag_faces_24));
        header_instruction.setText(getString(R.string.Welcome, name));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public class JsonFileReader extends AsyncTask<String, Void, ArrayList<String>> {
        private final Context context;

        public JsonFileReader(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            ArrayList<String> cities = new ArrayList<>();
            JsonReader jsonReader;
            try {
                InputStream is = context.getAssets().open(strings[0]);
                InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
                jsonReader = new JsonReader(streamReader);
                Gson gson = new GsonBuilder().create();
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    AllCity allCity = gson.fromJson(jsonReader, AllCity.class);
                    String city = allCity.getName();
                    cities.add(city);
                }
                jsonReader.endArray();
                Collections.sort(cities);
                return cities;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, strings);
            textInput_enter_city.setDropDownBackgroundResource((R.color.green));
            textInput_enter_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String value = parent.getItemAtPosition(position).toString();
                    Log.v(TAG, "textInput_enter_city " + value);
                    Intent intent = new Intent(MainActivity.this, WeatherDescription.class);
                    intent.putExtra(Keys.CITY, value);
                    startActivity(intent);
                }
            });
            textInput_enter_city.setAdapter(adapter);
        }
    }
}