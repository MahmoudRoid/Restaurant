package unicode.ir.restaurant.Database;

import com.orm.SugarRecord;


public class db_order_list extends SugarRecord {
    public String orderid,address,price,orderdetails,orderdate,status ;

    public db_order_list(){}

    // status
    // 0 : submit sucssefully
    // 1 : waiting for response
    // 2 : closed

    public db_order_list(String orderid, String address, String price, String orderdetails, String orderdate , String status) {
        this.orderid = orderid;
        this.address = address;
        this.price = price;
        this.orderdetails = orderdetails;
        this.orderdate = orderdate;
        this.status = status ;
    }

    public String getOrderid() {
        return orderid;
    }

    public String getAddress() {
        return address;
    }

    public String getPrice() {
        return price;
    }

    public String getOrderdetails() {
        return orderdetails;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public String getStatus(){return status;}
}
