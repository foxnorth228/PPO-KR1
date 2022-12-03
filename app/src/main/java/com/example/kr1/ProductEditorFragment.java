package com.example.kr1;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class ProductEditorFragment extends Fragment {
    public ProductEditorFragment(){
        super(R.layout.fragment_product_editor);
    }
    String oldName = "";
    String bitmapUrl = null;
    int src;
    int visibility = View.INVISIBLE;

    public void setBitmap(String s) {
        bitmapUrl = s;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button b = view.findViewById(R.id.backButton);
        Button delete = view.findViewById(R.id.deleteButton);
        Button apply = view.findViewById(R.id.applyButton);
        ImageView i = view.findViewById(R.id.productImage);
        i.setOnClickListener((View v) -> {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                ((MainActivity) getActivity()).startFileManager();
            } else {
                ((ProductEditorActivity) getActivity()).startFileManager();
            }
        });
        delete.setOnClickListener((View v) -> {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                MainActivity a = (MainActivity) getActivity();
                reset(a);
                a.deleteElement();
            } else {
                ((ProductEditorActivity) getActivity()).returnMessage("DELETE");
            }
        });
        b.setOnClickListener((View v) -> {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                reset(getActivity());
            } else {
                requireActivity().finish();
            }
        });
        apply.setOnClickListener((View v) -> {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                createElement();
            } else {
                createElement();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_editor, container, false);
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
        ((ImageView) a.findViewById(R.id.productImage)).setImageResource(R.drawable.none);
        ((TextView) a.findViewById(R.id.productNameChange)).setText("");
        ((TextView) a.findViewById(R.id.productCountChange)).setText("");
    }

    public void setProduct(Product p) {
        if(p == null) {
            reset(getActivity());
            return;
        }
        bitmapUrl = p.bitmapUrl;
        src = p.imageSrc;
        if(getActivity() != null) {
            if(p.bitmapUrl == null || p.bitmapUrl.equals("")) {
                ((ImageView) getActivity().findViewById(R.id.productImage)).setImageResource(p.imageSrc);
            } else {
                Bitmap bit = BitmapFactory.decodeFile(p.bitmapUrl);
                ((ImageView) getActivity().findViewById(R.id.productImage)).setImageBitmap(bit);
            }
            oldName = p.name;
            ((TextView) getActivity().findViewById(R.id.productNameChange)).setText(p.name);
            ((TextView) getActivity().findViewById(R.id.productCountChange)).setText((p.cost).toString());
        }
    }

    public void createElement() {
        EditText nameEdit = getActivity().findViewById(R.id.productNameChange);
        EditText countEdit = getActivity().findViewById(R.id.productCountChange);
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
            a.changeElement(src, oldName, name, num, bitmapUrl);
            reset(getActivity());
        } else {
            ProductEditorActivity a = (ProductEditorActivity) getActivity();
            a.sendProductChange(src, oldName, name, num, bitmapUrl);
        }
    }
}
