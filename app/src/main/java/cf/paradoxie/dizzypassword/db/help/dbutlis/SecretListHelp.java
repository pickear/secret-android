package cf.paradoxie.dizzypassword.db.help.dbutlis;

import java.util.List;

import cf.paradoxie.dizzypassword.dbdomain.SecretList;
import cf.paradoxie.dizzypassword.gen.SecretListDao;

/**
 * Created by a1 on 2017/11/28.
 */
public class SecretListHelp {
    /**
     * 添加数据
     *
     * @param
     */
    public static void insert(SecretList secretList) {
        DaoManager.getInstance().getDaoSession().getSecretListDao().insert(secretList);
    }

    /**
     * 添加数据
     *
     * @param
     */
    public static void insertList(List<SecretList> secrets) {
        DaoManager.getInstance().getDaoSession().getSecretListDao().insertInTx(secrets);
    }
    /**
     * 删除数据
     *
     * @param id
     */
    public static void delete(long id) {
        DaoManager.getInstance().getDaoSession().getSecretListDao().deleteByKey(id);
    }
    /**
     * 删除数据
     *
     * @param
     */
    public static void deleteall() {
        DaoManager.getInstance().getDaoSession().getSecretListDao().deleteAll();
    }
    /**
     * 更新数据
     *
     * @param
     */
    public static void update(SecretList secret) {
        DaoManager.getInstance().getDaoSession().getSecretListDao().update(secret);
    }
    public static SecretListDao customerDao() {
        return DaoManager.getInstance().getDaoSession().getSecretListDao();
    }

    /**
     * 查询条件为Type=TYPE_LOVE的数据
     *
     * @return
     */
    public static List<SecretList> querySecretAll() {
        return DaoManager.getInstance().getDaoSession().getSecretListDao().queryBuilder().list();
    }

    /**
     * 查询条件为Type=TYPE_LOVE的数据
     *
     * @return
     */
    public static List<SecretList> query(Long id) {
        return DaoManager.getInstance().getDaoSession().getSecretListDao().queryBuilder().where(SecretListDao.Properties.SecretId.eq(id)).list();
    }

    /**
     * 查询条件为Type=TYPE_LOVE的数据
     *  List<Student> list = msgBeanDao.queryBuilder()
     .offset(1)//偏移量，相当于 SQL 语句中的 skip
     .limit(3)//只获取结果集的前 3 个数据
     .orderAsc(StudentMsgBeanDao.Properties.StudentNum)//通过 StudentNum 这个属性进行正序排序
     .where(StudentMsgBeanDao.Properties.Name.eq("zone"))//数据筛选，只获取 Name = "zone" 的数据。
     .build()
     .list();
     * @return
     */
    public static List<SecretList> queryCustomer(String name) {
        return DaoManager.getInstance().getDaoSession().getSecretListDao().queryBuilder().where(SecretListDao.Properties.Name.eq(name)).list();
    }

    /**
     * @param offset
     * @return
     * 分页加载20条数据，getTwentyRec(int offset)中控制页数offset++即可
     */
    public static List<SecretList> getTwentyRec(int offset) {
        return DaoManager.getInstance().getDaoSession().getSecretListDao().queryBuilder().offset(offset*20).limit(20).list();
    }
}
