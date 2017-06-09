package com.application.nektar.debtsaver.navigation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.application.nektar.debtsaver.DebtContainer;
import com.application.nektar.debtsaver.R;
import com.application.nektar.debtsaver.data.SingleDebt;
import com.application.nektar.debtsaver.data.SingleUser;
import com.application.nektar.debtsaver.navigation.add_new.UserAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pc on 23.02.2017.
 */

public class AddFragment extends Fragment {
    @BindView(R.id.add_fragment_debt_button) Button mAddButton;
    @BindView(R.id.add_fragment_debt_name) EditText mNameEdit;
    @BindView(R.id.add_fragment_debt_value) EditText mValueEdit;
    @BindView(R.id.add_fragment_debt_currency) TextView mCurrencyTextView;
    @BindView(R.id.add_fragment_debt_switch) Switch mSwitch;
    @BindView(R.id.recycler_view_home_fragment) RecyclerView mUsersRecyclerView;

    private UserAdapter mUsersAdapter;
    private List<SingleUser> mUserList;
    private List<SingleUser> mCheckedUsers;
/*
    private class UserHolder extends RecyclerView.ViewHolder{
        private RelativeLayout mLinearLayout;
        private TextView mNameTextView;
        private ImageView mProfileImage;
        private boolean mChecked;

        public void clearAnimation(){
            mLinearLayout.clearAnimation();
        }

        public UserHolder(View itemView) {
            super(itemView);
            mLinearLayout = (RelativeLayout) itemView.findViewById(R.id.single_user_linear_layout);
            mProfileImage = (ImageView) itemView.findViewById(R.id.user_picture_single);
            mNameTextView = (TextView) itemView.findViewById(R.id.user_name_textview);
            mChecked = true;

        }

        public void bindResult(final SingleUser singleUser){

            mLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mChecked){
                        mLinearLayout.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.grey));
                        //mLinearLayout.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.rounded_choose_shape));
                        mCheckedUsers.add(singleUser);
                        mChecked = false;
                    } else {
                        mLinearLayout.setBackgroundColor(Color.TRANSPARENT);
                        mCheckedUsers.remove(singleUser);
                        mChecked = true;
                    }
                }
            });

            //get user name by id from info

            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");


            mDatabase.child(singleUser.getName()).child("informations").child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mNameTextView.setText(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            storageRef.child(singleUser.getName()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(getContext()).load(uri)
                            .resize(100, 100)
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

            if (position > lastPosition) {
                AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
                animation.setDuration(250);
                animation.setFillAfter(true);
                holder.mLinearLayout.startAnimation(animation);
                lastPosition = position;
            }
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
*/
    public static AddFragment newInstance(){
        return new AddFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_fragment, container, false);

        ButterKnife.bind(this, view);

        mCurrencyTextView.setText(NumberFormat.getCurrencyInstance().getCurrency().getSymbol());

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getActivity().getCurrentFocus();
                if(view != null){
                    InputMethodManager inputMethodManager=(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
                }
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

                String id =FirebaseAuth.getInstance().getCurrentUser().getUid();
                String value = mValueEdit.getText().toString();
                if(value.isEmpty()){
                    return;
                }
                value = value.replaceAll("[^0-9.]", "");

                Double doubleValue = Double.valueOf(value);

                if(mSwitch.isChecked()){
                    doubleValue = doubleValue*(-1);
                }

                SingleDebt debt = new SingleDebt(mNameEdit.getText().toString(),doubleValue);
                debt.getUserList().addAll(mCheckedUsers);
                mDatabase.child(id).child("debtsList").push().setValue(debt);

                mNameEdit.setText("");
                mValueEdit.setText("");
            }
        });

        mUserList = new ArrayList<>();
        mCheckedUsers = new ArrayList<>();

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
}
