package com.billyji.datenight;



import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public class TMDBApiInterface
{
    public interface ApiInterface {

        //MOVIES

        @GET("movie/popular")
        Call<PopularMoviesResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("region") String region);

        @GET("movie/top_rated")
        Call<Movie> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("region") String region);


        @GET("tv/popular")
        Call<Movie> getPopularTVShows(@Query("api_key") String apiKey, @Query("page") Integer page);


        //will implement later
//        @GET("tv/{id}/similar")
//        Call<SimilarTVShowsResponse> getSimilarTVShows(@Path("id") Integer movieId, @Query("api_key") String apiKey, @Query("page") Integer page);


    }
}
