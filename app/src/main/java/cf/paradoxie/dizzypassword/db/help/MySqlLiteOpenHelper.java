package cf.paradoxie.dizzypassword.db.help;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

import cf.paradoxie.dizzypassword.gen.DaoMaster;
import cf.paradoxie.dizzypassword.gen.SecretDao;

/**
 * Created by zhangqie on 2016/3/26.
 */

public class MySqlLiteOpenHelper extends DaoMaster.OpenHelper{

    public MySqlLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory)
    {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion)
    {

        super.onUpgrade(db, oldVersion, newVersion);
        if (oldVersion < newVersion)
        {
            Log.e("backinfo","要更新数据库了");
            MigrationHelper.getInstance().migrate(db, SecretDao.class);

        }
    }
}