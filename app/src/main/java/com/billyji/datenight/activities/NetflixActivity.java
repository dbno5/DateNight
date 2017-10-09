package com.billyji.datenight.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.billyji.datenight.ApiClient;
import com.billyji.datenight.Movie;
import com.billyji.datenight.NetflixRunner2;
import com.billyji.datenight.PopularMoviesResponse;
import com.billyji.datenight.R;
import com.billyji.datenight.TMDBApiInterface.ApiInterface;
import com.net.codeusa.NetflixRoulette;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetflixActivity extends AppCompatActivity
{
    private Call<PopularMoviesResponse> mPopularMoviesCall;
    private int currentPage = 1;
    private List<Movie> m_movieList = new ArrayList<>();
    private NetflixRunner m_netflixRunner;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.food_list);
        loadActivity();

    }

    private void loadActivity()
    {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        mPopularMoviesCall = apiService.getPopularMovies(getResources().getString(R.string.movie_db_api_key), currentPage, "US");
        mPopularMoviesCall.enqueue(new Callback<PopularMoviesResponse>()
        {
            @Override
            public void onResponse(Call<PopularMoviesResponse> call, Response<PopularMoviesResponse> response)
            {
                if (!response.isSuccessful())
                {
                    mPopularMoviesCall = call.clone();
                    mPopularMoviesCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;
                if (response.body().getResults() == null) return;

                for (Movie movie : response.body().getResults())
                {
                    if (movie != null)
                        m_movieList.add(movie);
                }

                m_netflixRunner = new NetflixRunner(m_movieList);

                for (Movie movie : m_movieList)
                {
                    Log.e("movie", movie.getTitle());
                }

                for(String title : NetflixRunner2.movieList)
                {
                    Log.e("netflixc ", title);

                }
//                mMoviesAdapter.notifyDataSetChanged();
//                if (response.body().getPage() == response.body().getTotalPages())
//                    pagesOver = true;
//                else
//                    presentPage++;
            }

            @Override
            public void onFailure(Call<PopularMoviesResponse> call, Throwable t)
            {

            }
        });
    }


    private static class NetflixRunner extends AsyncTask<String, Context, List<Movie>>
    {
        private NetflixRunner2 m_netflixRunner = new NetflixRunner2();
        private String m_id;
        private List<Movie> movieList;
        private List<Movie> netflixMovieList = new ArrayList<>();

        NetflixRunner(List<Movie> list)
        {
            movieList = list;
        }

        @Override
        protected List<Movie> doInBackground(String... strings)
        {
            for(Movie movie : movieList)
            {
                try
                {
                    m_netflixRunner.findTitle(movie.getTitle());
                }
                catch (IOException e)
                {
                    Log.e("tits", "tats");
                }
                catch (JSONException e)
                {
                    Log.e("tit222s", "tats");

                }
            }

            return movieList;
        }
    }
}
