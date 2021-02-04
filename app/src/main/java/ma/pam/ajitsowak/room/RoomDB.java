package ma.pam.ajitsowak.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {CartItem.class,FavModel.class,OrderItem.class},version = 1,exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    private static RoomDB instance;
    public abstract DataDao Dao();

    public static synchronized RoomDB getInstance(Context context){
        if (instance == null)
            instance= Room.databaseBuilder(context,RoomDB.class,"mydbname").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        return instance;
    }

}
