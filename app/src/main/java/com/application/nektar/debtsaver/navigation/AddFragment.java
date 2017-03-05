package com.application.nektar.debtsaver.navigation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.application.nektar.debtsaver.R;
import com.application.nektar.debtsaver.data.SingleDebt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;

/**
 * Created by pc on 23.02.2017.
 */

public class AddFragment extends Fragment {
    private Button mAddButton;
    private EditText mNameEdit;
    private EditText mValueEdit;
    private Switch mSwitch;

    public static AddFragment newInstance(){
        return new AddFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_fragment, container, false);

        mAddButton = (Button) view.findViewById(R.id.add_fragment_debt_button);
        mNameEdit = (EditText) view.findViewById(R.id.add_fragment_debt_name);
        mValueEdit = (EditText) view.findViewById(R.id.add_fragment_debt_value);
        mSwitch = (Switch) view.findViewById(R.id.add_fragment_debt_switch);

        mValueEdit.addTextChangedListener(new TextWatcher() {
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
            private String current = "";

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(!s.toString().equals(current))
                {
                    mValueEdit.removeTextChangedListener(this);

                    int selection = mValueEdit.getSelectionStart();

                    String replaceable = String.format("[%s,\\s]", NumberFormat.getCurrencyInstance().getCurrency().getSymbol());
                    String cleanString = s.toString().replaceAll(replaceable, "");

                    double price;

                    try
                    {
                        price = Double.parseDouble(cleanString);
                    }
                    catch(java.lang.NumberFormatException e)
                    {
                        price = 0;
                    }

                    int shrink = 1;
                    if(!(s.toString().contains(".")))
                    {
                        shrink = 100;
                    }

                    String formated = currencyFormat.format((price / shrink));

                    current = formated;
                    mValueEdit.setText(formated);
                    mValueEdit.setSelection(Math.min(selection, mValueEdit.getText().length()));

                    mValueEdit.addTextChangedListener(this);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

        });

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

                Double doubleValue = Double.valueOf(value)/100;

                if(mSwitch.isChecked()){
                    doubleValue = doubleValue*(-1);
                }

                SingleDebt debt = new SingleDebt(mNameEdit.getText().toString(),doubleValue);
                mDatabase.child(id).child("debtsList").push().setValue(debt);

                mNameEdit.setText("");
                mValueEdit.setText("");
            }
        });

        return view;
    }
}
