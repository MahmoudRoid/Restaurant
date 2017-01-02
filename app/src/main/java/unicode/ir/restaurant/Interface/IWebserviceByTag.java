package unicode.ir.restaurant.Interface;

/**
 * Created by soheil syetem on 11/6/2016.
 */

public interface IWebserviceByTag {
    void getResult(Object result, String Tag) throws Exception;
    void getError(String ErrorCodeTitle, String Tag)throws Exception;
}
