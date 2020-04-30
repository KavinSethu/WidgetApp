package kavin.learn.widgetapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView string_android,hello;
    Button change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hello=(TextView)findViewById(R.id.hello);
        string_android=(TextView)findViewById(R.id.string_android);
        change=(Button) findViewById(R.id.change);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(MainActivity.this,SecondActivity.class);
                startActivity(i);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        settext();
    }

    private void settext() {

        hello.setText(R.string.hello);
        string_android.setText(R.string.android);
    }
}
