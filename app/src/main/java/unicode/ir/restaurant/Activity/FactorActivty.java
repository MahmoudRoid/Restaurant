package unicode.ir.restaurant.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unicode.ir.restaurant.Adapter.RecyclerViewAdapter_Factor;
import unicode.ir.restaurant.AsyncTasks.GetPostCost;
import unicode.ir.restaurant.AsyncTasks.PostOrder;
import unicode.ir.restaurant.Classes.Internet;
import unicode.ir.restaurant.Classes.myApplication;
import unicode.ir.restaurant.Database.db_goods;
import unicode.ir.restaurant.Interface.IUpdatePrice;
import unicode.ir.restaurant.Interface.IWebserviceByTag;
import unicode.ir.restaurant.R;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

public class FactorActivty extends AppCompatActivity implements IWebserviceByTag , IUpdatePrice{

    @BindView(R.id.total_count_value_tv)
    TextView totalCountValueTv;
    @BindView(R.id.postal_cost_value_tv)
    TextView postalCountValueTv;
    @BindView(R.id.orer_postal_cost_value_tv)
    TextView finaltotalCountValueTv;
    private Typeface San;
    private Toolbar toolbar;
    private TextView txtToolbar;

    @BindView(R.id.rv)
    RecyclerView rv;
    RecyclerViewAdapter_Factor adapter;

    static final String PostTag = "PostTag";
    static final String GetTag  = "GetTag";

    public myApplication application ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factor_activty);
        ButterKnife.bind(this);
        define();
        init();
    }

    private void define() {

        San = Typeface.createFromAsset(getAssets(), "fonts/SansLight.ttf");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtToolbar = (TextView) findViewById(R.id.txtToolbar_appbar);
        txtToolbar.setTypeface(San);
        txtToolbar.setText("فاکتور");

        application = (myApplication) getApplicationContext();
    }

    private void init() {
        // get postal cost

        if(Internet.isNetworkAvailable(this)){
            GetPostCost get  = new GetPostCost(this,this,GetTag);
            get.execute();
        }

        List<db_goods> list = Select.from(db_goods.class).where(Condition.prop("goodcount").gt(0)).list();
        showList(list);
        updateTotalPrice();
    }

    private void showList(List<db_goods> db_goodsArrayList) {
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(lm);
        adapter = new RecyclerViewAdapter_Factor(db_goodsArrayList, San, this,this);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        alphaAdapter.setDuration(1000);
        rv.setAdapter(alphaAdapter);
    }

    @OnClick(R.id.buy_relative)
    public void onClick() {
        String price = finaltotalCountValueTv.getText().toString();

        if (Internet.isNetworkAvailable(this)) {
            PostOrder post = new PostOrder(this, this, getUserId(), price, "","1234567890","", getOrderString(),getOrderDetails(),PostTag);
            post.execute();
        } else {
            Toast.makeText(this, R.string.error_internet, Toast.LENGTH_SHORT).show();
        }
    }

    private String getUserId() {
        SharedPreferences prefs =getSharedPreferences("Login", MODE_PRIVATE);
        return prefs.getString("userid","");
    }

    public String getOrderString() {
        String order = "" ;
        List<db_goods> list = Select.from(db_goods.class).where(Condition.prop("goodcount").gt(0)).list();
        for (int i=0 ; i<list.size() ; i++){
            // TODO : check shavad aya dorost string misazim ya kheir
         order += String.valueOf(list.get(i).getGid())+","+String.valueOf(list.get(i).getGoodcount())+"&";
        }
        return order;
    }

    public String getOrderDetails() {
        String orderDetails = "" ;
        List<db_goods> list = Select.from(db_goods.class).where(Condition.prop("goodcount").gt(0)).list();
        for (int i=0 ; i<list.size() ; i++){

            orderDetails += String.valueOf(list.get(i).getName())+" * "+String.valueOf(list.get(i).getGoodcount())+"\n\n";
        }

        return orderDetails.trim();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_empty, menu);
        return true;
    }

    /*on toolbar menu item click support*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                back();
                break;
            default:
                break;

        }
        return false;
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        finish();
    }

    @Override
    public void updateTotalPrice() {
        double totalPrice  = 0;
        List<db_goods> list = Select.from(db_goods.class).where(Condition.prop("goodcount").gt(0)).list();
        for (db_goods db : list){
            totalPrice += db.getSellprice() * db.getGoodcount() ;
        }
        totalCountValueTv.setText(String.valueOf(totalPrice));
        double finalCost = totalPrice + Double.parseDouble(postalCountValueTv.getText().toString());
        finaltotalCountValueTv.setText(String.valueOf(finalCost));
    }


    @Override
    public void getResult(Object result, String Tag) throws Exception {
        switch (Tag){
            case "PostTag":
                Toast.makeText(this, "سفارش شما ثبت شد", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,MainActivity.class));
                break;
            case "GetTag":
                // set to application class
                application.setPostalCode(Integer.parseInt(String.valueOf(result)));
                // set postal cost textview
                postalCountValueTv.setText(String.valueOf(result));
                // set final textview
                double finalCost = Double.parseDouble(String.valueOf(result))
                        + Double.parseDouble(totalCountValueTv.getText().toString());
                finaltotalCountValueTv.setText(String.valueOf(finalCost));

                break;
        }
    }

    @Override
    public void getError(String ErrorCodeTitle, String Tag) throws Exception {

        switch (Tag){
            case "PostTag":
                if(ErrorCodeTitle.equals("101")){
                    Toast.makeText(this, "مقدار اجناس کافی نیست", Toast.LENGTH_SHORT).show();
                }
                else  if(ErrorCodeTitle.equals("101")){
                    Toast.makeText(this, "مشکل مالی با سرور", Toast.LENGTH_SHORT).show();
                }
                else  if(ErrorCodeTitle.equals("101")){
                    Toast.makeText(this, "اعتبار شما کفی نیست", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, R.string.error_server, Toast.LENGTH_SHORT).show();
                }

                break;
            case "GetTag":
                Toast.makeText(this, R.string.error_server, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
