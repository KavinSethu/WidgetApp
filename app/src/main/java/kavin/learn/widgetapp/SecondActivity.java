package kavin.learn.widgetapp;

import android.app.Activity;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

import kavin.learn.widgetapp.Network.NetworkChangeReceiver;


public class SecondActivity extends AppCompatActivity {

    public NetworkChangeReceiver receiver;
    Boolean bl = true;

    private static Button english, tamil;
    String lang = "en";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static Locale myLocale;
    private static final String Locale_Preference = "Locale Preference";
    private static final String Locale_KeyValue = "Saved Locale";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        english = (Button) findViewById(R.id.english);
        tamil = (Button) findViewById(R.id.tamil);

        sharedPreferences = getSharedPreferences(Locale_Preference, Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        loadLocale();

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lang="en";
                changeLocale(lang);
            }
        });

        tamil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lang="ta";
                changeLocale(lang);
            }
        });

//        checkInternet();
    }

    public void changeLocale(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);//Set Selected Locale
        saveLocale(lang);//Save the selected locale
        Locale.setDefault(myLocale);//set new locale as default
        Configuration config = new Configuration();//get Configuration
        config.locale = myLocale;//set config locale as selected locale
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());//Update the config
        updateTexts();//Update texts according to locale
    }

    //Save locale method in preferences
    public void saveLocale(String lang) {
        editor.putString(Locale_KeyValue, lang);
        editor.commit();
    }

    //Update text methods
    private void updateTexts() {
        tamil.setText(R.string.btn_in);
        english.setText(R.string.btn_en);
    }

    public void loadLocale() {
        String language = sharedPreferences.getString(Locale_KeyValue, "");
        changeLocale(language);
    }


    public void checkInternet() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver(this);
        registerReceiver(receiver, filter);
        bl = receiver.is_connected();
        Log.d("Boolean ", bl.toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {

        }
    }

}
