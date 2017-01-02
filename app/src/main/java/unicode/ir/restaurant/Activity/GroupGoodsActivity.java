package unicode.ir.restaurant.Activity;

import android.content.Context;
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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import unicode.ir.restaurant.Adapter.RecyclerViewAdapter_GroupGoods;
import unicode.ir.restaurant.AsyncTasks.GetGroupFoods;
import unicode.ir.restaurant.Classes.Internet;
import unicode.ir.restaurant.Classes.RecyclerItemClickListener;
import unicode.ir.restaurant.Database.db_goods;
import unicode.ir.restaurant.Database.db_order_list;
import unicode.ir.restaurant.Interface.IWebservice;
import unicode.ir.restaurant.Objects.Object_GoodGroup;
import unicode.ir.restaurant.R;

public class GroupGoodsActivity extends AppCompatActivity implements IWebservice {

    Typeface San;
    Toolbar toolbar;
    TextView txtToolbar;
    ArrayList<Object_GoodGroup> objectGoodGroupArrayList;
    public SharedPreferences prefs;

    @BindView(R.id.etebar_value_tv)
    TextView etebarValueTv;
    @BindView(R.id.count_value_tv)
    TextView countValueTv;

    @BindView(R.id.rv)
    RecyclerView rv;
    RecyclerViewAdapter_GroupGoods adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_goods);
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
        txtToolbar.setText("گروه های غذایی");

        objectGoodGroupArrayList = new ArrayList<>();

    }// end define()

    private void init() {
        // get etebar
        prefs = getSharedPreferences("Login", MODE_PRIVATE);
        etebarValueTv.setText(String.valueOf(prefs.getInt("cash",0)));
        // delete table of goods
        try {
            db_goods.deleteAll(db_goods.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // get data
        if (Internet.isNetworkAvailable(this)) {
            // call webservice
            GetGroupFoods get_data = new GetGroupFoods(this, this);
            get_data.execute();
        }
    }

    @OnClick(R.id.bastekt)
    public void onClick() {
        List<db_goods> list = Select.from(db_goods.class).where(Condition.prop("goodcount").gt(0)).list();
        if(list.size()>0){
           startActivity(new Intent(this,FactorActivty.class));
        }
        else Toast.makeText(this, "آیتمی انتخاب نکرده اید", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getResult(Object result) throws Exception {
        showList((ArrayList<Object_GoodGroup>) result);
    }

    @Override
    public void getError(String ErrorCodeTitle) throws Exception {
        Toast.makeText(this, ErrorCodeTitle, Toast.LENGTH_SHORT).show();
    }

    public void showList(ArrayList<Object_GoodGroup> objectGoodGroups) {
        objectGoodGroupArrayList = objectGoodGroups;
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(lm);
        adapter = new RecyclerViewAdapter_GroupGoods(objectGoodGroupArrayList, San, this);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        alphaAdapter.setDuration(1000);
        rv.setAdapter(alphaAdapter);

        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(GroupGoodsActivity.this,GoodsActivity.class);
                                intent.putExtra("groupId",objectGoodGroupArrayList.get(position).getId());
                                intent.putExtra("groupTitle",objectGoodGroupArrayList.get(position).getName());
                                startActivity(intent);
                            }
                        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            List<db_goods> list = Select.from(db_goods.class).where(Condition.prop("goodcount").gt(0)).list();
            int xx = list.size();
            countValueTv.setText(String.valueOf(list.size()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order_list, menu);
        return true;
    }// end onCreateOptionsMenu()

    /*on toolbar menu item click support*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                back();
                break;
            case R.id.action_order_list:
                openOrderListActivity();
                break;

            default:
                break;

        }
        return false;
    }// end onOptionsItemSelected()

    private void openOrderListActivity() {
        List<db_order_list> list = Select.from(db_order_list.class).list();
        if(list.size()>0){
            startActivity(new Intent(this,OrderListActivity.class));
        }
        else Toast.makeText(this, "هیچ سفارشی برای شما ثبت نشده است", Toast.LENGTH_SHORT).show();
    } 

    @Override
    public void onBackPressed() {
        back();
    }

    public void back() {
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
