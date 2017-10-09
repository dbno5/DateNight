package com.billyji.datenight;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.net.codeusa.NetflixRoulette;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bj0716 on 10/9/17.
 */

public class NetflixRunner2
{
    private NetflixRoulette m_netflixRunner = new NetflixRoulette();
    private String m_id;
    public static List<String> movieList = new ArrayList<>();

    public void findTitle(String title) throws IOException, JSONException
    {
        m_id = m_netflixRunner.getNetflixId(title);
        if(m_id.length() >= 1)
            movieList.add(title);

        Log.e("suuup", m_id);
    }

}
