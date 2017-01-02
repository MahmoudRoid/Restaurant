package unicode.ir.restaurant.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import unicode.ir.restaurant.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View view){
        // goto folan safe :-D lol
        startActivity(new Intent(this,GroupGoodsActivity.class));
    }
}
