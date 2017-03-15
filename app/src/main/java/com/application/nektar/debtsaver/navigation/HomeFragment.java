package com.application.nektar.debtsaver.navigation;

import android.content.Intent;
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
import com.application.nektar.debtsaver.login.LoginActivity;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.firebase.client.Firebase;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by pc on 23.02.2017.
 */

public class HomeFragment extends Fragment {
    private RecyclerView mDebtsRecyclerView;
    private ImageView mSignOutButton;
    private GraphRequest mGraphRequest;
    private TextView mNameTextView;
    private ImageView mProfileImageView;
    private List<SingleDebt> mDebtsList;
    private DebtAdapter mDebtAdapter;
    private Map<SingleDebt,String> mKeyList;
    private String mId;

    public static HomeFragment newInstance(){
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_fragment, container, false);

        mSignOutButton = (ImageView) view.findViewById(R.id.home_sign_out);
        mNameTextView = (TextView) view.findViewById(R.id.home_name_textview);
        mProfileImageView = (ImageView) view.findViewById(R.id.profile_picture_home_another);
        mDebtsRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_home_fragment);

        mDebtsList = new ArrayList<>();
        mKeyList = new HashMap<>();

        mDebtsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDebtAdapter = new DebtAdapter(mDebtsList);
        mDebtsRecyclerView.setAdapter(mDebtAdapter);

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

        mId =FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase.child(mId).child("debtsList").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SingleDebt debt = dataSnapshot.getValue(SingleDebt.class);
                mKeyList.put(debt,dataSnapshot.getKey());
                mDebtsList.add(debt);
                mDebtAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                mKeyList.remove(dataSnapshot.getKey());
                SingleDebt debt = dataSnapshot.getValue(SingleDebt.class);
                mDebtsList.remove(debt);
                mDebtAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*
        mDatabase.child(id).child("debtsList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.i("DDD",snapshot.getValue().toString());
                    SingleDebt debt = snapshot.getValue(SingleDebt.class);
                    if(!mDebtsList.contains(debt)){
                        mDebtsList.add(debt);
                    }
                }

                mDebtAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */
        mGraphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if(object != null){
                    try {
                        DebtContainer.get().setPhotoUrl(object.getJSONObject("picture").getJSONObject("data").getString("url"));
                    }
                    catch (JSONException je){
                        je.printStackTrace();
                    }
                    mNameTextView.setText(object.optString("name"));
                } else {
                    mNameTextView.setText(DebtContainer.get().getName());
                }


                if(DebtContainer.get().getPhotoUrl().isEmpty()){
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_account_circle_black_48dp);
                    mProfileImageView.setImageDrawable(new BitmapDrawable(getResources(),bitmap));
                } else {
                    Picasso.with(getContext()).load(DebtContainer.get().getPhotoUrl())
                            .resize(200, 200)
                            .centerCrop()
                            .onlyScaleDown()
                            .into(mProfileImageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Bitmap imageBitmap = ((BitmapDrawable) mProfileImageView.getDrawable()).getBitmap();
                                    RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                                    imageDrawable.setCircular(true);
                                    imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                                    DebtContainer.get().setPhotoBitmap(imageBitmap);
                                    mProfileImageView.setImageDrawable(imageDrawable);

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] data = baos.toByteArray();

                                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                    StorageReference mountainsRef = storageRef.child(mId+".jpg");

                                    UploadTask uploadTask = mountainsRef.putBytes(data);
                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle unsuccessful uploads
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                        }
                                    });

                                }

                                @Override
                                public void onError() {
                                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_circle_black_48dp);
                                    mProfileImageView.setImageDrawable(new BitmapDrawable(getResources(), bitmap));
                                }
                            });
                }
            }

        });

        Bundle params = new Bundle();
        params.putString("fields", "id,name,picture.type(large)");

        mGraphRequest.setParameters(params);
        mGraphRequest.executeAsync();

        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                DebtContainer.get().clearCache();
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

    private class DebtHolder extends RecyclerView.ViewHolder{
        private RelativeLayout mLinearLayout;
        private TextView mNameTextView;
        private TextView mValueTextView;
        private ImageView mCheckImage;

        public void clearAnimation(){
            mLinearLayout.clearAnimation();
        }

        public DebtHolder(View itemView) {
            super(itemView);
            mLinearLayout = (RelativeLayout) itemView.findViewById(R.id.single_debt_linear_layout);
            mNameTextView = (TextView) itemView.findViewById(R.id.single_debt_name);
            mValueTextView = (TextView) itemView.findViewById(R.id.single_debt_value);
            mCheckImage = (ImageView) itemView.findViewById(R.id.single_debt_check);
        }

        public void bindResult(final SingleDebt singleDebt){
            mNameTextView.setText(singleDebt.getName());
            mValueTextView.setText(String.format(Locale.getDefault(),"%.2f",singleDebt.getValue()));
            if(singleDebt.getValue()>0){
                mValueTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.green));
            } else {
                mValueTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.red));
            }
            mCheckImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDebtsList.remove(singleDebt);
                    String idItem = mKeyList.get(singleDebt);

                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

                    String id =FirebaseAuth.getInstance().getCurrentUser().getUid();
                    mDatabase.child(id).child("debtsList").child(idItem).setValue(null);

                    mKeyList.remove(singleDebt);
                    mDebtAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private class DebtAdapter extends RecyclerView.Adapter<DebtHolder> {
        private List<SingleDebt> mSingleDebts;
        private int lastPosition = -1;

        public DebtAdapter(List<SingleDebt> singleDebts) {
            mSingleDebts = singleDebts;
        }

        @Override
        public DebtHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.single_debt_item,parent,false);
            return new DebtHolder(view);
        }

        @Override
        public void onBindViewHolder(DebtHolder holder, int position) {
            SingleDebt singleDebt = mSingleDebts.get(position);
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
        public void onViewDetachedFromWindow(DebtHolder holder) {
            super.onViewDetachedFromWindow(holder);
            holder.clearAnimation();
        }

        @Override
        public int getItemCount() {
            return mSingleDebts.size();
        }
    }
}
