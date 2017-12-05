package cf.paradoxie.dizzypassword.dbdomain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by a1 on 2017/11/28.
 */
@Entity
public class SecretList {
    @Id
    private Long id;
    private String name;
    @Property(nameInDb = "value")
    private String value;
    private long secretId;//此为 外键,和customer 对应起来
    public long getSecretId() {
        return this.secretId;
    }
    public void setSecretId(long secretId) {
        this.secretId = secretId;
    }
    public String getValue() {
        return this.value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 275404014)
    public SecretList(Long id, String name, String value, long secretId) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.secretId = secretId;
    }
    @Generated(hash = 957179475)
    public SecretList() {
    }
   
}
