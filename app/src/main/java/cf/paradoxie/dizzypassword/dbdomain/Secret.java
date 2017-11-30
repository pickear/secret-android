package cf.paradoxie.dizzypassword.dbdomain;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

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
    @ToMany(referencedJoinProperty = "secretId")
    private List<SecretList> secretLists;

    public void setSecretLists(List<SecretList> secretLists) {
        this.secretLists = secretLists;
    }

    /** Used for active entity operations. */
    @Generated(hash = 791731612)
    private transient SecretDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
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
    @Generated(hash = 1865424730)
    public synchronized void resetSecretLists() {
        secretLists = null;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 2004342236)
    public List<SecretList> getSecretLists() {
        if (secretLists == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SecretListDao targetDao = daoSession.getSecretListDao();
            List<SecretList> secretListsNew = targetDao._querySecret_SecretLists(id);
            synchronized (this) {
                if(secretLists == null) {
                    secretLists = secretListsNew;
                }
            }
        }
        return secretLists;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 131032064)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSecretDao() : null;
    }
    public boolean getCloud() {
        return this.cloud;
    }
    public void setCloud(boolean cloud) {
        this.cloud = cloud;
    }
    @Generated(hash = 29723780)
    public Secret(Long id, String title, String url, boolean cloud) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.cloud = cloud;
    }
    @Generated(hash = 974314130)
    public Secret() {
    }
}
