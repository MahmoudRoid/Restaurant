package unicode.ir.restaurant.AsyncTasks;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import unicode.ir.restaurant.Classes.URLS;
import unicode.ir.restaurant.Classes.Variables;
import unicode.ir.restaurant.Database.db_order_list;
import unicode.ir.restaurant.Database.db_orders;
import unicode.ir.restaurant.Interface.IWebserviceByTag;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class PostOrder extends AsyncTask<Void,Void,String> {

    public Context context;
    private IWebserviceByTag delegate = null;
    public String userId,price,address,description,order,postalCode,orderdetails,Tag;
    SweetAlertDialog pDialog ;
    public String Url;

    public PostOrder(Context context, IWebserviceByTag delegate, String userId, String price, String address, String postalCode, String description, String order,String orderdetails,String Tag){
        this.context=context;
        this.delegate=delegate;
        this.userId=userId;
        this.price=price;
        this.address=address;
        this.postalCode=postalCode;
        this.description= description;
        this.order=order;
        this.orderdetails  = orderdetails ;
        this.Tag=Tag;

        this.Url= URLS.AddInvoice;
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
    }

    @Override
    protected void onPreExecute() {
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("در حال ارسال اطلاعات");
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
                        .add("DealerId",userId)
                        .add("Price",price)
                        .add("Address",address)
                        .add("PostalCode",postalCode)
                        .add("Description",description)
                        .add("Order",order)
                        .build();
                Request request = new Request.Builder()
                        .url(this.Url)
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
                delegate.getError("NoData",Tag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(!result.startsWith("{")){
            // moshkel dare kollan
            try {
                delegate.getError("problem",Tag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else {

            try {
                JSONObject jsonObject=new JSONObject(result);
                int Type=jsonObject.getInt("Status");
                if(Type==1){
                    int orderId = jsonObject.optInt("Data",-1);
                    
                    // save into orders Table
                    String orderDate = ""; // TODO:  edit this by mosi ,  yani mosi bayad date ham vasamoon befreste
                    // parametere akhar k 1 ast  baraye ine k status = 1  yani montazere response
                    db_orders db = new db_orders(String.valueOf(orderId),this.address,this.price,this.orderdetails,orderDate,"1");
                    db.save();

                    // db fot order list
                    db_order_list db1 = new db_order_list(String.valueOf(orderId),this.address,this.price,this.orderdetails,orderDate,"1");
                    db1.save();

                     // 0 : submit sucssefully
                     // 1 : waiting for response
                     // 2 : closed

                    delegate.getResult("",Tag);
                }
                else {
                    delegate.getError(String.valueOf(Type),Tag);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

