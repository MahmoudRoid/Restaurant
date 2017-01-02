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
import unicode.ir.restaurant.Interface.IWebserviceByTag;
import unicode.ir.restaurant.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by soheil syetem on 12/31/2016.
 */

public class GetPostCost extends AsyncTask<Void,Void,String> {


    private Context context;
    private IWebserviceByTag delegate = null;
    private String WEB_SERVICE_URL,Tag;
    private SweetAlertDialog pDialog;


    public GetPostCost(Context context, IWebserviceByTag delegate,String Tag) {
        this.context = context;
        this.delegate = delegate;
        this.Tag= Tag;
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);

        WEB_SERVICE_URL = URLS.GetPostalCost;
    }// end GetData()

    @Override
    protected void onPreExecute() {
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("در حال دریافت اطلاعات");
        pDialog.setCancelable(true);
        pDialog.show();
    }// end onPreExecute()

    @Override
    protected String doInBackground(Void... voids) {
        Response response = null;
        String strResponse = "nothing_got";

        for (int i = 0; i <= 9; i++) {
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody body = new FormBody.Builder()
                        .add("Token", Variables.TOKEN)
                        .build();
                Request request = new Request.Builder()
                        .url(WEB_SERVICE_URL)
                        .post(body)
                        .build();

                response = client.newCall(request).execute();
                strResponse = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response != null) break;
        }

        return strResponse;
    }// end doInBackground()

    @Override
    protected void onPostExecute(String result) {
        pDialog.dismiss();
        if (result.equals("nothing_got")) {
            // no data to get
            try {
                delegate.getError(context.getResources().getString(R.string.error_empty_server),Tag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!result.startsWith("{")) {
            // it has a problem completely
            try {
                delegate.getError(context.getResources().getString(R.string.error_server),Tag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // data is okay and gotten successfully
            try {

                // parse JSON and pour it in array for future use
                JSONObject jsonObject = new JSONObject(result);
                int postCost = jsonObject.optInt("Postage",0);
                delegate.getResult(postCost,Tag);

            } // end try
            catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }// end else
    }// end onPostExecute()
}

