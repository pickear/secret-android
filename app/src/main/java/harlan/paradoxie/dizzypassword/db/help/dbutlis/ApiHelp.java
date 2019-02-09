package harlan.paradoxie.dizzypassword.db.help.dbutlis;

import java.util.List;

import harlan.paradoxie.dizzypassword.dbdomain.ApiUrl;
import harlan.paradoxie.dizzypassword.dbdomain.Secret;
import harlan.paradoxie.dizzypassword.gen.SecretDao;

/**
 * Created by Dylan on 2019/2/6.
 */

public class ApiHelp {
    public static void insertdate(ApiUrl apiUrl) {
        DaoManager.getInstance().getDaoSession().getApiUrlDao().insert(apiUrl);

    }

    public static List<ApiUrl> queryApiUrlAll() {
        return DaoManager.getInstance().getDaoSession().getApiUrlDao().queryBuilder().list();
    }
}
