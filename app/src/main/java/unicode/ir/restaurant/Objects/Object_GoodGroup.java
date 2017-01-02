package unicode.ir.restaurant.Objects;

public class Object_GoodGroup {
    public int Id,ParentId;
    public String Name;

    public Object_GoodGroup(int Id, int ParentId, String Name) {
        this.Id = Id;
        this.ParentId = ParentId;
        this.Name = Name;
    }


    public int getId() {
        return Id;
    }

    public int getParentId() {
        return ParentId;
    }

    public String getName() {
        return Name;
    }
}
