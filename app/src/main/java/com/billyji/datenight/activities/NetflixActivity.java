package com.billyji.datenight.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.billyji.datenight.ApiClient;
import com.billyji.datenight.Movie;
import com.billyji.datenight.PopularMoviesResponse;
import com.billyji.datenight.R;
import com.billyji.datenight.TMDBApiInterface.ApiInterface;

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

    public enum Genres{
        ACTION("28"), ADVENTURE("16"), ANIMATION("16"),
        COMEDY("35"), DOCUMENTARY("99"), HORROR("27"),
        ROMANCE("10749"), SCIFI("878");

        private String genreId;

        Genres(String genre){
            this.genreId = genre;
        }

        public String getGenre()
        {
            return genreId;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.netflix_main);

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
            }

            @Override
            public void onFailure(Call<PopularMoviesResponse> call, Throwable t)
            {

            }
        });



    }
}
