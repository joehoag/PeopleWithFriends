package com.joeh.peoplewithfriends;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This ViewModel allows the main activity and its underlying list to perform update operations
 * without directly communicating with each other.
 *
 *
 */
public class PersonViewModel extends ViewModel implements Callback<Person> {

    /**
     * A compound object that can contain Person info or an error message
     */
    public static class QueryResult
    {
        Person personData;
        String errorMessage;

        /**
         * Create a QueryResult from an error message
         * @param errorMessage
         * @return
         */
        public static QueryResult fromErrorMessage( String errorMessage )
        {
            return new QueryResult( null, errorMessage );
        }

        /**
         * Create a QueryResult from a Throwable
         * @param throwable
         * @return
         */
        public static QueryResult fromThrowable ( Throwable throwable )
        {
            return new QueryResult( null, throwable.toString() );
        }

        /**
         * Create a QueryResult from a Person object
         * @param person
         * @return
         */
        public static QueryResult fromData( Person person )
        {
            return new QueryResult( person, null );
        }

        /**
         * Private constructor.  Need to use one of the above fromXXX static methods to
         * create a QueryResult from external code.
         * @param person
         * @param errorMessage
         */
        private QueryResult( Person person, String errorMessage )
        {
            this.personData = person;
            this.errorMessage = errorMessage;
        }
    }

    /**
     * A MutableLiveData to which to subscribe for person query updates
     */
    public MutableLiveData<QueryResult> mPersonLiveData = new MutableLiveData<>();

    /**
     * Grabs a new random person, which will result in mPersonLiveData being hit with an update
     */
    public void grabRandomPersonData()
    {
        RetrofitClient.getInstance().getRandomPerson( this );
    }

    /**
     * Grabs a new specific person, which will result in mPersonLiveData being hit with an update
     * @param id
     */
    public void grabSpecificPersonData( String id )
    {
        RetrofitClient.getInstance().getSpecificPerson( id, this );
    }

    /**
     * Invoked for a received HTTP response.
     * <p>
     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
     * Call {@link Response#isSuccessful()} to determine if the response indicates success.
     *
     * @param call
     * @param response
     */
    @Override
    public void onResponse(Call<Person> call, Response<Person> response) {
        if( response != null && response.body() != null)
        {
            mPersonLiveData.postValue( QueryResult.fromData( response.body() ) );
        }
        else
        {
            mPersonLiveData.postValue( QueryResult.fromErrorMessage( response.message() ) );
            //Log.v("PersonViewModel", "Response is null or body is null; message = " + response.message() );
        }
    }

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     *
     * @param call
     * @param t
     */
    @Override
    public void onFailure(Call<Person> call, Throwable t) {
        mPersonLiveData.postValue( QueryResult.fromThrowable( t ) );
        //Log.v( "PersonViewModel", "FAILURE: " + t.toString() );
    }
}
