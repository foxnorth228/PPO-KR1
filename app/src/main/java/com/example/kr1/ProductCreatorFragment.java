package com.example.kr1;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProductCreatorFragment extends Fragment {
    String url = null;
    int visibility = View.VISIBLE;
    public ProductCreatorFragment(){
        super(R.layout.fragment_product_creator);
    }

    public void setBitmap(String s) {
        url = s;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_creator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button b = view.findViewById(R.id.c_backButton);
        Button createButton = view.findViewById(R.id.c_createButton);
        ImageView i = view.findViewById(R.id.c_productImage);
        i.setOnClickListener((View v) -> {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                ((MainActivity) getActivity()).startFileManager();
            } else {
                ((ProductEditorActivity) getActivity()).startFileManager();
            }
        });
        b.setOnClickListener((View v) -> {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                reset(getActivity());
            } else {
                requireActivity().finish();
            }
        });
        createButton.setOnClickListener((View v) -> {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                createElement();
            } else {
                createElement();
            }
        });
    }

    public void changeVisibility(int change) {
        if(change != -1) {
            visibility = change;
            getView().setVisibility(visibility);
            return;
        }
        if(visibility == View.VISIBLE) {
            visibility = View.INVISIBLE;
        } else {
            visibility = View.VISIBLE;
        }
        getView().setVisibility(visibility);
    }

    private void reset(androidx.fragment.app.FragmentActivity a) {
        ((ImageView) a.findViewById(R.id.c_productImage)).setImageResource(R.drawable.none);
        ((TextView) a.findViewById(R.id.c_productNameChange)).setText("");
        ((TextView) a.findViewById(R.id.c_productCountChange)).setText("");
    }

    public void createElement() {
        EditText nameEdit = getActivity().findViewById(R.id.c_productNameChange);
        EditText countEdit = getActivity().findViewById(R.id.c_productCountChange);
        String name = nameEdit.getText().toString().trim();
        String count = countEdit.getText().toString().trim();
        if(name.equals("")) {
            return;
        }
        if(count.equals("")) {
            return;
        }
        int num;
        try {
             num = Integer.parseInt(count);
        }
        catch (Exception e){
            return;
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            MainActivity a = (MainActivity) getActivity();
            a.createElement(R.drawable.none, name, num, url);
            reset(getActivity());
        } else {
            ProductEditorActivity a = (ProductEditorActivity) getActivity();
            a.sendProduct(R.drawable.none, name, num, url);
        }
    }

    public void setName(String name) {
        if(getActivity() != null) {
            ((EditText)getActivity().findViewById(R.id.c_productNameChange)).setText(name);
        }
    }
}
