package unicode.ir.restaurant.Classes;

import com.orm.SugarApp;


public class myApplication extends SugarApp {

    public int postalCode ;

    @Override
    public void onCreate() {
        super.onCreate();
        postalCode = 0;

    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }
}
