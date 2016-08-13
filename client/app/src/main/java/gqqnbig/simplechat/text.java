package gqqnbig.simplechat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Kitster on 9/8/2016.
 */
public class text extends AppCompatActivity {


    private TextView tv;private Button btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_main);
        tv = (TextView) findViewById(R.id.idTextView);
        Bundle bundle=getIntent().getExtras();
        String string=bundle.getString("key");
        tv.setText(string);


    }

}
