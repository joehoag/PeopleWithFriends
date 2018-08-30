package com.joeh.peoplewithfriends;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import retrofit2.Response;

import static org.junit.Assert.*;

public class PersonViewModelTest implements Observer<PersonViewModel.QueryResult> {

    private PersonViewModel.QueryResult mQueryResult;

    /** Allows MutableLiveData.postData() calls to be propagated immediately */
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp()
    {
        mQueryResult = null;
    }

    /** Tests that a call failure elicits the correct response from the ViewModel */
    @Test
    public void testCallFailure()
    {
        PersonViewModel vm = new PersonViewModel();
        vm.mPersonLiveData.observeForever( this );
        vm.onFailure( null, new Exception("foo") );
        assertNotNull( "Should have seen a query result", mQueryResult );
        assertNull( "Should not have seen data in result", mQueryResult.personData );
        assertNotNull( "Should have seen error message in result", mQueryResult.errorMessage );
    }

    /** Tests that a successful response elicits the correct response from the ViewModel */
    @Test
    public void testCallSuccess()
    {
        PersonViewModel vm = new PersonViewModel();
        vm.mPersonLiveData.observeForever( this );
        Person person = new Person();
        vm.onResponse( null, Response.success( person ) );
        assertNotNull( "Should have seen a query result", mQueryResult );
        assertNotNull( "Should have seen data in result", mQueryResult.personData );
        assertNull( "Should not have seen error message in result", mQueryResult.errorMessage );
    }

    /**
     * Called when the data is changed.
     *
     * @param queryResult The new data
     */
    @Override
    public void onChanged(@Nullable PersonViewModel.QueryResult queryResult) {
        mQueryResult = queryResult;
    }
}
