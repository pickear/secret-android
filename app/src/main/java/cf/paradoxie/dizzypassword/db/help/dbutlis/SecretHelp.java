package cf.paradoxie.dizzypassword.db.help.dbutlis;

import java.util.List;

import cf.paradoxie.dizzypassword.dbdomain.Secret;
import cf.paradoxie.dizzypassword.dbdomain.SecretList;
import cf.paradoxie.dizzypassword.gen.SecretDao;
import cf.paradoxie.dizzypassword.gen.SecretListDao;

/**
 * Created by a1 on 2017/11/28.
 */
public class SecretHelp {
    /**
     * 添加数据
     *
     * @param
     */
    public static void insert(Secret secret) {
        DaoManager.getInstance().getDaoSession().getSecretDao().insert(secret);
    }

    /**
     * 添加数据
     *
     * @param
     */
    public static void insertList(List<Secret> secrets) {
        DaoManager.getInstance().getDaoSession().getSecretDao().insertInTx(secrets);
    }
    /**
     * 删除数据
     *
     * @param id
     */
    public static void delete(long id) {
        DaoManager.getInstance().getDaoSession().getSecretDao().deleteByKey(id);
        DaoManager.getInstance().getDaoSession().getSecretListDao().queryBuilder().where(SecretListDao.Properties.SecretId.eq(id)).buildDelete();
    }
    /**
     * 删除数据
     *
     * @param id
     */

    /**
     * 删除数据
     *
     * @param
     */
    public static void deleteall() {
        DaoManager.getInstance().getDaoSession().getSecretDao().deleteAll();
    }
    /**
     * 更新数据
     *
     * @param
     */
    public static void update(Secret secret,List<SecretList> secres) {
        DaoManager.getInstance().getDaoSession().getSecretDao().update(secret);
        for(int i=0;i<secres.size();i++){
            DaoManager.getInstance().getDaoSession().getSecretListDao().update(secres.get(i));
        }
    }
    /**
     * 更新数据
     *
     * @param
     */
    public static void update(Secret secret) {
        DaoManager.getInstance().getDaoSession().getSecretDao().update(secret);

    }
    public static SecretDao customerDao() {
        return DaoManager.getInstance().getDaoSession().getSecretDao();
    }
    public static SecretListDao orderDao() {
        return DaoManager.getInstance().getDaoSession().getSecretListDao();
    }
    /**
     * 查询条件为Type=TYPE_LOVE的数据
     *
     * @return
     */
    public static List<Secret> querySecretAll() {
        return DaoManager.getInstance().getDaoSession().getSecretDao().queryBuilder().list();
    }
    public static List<SecretList> querySecretListAll() {
        return DaoManager.getInstance().getDaoSession().getSecretListDao().queryBuilder().list();
    }
    /**
     * 查询条件为Type=TYPE_LOVE的数据
     *
     * @return
     */
    public static List<Secret> query(Long id) {
        List<Secret> secrets=DaoManager.getInstance().getDaoSession().getSecretDao().queryBuilder().where(SecretListDao.Properties.Id.eq(id)).list();
        for(int i=0;i<secrets.size();i++){
            secrets.get(i).setSecrets(SecretListHelp.query(secrets.get(i).getId()));
        }
        return secrets;
    }
    public static List<Secret> queryall() {
        List<Secret> secrets=DaoManager.getInstance().getDaoSession().getSecretDao().queryBuilder().list();
        for(int i=0;i<secrets.size();i++){
            secrets.get(i).setSecrets(SecretListHelp.query(secrets.get(i).getId()));
        }
        return secrets;
    }
    public static void updatacloud() {
        List<Secret> secrets=DaoManager.getInstance().getDaoSession().getSecretDao().queryBuilder().where(SecretDao.Properties.Cloud.eq(false)).list();
        for(int i=0;i<secrets.size();i++){
           secrets.get(i).setCloud(true);
            update(secrets.get(i));
        }

    }
    public static List<Secret> querycloud() {
        List<Secret> secrets=DaoManager.getInstance().getDaoSession().getSecretDao().queryBuilder().where(SecretDao.Properties.Cloud.eq(false)).list();
        for(int i=0;i<secrets.size();i++){
            secrets.get(i).setSecrets(SecretListHelp.query(secrets.get(i).getId()));
        }
        return secrets;
    }
    public static Long getlastid(){
        List<Secret> secrets=querySecretAll();
        return secrets.get(secrets.size()-1).getId();
    }

    /**


    /**
     * @param offset
     * @return
     * 分页加载20条数据，getTwentyRec(int offset)中控制页数offset++即可
     */
    public static List<Secret> getTwentyRec(int offset) {
        return DaoManager.getInstance().getDaoSession().getSecretDao().queryBuilder().offset(offset*20).limit(20).list();
    }
}
