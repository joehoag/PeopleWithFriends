package com.joeh.peoplewithfriends;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Retrofit interface to person calls.
 */
public interface PersonService {

    @GET("person")
    Call<Person> getRandomPerson();

    @GET("person/{id}")
    Call<Person> getSpecificPerson(@Path("id") String id);
}
