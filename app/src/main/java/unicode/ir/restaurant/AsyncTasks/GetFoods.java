package unicode.ir.restaurant.AsyncTasks;


import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import unicode.ir.restaurant.Classes.URLS;
import unicode.ir.restaurant.Classes.Variables;
import unicode.ir.restaurant.Database.db_goods;
import unicode.ir.restaurant.Interface.IWebservice;
import unicode.ir.restaurant.Objects.Object_Good;
import unicode.ir.restaurant.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetFoods extends AsyncTask<Void,Void,String> {

    public ArrayList<Object_Good> objectGoodsaArrayList = new ArrayList<>();
    public Context context;
    private IWebservice delegate = null;
    SweetAlertDialog pDialog;
    public String url,id;

    public GetFoods(Context context, IWebservice delegate, String id){
        this.context    = context;
        this.delegate   = delegate;
        this.id=id;

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        this.url= URLS.GetGoods;
    }// end GetData()

    @Override
    protected void onPreExecute() {
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("در حال دریافت اطلاعات");
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected String doInBackground(Void... voids) {
        Response response = null;
        String strResponse = "nothing_got";

        for(int i=0;i<=9;i++){
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody body = new FormBody.Builder()
                        .add("Token", Variables.TOKEN)
                        .add("Id",this.id)
                        .build();
                Request request = new Request.Builder()
                        .url(this.url)
                        .post(body)
                        .build();

                response = client.newCall(request).execute();
                strResponse= response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(response!=null) break;
        }
        return strResponse;
    }

    @Override
    protected void onPostExecute(String result) {
        pDialog.dismiss();
        if (result.equals("nothing_got")) {
            try {
                delegate.getError(context.getResources().getString(R.string.error_empty_server));
            }
            catch (Exception e) {e.printStackTrace();}
        }
        else if(!result.startsWith("{")){

            try {
                delegate.getError(context.getResources().getString(R.string.error_server));
            }
            catch (Exception e) {e.printStackTrace();}
        }

        else {

            try {
                JSONObject jsonObject=new JSONObject(result);
                int Type=jsonObject.getInt("Status");
                if(Type==1){
                    JSONArray jsonArray = jsonObject.getJSONArray("Data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                        int Id            = jsonObject2.optInt("Id");
                        int GroupId       = jsonObject2.optInt("GroupId");
                        String GroupName  = jsonObject2.optString("GroupName");
                        String UnitName   = jsonObject2.optString("UnitName");
                        String UserName   = jsonObject2.optString("UserName");
                        String Code       = jsonObject2.optString("Code"); //
                        String Name       = jsonObject2.optString("Name");
                        String Details    = jsonObject2.optString("Details","");
                        double Size       = jsonObject2.optDouble("Size",0);
                        double SellPrice  = jsonObject2.optDouble("SellPrice",0);
                        double Discount   = jsonObject2.optDouble("Discount",0);
                        String Company    = jsonObject2.optString("Company");
                        String Country    = jsonObject2.optString("Country");
                        double Inventory   = jsonObject2.optDouble("Inventory",0);
                        String DateCreated   = jsonObject2.optString("DateCreated");
                        String Description   = jsonObject2.optString("Description");
                        String ImageUrl ="";

                        try {
                            JSONArray jsonArray2 = jsonObject2.getJSONArray("ImageUrl");
                            ImageUrl = jsonArray2.get(0).toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        Object_Good obj = new Object_Good(Id,GroupId,Size,Code,SellPrice,Discount,GroupName
                        ,UnitName,UserName,Name,Details,Company,Country,8,DateCreated,Description,ImageUrl);
                        objectGoodsaArrayList.add(obj);

                        // add to db
                        db_goods db = new db_goods(Id,GroupId,0,Size,SellPrice,Discount,8,GroupName,UnitName
                                ,UserName,Name,Details,Company,Country,DateCreated,Description,ImageUrl,Code);
                        db.save();

                    }


                    if (objectGoodsaArrayList.size()>0) {
                        delegate.getResult(objectGoodsaArrayList);
                    } else {
                        delegate.getError("no data");
                    }
                }
                else {
                    // server said data is incorrect
                    delegate.getError(context.getResources().getString(R.string.error_invalid));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

