package ma.pam.ajitsowak.room;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;


public class Ex1  extends AppCompatActivity {
    private static final String TAG = "ex1hh";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RoomDB room =  RoomDB.getInstance(this);



//        AttributeChild ff =  new AttributeChild(pp);

        /*room.postDao().insertPost(ff);
        List<PostRoom> postList = room.postDao().getallPosts();

        for (PostRoom post :postList) {
            Log.d(TAG, post.toString());
        }*/
    }


}