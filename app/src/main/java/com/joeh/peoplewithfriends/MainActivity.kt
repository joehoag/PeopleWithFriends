package com.joeh.peoplewithfriends

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * The main activity class
 */
class MainActivity : AppCompatActivity(), Observer<PersonViewModel.QueryResult>  {

    var mPersonViewModel : PersonViewModel? = null

    /**
     * Override onCreate to allow us to do some custom creation tasks
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // Make sure that we snap to the top of the scrollview after laying it out
        main_scrollview.viewTreeObserver.addOnGlobalLayoutListener( object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout()
            {
                Log.v("MainActivity", "ScrollView global layout complete")
                //main_scrollview.fullScroll( View.FOCUS_UP )
                main_scrollview.smoothScrollTo( 0,0 )
            }
        })

        // Set up our ViewModel and launch the initial request for random person data
        mPersonViewModel = ViewModelProviders.of(this).get(PersonViewModel::class.java)
        mPersonViewModel?.mPersonLiveData?.observe( this, this )
        mPersonViewModel?.grabRandomPersonData() // kick off the initial request for random person data

    }

    /**
     * Create our option menu
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /**
     * Handle menu item selections
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_new_info ->
            {
                mPersonViewModel?.grabRandomPersonData()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    /**
     * Called when a Person request has completed.
     * @param t  The new data
     */
    override fun onChanged(t: PersonViewModel.QueryResult?) {
        val person = t?.personData
        val errorMessage = t?.errorMessage

        if( person != null )
        {
            // If we have data, then render it
            val nameAndEmail = "${person.firstName.capitalize()} ${person.lastName.capitalize()}\n\n${person.email}"
            main_name_and_email.text = nameAndEmail

            Picasso.get().load(person.imageURL).into( main_image )
            main_address.text="${wordsAllCapitalized(person.address)}\n${wordsAllCapitalized(person.city)}, ${wordsAllCapitalized(person.state)} ${person.zipCode}"
            main_phone.text="${person.phoneNumber}"

            // For the friend list, (1) Only show the header if the current person has friencs, and (2) create a
            // new ListAdapter for this user.
            val friendCount = person?.friends?.size ?: 0
            main_friends_header.visibility = if ( friendCount > 0 ) View.VISIBLE else View.GONE
            main_list.adapter = FriendListAdaptor( this, person.friends )

            Log.v("friend count", "count=${person.friends?.size}" )

        }
        else if( errorMessage != null )
        {
            // For service errors, show a snackbar.
            Snackbar.make( main_address, "Service error: $errorMessage", 2000 ).show();
        }
    }

    /**
     * Utility function that capitalizes all words in the input string
     */
    private fun wordsAllCapitalized( s : String? ) : String
    {
        if( s == null )
        {
            return "";
        }

        val words = s.split(" ")
        val mappedWords = words.map( { x -> x.capitalize() } )
        val joinedWords = mappedWords.joinToString( separator = " " )
        return joinedWords
        //return words.map( { x -> x.capitalize() } ).joinToString { " " }
    }

}
