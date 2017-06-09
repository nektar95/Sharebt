package com.application.nektar.debtsaver.navigation.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.application.nektar.debtsaver.R;
import com.application.nektar.debtsaver.data.SingleDebt;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Locale;

/**
 * Created by Aleksander on 08.06.2017.
 */

public class DebtHolder extends RecyclerView.ViewHolder{
    private RelativeLayout mLinearLayout;
    private TextView mNameTextView;
    private TextView mValueTextView;
    private ImageView mCheckImage;
    private ImageView mUserPicture;

    public void clearAnimation(){
        mLinearLayout.clearAnimation();
    }

    public DebtHolder(View itemView) {
        super(itemView);
        mLinearLayout = (RelativeLayout) itemView.findViewById(R.id.single_debt_linear_layout);
        mNameTextView = (TextView) itemView.findViewById(R.id.single_debt_name);
        mValueTextView = (TextView) itemView.findViewById(R.id.single_debt_value);
        mCheckImage = (ImageView) itemView.findViewById(R.id.single_debt_check);
        mUserPicture = (ImageView) itemView.findViewById(R.id.single_debt_user_picture);
    }

    public void bindResult(final SingleDebt singleDebt){
        mNameTextView.setText(singleDebt.getName());
        mValueTextView.setText(String.format(Locale.getDefault(),"%.2f",singleDebt.getValue()));
        if(singleDebt.getValue()>0){
            mValueTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.green));
        } else {
            mValueTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.red));
        }

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        if (singleDebt.getUserList().size()==1) {
            storageRef.child(singleDebt.getUserList().get(0).getName() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(getContext()).load(uri)
                            .resize(100, 100)
                            .centerCrop()
                            .onlyScaleDown()
                            .into(mUserPicture, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Bitmap imageBitmap = ((BitmapDrawable) mUserPicture.getDrawable()).getBitmap();
                                    RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                                    imageDrawable.setCircular(true);
                                    imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);

                                }

                                @Override
                                public void onError() {
                                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_circle_black_48dp);
                                    mUserPicture.setImageDrawable(new BitmapDrawable(getResources(), bitmap));
                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        } else {
            //+1 image and opening animation with all users later
        }

        mCheckImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDebtsList.remove(singleDebt);
                String idItem = mKeyList.get(singleDebt);

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

                String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                mDatabase.child(id).child("debtsList").child(idItem).setValue(null);

                mKeyList.remove(singleDebt);
                mDebtAdapter.notifyDataSetChanged();
            }
        });
    }
}
