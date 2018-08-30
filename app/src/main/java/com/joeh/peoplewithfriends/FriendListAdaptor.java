package com.joeh.peoplewithfriends;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * The list adapter for the friends list on the main mage
 */
public class FriendListAdaptor extends BaseAdapter {

    private List<Person> mFriendList; // The friend list to display
    private AppCompatActivity mContext; // The (activity) context from which we are displaying

    /**
     * Create/construct a FriendListAdapter
     * @param context The nesting context
     * @param friendList The list of friends to display
     */
    public FriendListAdaptor(AppCompatActivity context, List<Person> friendList )
    {
        mContext = context;
        mFriendList = friendList;
    }

    @Override
    public int getCount() {
        return mFriendList == null ? 0 : mFriendList.size();
    }

    @Override
    public Object getItem(int i) {
        return mFriendList.get( i );
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        // If the view has not yet been instantiated, do so, also creating the view holder and associating
        // it with the view.
        if(view == null)
        {
            view = LayoutInflater.from(mContext).inflate(R.layout.main_list_item, viewGroup, false);
            view.setTag( new ViewHolder( view ) );
        }

        // Retrieve our ViewHolder and Person objects
        ViewHolder holder = (ViewHolder) view.getTag();
        final Person person = mFriendList.get( i );

        // Render the view elements
        holder.mNameText.setText( capitalize( person.getFirstName() ) + " " + capitalize( person.getLastName() ) );
        holder.mEmailText.setText( person.getEmail() );
        Picasso.get().load( person.getImageURL() ).placeholder( R.drawable.ic_person_black_24dp ).into( holder.mImageView );

        // Alternate the background colors on odd/even list members
        view.setBackgroundColor( (i % 2 == 0) ? Color.CYAN : Color.YELLOW );

        // Handle clicks
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Grab our shared ViewModel and instruct it to grab the info for the clicked person.
                // This will result in the main activity getting updated.
                PersonViewModel vm = ViewModelProviders.of( mContext ).get( PersonViewModel.class );
                vm.grabSpecificPersonData( person.getId() );
            }
        });

        return view;
    }

    /**
     * Class to support ViewHolder pattern
     */
    private class ViewHolder
    {
        ImageView mImageView;
        TextView mNameText;
        TextView mEmailText;

        public ViewHolder( View base )
        {
            mImageView = base.findViewById( R.id.list_image );
            mNameText = base.findViewById( R.id.list_name );
            mEmailText = base.findViewById( R.id.list_email );
        }
    }

    /**
     * Utility function to capitalize first letter of string
     */
    private String capitalize( String s )
    {
        if( s == null || s.trim().isEmpty() )
        {
            return "";
        }

        char[] charArray = s.trim().toCharArray();
        charArray[0] = Character.toUpperCase(charArray[0]);
        return new String(charArray);
    }
}
