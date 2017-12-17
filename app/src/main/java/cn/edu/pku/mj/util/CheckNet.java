package cn.edu.pku.mj.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by MJ on 2017/12/16.
 */

public class CheckNet {
    public static final int NET_NONE=0;
    public static final int NET_WIFI=1;
    public static final int NET_MOBILE=2;

    public static int getNetState(Context context){
        ConnectivityManager connectivityManager=
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if (networkInfo==null) {
            return NET_NONE;
        }
        int type=networkInfo.getType();
        if(type==ConnectivityManager.TYPE_MOBILE){
            return NET_MOBILE;
        } else if (type == ConnectivityManager.TYPE_WIFI){
            return NET_WIFI;
        }
        return NET_NONE;
    }
}
