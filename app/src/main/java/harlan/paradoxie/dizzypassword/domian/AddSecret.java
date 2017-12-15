package harlan.paradoxie.dizzypassword.domian;

/**
 * Created by a1 on 2017/11/22.
 */
public class AddSecret {
    private boolean custtype=false;
    private String name="";
    private String valuse;

    public boolean isCusttype() {
        return custtype;
    }

    public void setCusttype(boolean custtype) {
        this.custtype = custtype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValuse() {
        return valuse;
    }

    public void setValuse(String valuse) {
        this.valuse = valuse;
    }
}
