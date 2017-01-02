package unicode.ir.restaurant.AsyncTasks;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.orm.query.Condition;
import com.orm.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import unicode.ir.restaurant.Classes.URLS;
import unicode.ir.restaurant.Classes.Variables;
import unicode.ir.restaurant.Database.db_order_list;
import unicode.ir.restaurant.Interface.IWebservice;
import unicode.ir.restaurant.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by soheil syetem on 12/31/2016.
 */

public class GetOrderStatus extends AsyncTask<Void,Void,String> {


    public Context context;
    private IWebservice delegate = null;
    SweetAlertDialog pDialog;
    public String url,orderId;

    public GetOrderStatus(Context context, IWebservice delegate,String orderId){
        this.context    = context;
        this.delegate   = delegate;
        this.orderId=orderId;

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        this.url= URLS.GetInvoiceStatus;
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
                        .add("Id",orderId)
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
                int Status = jsonObject.getInt("Status");
                db_order_list db = Select.from(db_order_list.class).where(Condition.prop("orderid").eq(orderId)).first();
                if(Status==0){
                // update database
                    db.status = "0";
                    db.save();
                    delegate.getResult("");
                }
                else if(Status==1){
                    db.status = "1";
                    db.save();
                    delegate.getResult("");
                }
                else if(Status==2){
                    db.status = "2";
                    db.save();
                    delegate.getResult("");
                }
                else {
                    delegate.getError("");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
