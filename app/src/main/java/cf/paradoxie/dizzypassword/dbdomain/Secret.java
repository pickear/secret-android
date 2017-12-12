package cf.paradoxie.dizzypassword.dbdomain;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.Date;
import java.util.List;

import cf.paradoxie.dizzypassword.gen.DaoSession;
import cf.paradoxie.dizzypassword.gen.SecretDao;
import cf.paradoxie.dizzypassword.gen.SecretListDao;



/**
 * Created by a1 on 2017/11/28.
 */
@Entity
public class Secret {
    @Id
    private Long id;
    private String title;
    private String url;
    private boolean cloud;
    private Date createTime;
    private Date updateTime;
    private boolean deleted;

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Property(nameInDb = "secrets")
    @ToMany(referencedJoinProperty = "secretId")
    private List<SecretList> secrets;

    public void setSecrets(List<SecretList> secrets) {
        this.secrets = secrets;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1626694458)
    public synchronized void resetSecrets() {
        secrets = null;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 358976932)
    public List<SecretList> getSecrets() {
        if (secrets == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SecretListDao targetDao = daoSession.getSecretListDao();
            List<SecretList> secretsNew = targetDao._querySecret_Secrets(id);
            synchronized (this) {
                if(secrets == null) {
                    secrets = secretsNew;
                }
            }
        }
        return secrets;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 131032064)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSecretDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 791731612)
    private transient SecretDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public boolean getCloud() {
        return this.cloud;
    }
    public void setCloud(boolean cloud) {
        this.cloud = cloud;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public boolean getDeleted() {
        return this.deleted;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Generated(hash = 693331982)
    public Secret(Long id, String title, String url, boolean cloud, Date createTime, Date updateTime,
            boolean deleted) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.cloud = cloud;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.deleted = deleted;
    }

    @Generated(hash = 974314130)
    public Secret() {
    }


}
