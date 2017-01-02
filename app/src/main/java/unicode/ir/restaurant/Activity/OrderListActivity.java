package unicode.ir.restaurant.Activity;

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

import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import unicode.ir.restaurant.Adapter.RecyclerViewAdapter_ListOrder;
import unicode.ir.restaurant.AsyncTasks.GetOrderStatus;
import unicode.ir.restaurant.Classes.Internet;
import unicode.ir.restaurant.Database.db_order_list;
import unicode.ir.restaurant.Database.db_orders;
import unicode.ir.restaurant.Interface.IListOrder;
import unicode.ir.restaurant.Interface.IWebservice;
import unicode.ir.restaurant.R;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

public class OrderListActivity extends AppCompatActivity implements IWebservice , IListOrder{

    private Typeface San;
    private Toolbar toolbar;
    private TextView txtToolbar;

    @BindView(R.id.rv)
    RecyclerView rv;
    RecyclerViewAdapter_ListOrder adapter ;
    List<db_orders> ordersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
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
        txtToolbar.setText("لیست سفارشات");

        ordersList = new ArrayList<>();
    }

    public void init() {
        List<db_order_list> list = Select.from(db_order_list.class).list();

        if (list.size() > 0) {
            // show data
            showList_withDb(list);
        }
    }

    public void showList_withDb(List<db_order_list> objectorders) {

        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(lm);
        adapter = new RecyclerViewAdapter_ListOrder(objectorders, San, this,this);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        alphaAdapter.setDuration(1000);
        rv.setAdapter(alphaAdapter);
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
    public void getResult(Object result) throws Exception {
        Toast.makeText(this, "وضعیت سفارش شما آپدیت شد", Toast.LENGTH_SHORT).show();
        // refresh list
        List<db_order_list> list = Select.from(db_order_list.class).list();
        showList_withDb(list);
    }

    @Override
    public void getError(String ErrorCodeTitle) throws Exception {
        Toast.makeText(this, R.string.error_server, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void checkStatus(String orderId) {
        // call web service to refresh status
        if(Internet.isNetworkAvailable(this)){
            GetOrderStatus get = new GetOrderStatus(this,this,orderId);
            get.execute();
        }
        else {
            Toast.makeText(this, R.string.error_internet, Toast.LENGTH_SHORT).show();
        }
    }
}
