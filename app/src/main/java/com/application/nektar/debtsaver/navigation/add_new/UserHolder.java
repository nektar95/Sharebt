package com.application.nektar.debtsaver.navigation.add_new;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.application.nektar.debtsaver.DebtContainer;
import com.application.nektar.debtsaver.R;
import com.application.nektar.debtsaver.data.SingleUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Aleksander on 08.06.2017.
 */

public class UserHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.single_user_linear_layout) RelativeLayout mLinearLayout;
    @BindView(R.id.user_name_textview) TextView mNameTextView;
    @BindView(R.id.user_picture_single) ImageView mProfileImage;

    private boolean mChecked;
    private Context context;

    public void clearAnimation(){
        mLinearLayout.clearAnimation();
    }

    public UserHolder(View itemView, Context context) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        mChecked = true;
        this.context = context;
    }

    public void bindResult(final SingleUser singleUser){
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mChecked){
                    mLinearLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.grey));
                    //mLinearLayout.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.rounded_choose_shape));
                    DebtContainer.get().getCheckedUsers().add(singleUser);
                    mChecked = false;
                } else {
                    mLinearLayout.setBackgroundColor(Color.TRANSPARENT);
                    DebtContainer.get().getCheckedUsers().remove(singleUser);
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
                Picasso.with(context).load(uri)
                        .resize(100, 100)
                        .centerCrop()
                        .onlyScaleDown()
                        .into(mProfileImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                Bitmap imageBitmap = ((BitmapDrawable) mProfileImage.getDrawable()).getBitmap();
                                RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), imageBitmap);
                                imageDrawable.setCircular(true);
                                imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                                DebtContainer.get().setPhotoBitmap(imageBitmap);
                                mProfileImage.setImageDrawable(imageDrawable);

                            }

                            @Override
                            public void onError() {
                                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_account_circle_black_48dp);
                                mProfileImage.setImageDrawable(new BitmapDrawable(context.getResources(), bitmap));
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
