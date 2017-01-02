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
import unicode.ir.restaurant.Interface.IWebservice;
import unicode.ir.restaurant.Objects.Object_GoodGroup;
import unicode.ir.restaurant.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by soheil syetem on 12/25/2016.
 */

public class GetGroupFoods extends AsyncTask<Void,Void,String> {

    public ArrayList<Object_GoodGroup> objectGoodGroupArrayList = new ArrayList<>();
    public Context context;
    private IWebservice delegate = null;
    SweetAlertDialog pDialog;
    public String url;

    public GetGroupFoods(Context context, IWebservice delegate){
        this.context    = context;
        this.delegate   = delegate;

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        this.url= URLS.GetGoodsGroup;
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
                        .add("Id",Variables.getFoods)
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

                        int Id       = jsonObject2.optInt("Id");
                        int ParentId = jsonObject2.optInt("ParentId");
                        String Name   = jsonObject2.optString("Name");

                        Object_GoodGroup obj = new Object_GoodGroup(Id,ParentId,Name);
                        objectGoodGroupArrayList.add(obj);
                    }


                    if (objectGoodGroupArrayList.size()>0) {
                        delegate.getResult(objectGoodGroupArrayList);
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
