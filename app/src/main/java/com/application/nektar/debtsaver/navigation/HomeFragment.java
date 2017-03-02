package com.application.nektar.debtsaver.navigation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.nektar.debtsaver.DebtContainer;
import com.application.nektar.debtsaver.NavigationActivity;
import com.application.nektar.debtsaver.R;
import com.application.nektar.debtsaver.login.LoginActivity;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pc on 23.02.2017.
 */

public class HomeFragment extends Fragment {
    private RecyclerView mDebtsRecyclerView;
    private Button mSignOutButton;
    private ProfilePictureView mProfilePictureView;
    private GraphRequestAsyncTask mRequestAsyncTask;
    private TextView mNameTextView;
    private ImageView mProfileImageView;

    public static HomeFragment newInstance(){
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_fragment, container, false);

        mSignOutButton = (Button) view.findViewById(R.id.home_sign_out);
        mProfilePictureView = (ProfilePictureView) view.findViewById(R.id.profile_picture_home);
        mNameTextView = (TextView) view.findViewById(R.id.home_name_textview);
        mProfileImageView = (ImageView) view.findViewById(R.id.profile_picture_home_another);

        mRequestAsyncTask = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if(object != null){
                    mProfilePictureView.setProfileId(object.optString("id"));
                    Log.i("d",response.getJSONObject().toString());
                    try {
                        DebtContainer.get().setPhotoUrl(response.getJSONObject().getJSONObject("picture").getJSONObject("data").getString("url"));
                    } catch (JSONException je){
                        je.printStackTrace();
                    }
                    mProfilePictureView.setVisibility(View.VISIBLE);
                    mNameTextView.setText(object.optString("name"));
                } else {
                    //primtive way
                    mNameTextView.setText(DebtContainer.get().getName());
                    mProfileImageView.setVisibility(View.VISIBLE);
                }
            }

        }).executeAsync();
        //mProfileImageView.setVisibility(View.VISIBLE);
        Picasso.with(getContext()).load(DebtContainer.get().getPhotoUrl())
                .resize(200,200)
                .centerCrop()
                .onlyScaleDown()
                .into(mProfileImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) mProfileImageView.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        mProfileImageView.setImageDrawable(imageDrawable);
                    }

                    @Override
                    public void onError() {

                    }
                });


        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                /*
                FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if(user == null){
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            getActivity().finish();
                        }
                    }
                };
                */
            }
        });

        return view;
    }
}
