package harlan.paradoxie.dizzypassword.dbdomain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Dylan on 2019/2/6.
 */
@Entity
public class ApiUrl {
    @Id
    private Long id;
    private String url;
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 589720721)
    public ApiUrl(Long id, String url) {
        this.id = id;
        this.url = url;
    }
    @Generated(hash = 1811734072)
    public ApiUrl() {
    }

  
}
