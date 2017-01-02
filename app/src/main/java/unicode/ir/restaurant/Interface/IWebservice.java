package unicode.ir.restaurant.Interface;

/**
 * Created by soheil syetem on 11/6/2016.
 */

public interface IWebservice {
    void getResult(Object result) throws Exception;
    void getError(String ErrorCodeTitle)throws Exception;
}
