package com.joeh.peoplewithfriends;

import android.support.annotation.NonNull;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton retrofit client
 */
public class RetrofitClient {

    private OkHttpClient mOkHttpClient;
    private Retrofit mRetrofit;
    private PersonService mService;

    // Our singleton instance
    private static RetrofitClient sInstance;

    /** Instance initializer; typically called during app's onCreate() */
    public static void initialize( @NonNull OkHttpClient client )
    {
        sInstance = new RetrofitClient( client );
    }

    /** Retrieve singleton RetrofitClient */
    public static RetrofitClient getInstance()
    {
        return sInstance;
    }

    /** Private constructor for singleton instance */
    private RetrofitClient( @NonNull OkHttpClient okHttpClient )
    {
        mOkHttpClient = okHttpClient;

        mRetrofit = new Retrofit.Builder()
                .baseUrl( "https://interview-api.shiftboard.com" )
                .client( mOkHttpClient )
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = mRetrofit.create( PersonService.class );
    }

    /**
     * Call to retrieve a random person
     * @param listener The listener to which to send the returned Person info
     */
    public void getRandomPerson( Callback<Person> listener )
    {
        Call<Person> call = mService.getRandomPerson();
        call.enqueue( listener );
    }

    /**
     * Call to retrieve a specific person
     * @param id The id of the person for whom infomation should be retrieved
     * @param listener The listener to which to send the returned Person info
     */
    public void getSpecificPerson( String id, Callback<Person> listener )
    {
        Call<Person> call = mService.getSpecificPerson( id );
        call.enqueue( listener );
    }

    /**
     * Retrieve OkHttpClient.  This is meant for use by Espresso logic, specifically the OkHttpIdlingResource.
     * @return
     */
    public OkHttpClient getOkHttpClient()
    {
        return mOkHttpClient;
    }

}
