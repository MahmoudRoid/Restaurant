package unicode.ir.restaurant.Objects;


public class Object_Good {
    public int Id,GroupId;
    public double Size,SellPrice,Discount,Inventory;
    public String GroupName,UnitName,Username,Name,Details,Company,Country,DateCreated,Description,ImageUrl,Code;

    public Object_Good(int id,int GroupId, double size, String code, double sellPrice, double discount, String groupName, String unitName, String username, String name, String details, String company, String country, double inventory, String dateCreated, String description, String imageUrl) {
        Id = id;
        this.GroupId=GroupId;
        Size = size;
        Code = code;
        SellPrice = sellPrice;
        Discount = discount;
        GroupName = groupName;
        UnitName = unitName;
        Username = username;
        Name = name;
        Details = details;
        Company = company;
        Country = country;
        Inventory = inventory;
        DateCreated = dateCreated;
        Description = description;
        ImageUrl = imageUrl;
    }

    public int getId() {
        return Id;
    }

    public int getGroupId(){return GroupId;}

    public double getSize() {
        return Size;
    }

    public String getCode() {
        return Code;
    }

    public double getSellPrice() {
        return SellPrice;
    }

    public double getDiscount() {
        return Discount;
    }

    public String getGroupName() {
        return GroupName;
    }

    public String getUnitName() {
        return UnitName;
    }

    public String getUsername() {
        return Username;
    }

    public String getName() {
        return Name;
    }

    public String getDetails() {
        return Details;
    }

    public String getCompany() {
        return Company;
    }

    public String getCountry() {
        return Country;
    }

    public double getInventory() {
        return Inventory;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public String getDescription() {
        return Description;
    }

    public String getImageUrl() {
        return ImageUrl;
    }
}
