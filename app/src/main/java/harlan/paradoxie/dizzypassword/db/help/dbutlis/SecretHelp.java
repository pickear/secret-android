package harlan.paradoxie.dizzypassword.db.help.dbutlis;

import android.util.Log;

import java.util.List;

import harlan.paradoxie.dizzypassword.dbdomain.Secret;
import harlan.paradoxie.dizzypassword.dbdomain.SecretList;
import harlan.paradoxie.dizzypassword.gen.SecretDao;
import harlan.paradoxie.dizzypassword.gen.SecretListDao;
import harlan.paradoxie.dizzypassword.help.GsonUtil;
import harlan.paradoxie.dizzypassword.util.SPUtils;
import harlan.paradoxie.dizzypassword.util.StringUtils;

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
        Log.e("backinfo", "要删除的数据id:" + id);
        DaoManager.getInstance().getDaoSession().getSecretDao().deleteByKey(id);
        List<SecretList> secretLists = DaoManager.getInstance().getDaoSession().getSecretListDao().queryBuilder().where(SecretListDao.Properties.SecretId.eq(id)).build().list();
        for (SecretList secretList : secretLists) {
            DaoManager.getInstance().getDaoSession().getSecretListDao().delete(secretList);
        }
        //DaoManager.getInstance().getDaoSession().getSecretListDao().queryBuilder().where(SecretListDao.Properties.SecretId.eq(id)).buildDelete();
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
        DaoManager.getInstance().getDaoSession().getSecretListDao().deleteAll();
    }
    /**
     * 更新数据
     *
     * @param
     */
    public static void update(Secret secret,List<SecretList> secres) {
        Log.e("backinfo", "要改变父类"+GsonUtil.getGsonInstance().toJson(secret));
        Log.e("backinfo", "要改变子类"+GsonUtil.getGsonInstance().toJson(secres));
        Log.e("backinfo", "要改变父类id"+secret.getId());
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
        return DaoManager.getInstance().getDaoSession().getSecretDao().queryBuilder().where(SecretDao.Properties.Deleted.eq(false)).list();
    }
    public static List<SecretList> querySecretListAll() {
        return DaoManager.getInstance().getDaoSession().getSecretListDao().queryBuilder().list();
    }
    /**
     * 查询条件为Type=TYPE_LOVE的数据
     *
     * @return
     */
    public static Secret query(Long id) {
        Secret secret=DaoManager.getInstance().getDaoSession().getSecretDao().queryBuilder().where(SecretDao.Properties.Id.eq(id)).unique();
        return secret;
    }
    public static List<Secret> queryall() {
        List<Secret> secrets = null;
        if(StringUtils.isEmpty(SPUtils.getInstance().getString("username",""))){
            secrets =DaoManager.getInstance().getDaoSession().getSecretDao().queryBuilder().list();
        }else{
            secrets =DaoManager.getInstance().getDaoSession().getSecretDao().queryBuilder().where(SecretDao.Properties.Username.eq(SPUtils.getInstance().getString("username",""))).list();

        }

        for(int i=0;i<secrets.size();i++){
            secrets.get(i).setSecrets(SecretListHelp.query(secrets.get(i).getSid()));
        }
        return secrets;
    }
    public static List<Secret> queryallCound() {
        List<Secret> secrets=DaoManager.getInstance().getDaoSession().getSecretDao().queryBuilder().where(SecretDao.Properties.Id.eq(-1)).list();
        for(int i=0;i<secrets.size();i++){
            secrets.get(i).setSecrets(SecretListHelp.query(secrets.get(i).getSid()));
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
        List<Secret> secrets=DaoManager.getInstance().getDaoSession().getSecretDao().queryBuilder().list();
        for(int i=0;i<secrets.size();i++){
            secrets.get(i).setSecrets(SecretListHelp.query(secrets.get(i).getSid()));
        }
        return secrets;
    }
    public static Long getlastid(){
        List<Secret> secrets=querySecretAll();
        return secrets.get(secrets.size()-1).getSid();
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
