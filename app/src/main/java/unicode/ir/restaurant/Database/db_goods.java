package unicode.ir.restaurant.Database;

import com.orm.SugarRecord;


public class db_goods extends SugarRecord {

    public int gid,groupid,goodcount;
    public double size,sellprice,discount,inventory;
    public String groupname,unitname,username,name,details,company,country,datecreated,description,imageurl,code;

    public db_goods(){}

    public db_goods(int gid, int groupid, int goodcount, double size, double sellprice, double discount, double inventory, String groupname, String unitname, String username, String name, String details, String company, String country, String datecreated, String description, String imageurl, String code) {
        this.gid = gid;
        this.groupid = groupid;
        this.goodcount = goodcount;
        this.size = size;
        this.sellprice = sellprice;
        this.discount = discount;
        this.inventory = inventory;
        this.groupname = groupname;
        this.unitname = unitname;
        this.username = username;
        this.name = name;
        this.details = details;
        this.company = company;
        this.country = country;
        this.datecreated = datecreated;
        this.description = description;
        this.imageurl = imageurl;
        this.code = code;
    }

    public int getGid() {
        return gid;
    }

    public int getGroupid() {
        return groupid;
    }

    public int getGoodcount() {
        return goodcount;
    }

    public double getSize() {
        return size;
    }

    public double getSellprice() {
        return sellprice;
    }

    public double getDiscount() {
        return discount;
    }

    public double getInventory() {
        return inventory;
    }

    public String getGroupname() {
        return groupname;
    }

    public String getUnitname() {
        return unitname;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getCompany() {
        return company;
    }

    public String getCountry() {
        return country;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public String getDescription() {
        return description;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getCode() {
        return code;
    }
}
