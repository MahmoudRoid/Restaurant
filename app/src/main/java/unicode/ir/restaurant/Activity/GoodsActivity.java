package unicode.ir.restaurant.Activity;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import unicode.ir.restaurant.Adapter.RecyclerViewAdapter_Good;
import unicode.ir.restaurant.AsyncTasks.GetFoods;
import unicode.ir.restaurant.Classes.Internet;
import unicode.ir.restaurant.Database.db_goods;
import unicode.ir.restaurant.Interface.IWebservice;
import unicode.ir.restaurant.Objects.Object_Good;
import unicode.ir.restaurant.R;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class GoodsActivity extends AppCompatActivity implements IWebservice{

    @BindView(R.id.rv)
    RecyclerView rv;
    ArrayList<Object_Good> objectGoodArrayList;
    RecyclerViewAdapter_Good adapter ;

    private Typeface San;
    private Toolbar toolbar;
    private TextView txtToolbar;
    String groupTitle, groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        ButterKnife.bind(this);
        define();
        init();
    }


    private void define() {
        // get extras
        groupId = String.valueOf(getIntent().getExtras().getInt("groupId"));
        groupTitle = getIntent().getExtras().getString("groupTitle");

        San = Typeface.createFromAsset(getAssets(), "fonts/SansLight.ttf");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtToolbar = (TextView) findViewById(R.id.txtToolbar_appbar);
        txtToolbar.setTypeface(San);
        txtToolbar.setText(groupTitle);

        objectGoodArrayList = new ArrayList<>();
    }

    private void init() {

        List<db_goods> list = Select.from(db_goods.class).where(Condition.prop("groupid").eq(getIntent().getExtras().getInt("groupId"))).list();

        if(list.size()>0){
            // show data
            showList_withDb(list);
        }
        else {
            if(Internet.isNetworkAvailable(this)){
                GetFoods getdata = new GetFoods(this,this,groupId);
                getdata.execute();
            }
            else Toast.makeText(this, R.string.error_internet, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order, menu);
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
            case R.id.action_basket:
                List<db_goods> list = Select.from(db_goods.class).where(Condition.prop("goodcount").gt(0)).list();
                if(list.size()>0){
                    startActivity(new Intent(this,FactorActivty.class));
                }
                else Toast.makeText(this, "آیتمی انتخاب نکرده اید", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;

        }
        return false;
    }

    @Override
    public void getResult(Object result) throws Exception {
//        showList((ArrayList<Object_Good>) result);
        List<db_goods> list = Select.from(db_goods.class).where(Condition.prop("groupid")
                .eq(getIntent().getExtras().getInt("groupId"))).list();

        showList_withDb(list);
    }

    @Override
    public void getError(String ErrorCodeTitle) throws Exception {
        Toast.makeText(this, ErrorCodeTitle, Toast.LENGTH_SHORT).show();
    }

    public void showList_withDb(List<db_goods> objectGood) {

        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(lm);
        adapter = new RecyclerViewAdapter_Good(objectGood, San, this);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        alphaAdapter.setDuration(1000);
        rv.setAdapter(alphaAdapter);
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
