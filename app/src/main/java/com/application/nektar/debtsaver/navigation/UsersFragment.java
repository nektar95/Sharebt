package com.application.nektar.debtsaver.navigation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.application.nektar.debtsaver.DebtContainer;
import com.application.nektar.debtsaver.R;
import com.application.nektar.debtsaver.data.SingleDebt;
import com.application.nektar.debtsaver.data.SingleUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pc on 15.03.2017.
 */

public class UsersFragment extends Fragment {

    @BindView(R.id.recycler_view_home_fragment) RecyclerView mUsersRecyclerView;
    private UserAdapter mUsersAdapter;
    private List<SingleUser> mUserList;

    public static UsersFragment newInstance(){
        return new UsersFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.users_fragment, container, false);
        ButterKnife.bind(this,view);

        mUserList = new ArrayList<>();

        mUsersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mUsersAdapter = new UserAdapter(mUserList);
        mUsersRecyclerView.setAdapter(mUsersAdapter);

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

        //mId =FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SingleUser user = new SingleUser(dataSnapshot.getKey());
                mUserList.add(user);
                mUsersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }

    private class UserHolder extends RecyclerView.ViewHolder{
        private RelativeLayout mLinearLayout;
        private TextView mNameTextView;
        private TextView mValueTextView;
        private ImageView mProfileImage;

        public void clearAnimation(){
            mLinearLayout.clearAnimation();
        }

        public UserHolder(View itemView) {
            super(itemView);
            mProfileImage = (ImageView) itemView.findViewById(R.id.user_picture_single);

        }

        public void bindResult(final SingleUser singleUser){

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            storageRef.child(singleUser.getName()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(getContext()).load(uri)
                            .resize(200, 200)
                            .centerCrop()
                            .onlyScaleDown()
                            .into(mProfileImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Bitmap imageBitmap = ((BitmapDrawable) mProfileImage.getDrawable()).getBitmap();
                                    RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                                    imageDrawable.setCircular(true);
                                    imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                                    DebtContainer.get().setPhotoBitmap(imageBitmap);
                                    mProfileImage.setImageDrawable(imageDrawable);

                                }

                                @Override
                                public void onError() {
                                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_circle_black_48dp);
                                    mProfileImage.setImageDrawable(new BitmapDrawable(getResources(), bitmap));
                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
    }

    private class UserAdapter extends RecyclerView.Adapter<UserHolder> {
        private List<SingleUser> mSingleUsers;
        private int lastPosition = -1;

        public UserAdapter(List<SingleUser> singleDebts) {
            mSingleUsers = singleDebts;
        }

        @Override
        public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.users_single_item,parent,false);
            return new UserHolder(view);
        }

        @Override
        public void onBindViewHolder(UserHolder holder, int position) {
            SingleUser singleDebt = mSingleUsers.get(position);
            holder.bindResult(singleDebt);
        }

        @Override
        public void onViewDetachedFromWindow(UserHolder holder) {
            super.onViewDetachedFromWindow(holder);
            holder.clearAnimation();
        }

        @Override
        public int getItemCount() {
            return mSingleUsers.size();
        }
    }
}
