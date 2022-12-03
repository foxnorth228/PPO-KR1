package com.example.kr1;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;


public class ListViewFragment extends Fragment {

    interface OnFragmentSendDataListener {
        void onSendData(String s, Product data);
    }

    private static boolean init = false;
    private static boolean viewInit = false;
    private OnFragmentSendDataListener fragmentSendDataListener;
    private static final ArrayList<Product> products = new ArrayList<>();
    private static final HashMap<String, Product> database = new HashMap<>();
    private static final ArrayList<String> productsName = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private static void initialize() {
        database.put("name1", new Product(R.drawable.bread, "name1", 1));
        database.put("name2", new Product(R.drawable.bread, "name2", 2));
        database.put("name3", new Product(R.drawable.bread, "name3", 3));
        database.put("name4", new Product(R.drawable.bread, "name4", 4));
        database.put("name5", new Product(R.drawable.bread, "name5", 5));
        database.put("name6", new Product(R.drawable.bread, "name6", 6));
        database.put("name7", new Product(R.drawable.bread, "name7", 7));
        database.put("name8", new Product(R.drawable.bread, "name8", 8));
        database.put("name9", new Product(R.drawable.bread, "name9", 9));
        database.put("name10", new Product(R.drawable.bread, "name10", 10));
        database.put("name11", new Product(R.drawable.bread, "name11", 11));
        database.put("name12", new Product(R.drawable.bread, "name12", 12));
        database.put("name13", new Product(R.drawable.bread, "name13", 13));
        database.put("name14", new Product(R.drawable.bread, "name14", 14));
        database.put("name15", new Product(R.drawable.bread, "name15", 15));
        database.put("name16", new Product(R.drawable.bread, "name16", 16));
        database.put("name17", new Product(R.drawable.bread, "name17", 17));
        database.put("name18", new Product(R.drawable.bread, "name18", 18));
        database.put("name19", new Product(R.drawable.bread, "name19", 19));
        database.put("name20", new Product(R.drawable.bread, "name20", 20));
    }

    public ListViewFragment() {
        super(R.layout.fragment_list_view);
        if(!init) {
            initialize();
            init = true;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            fragmentSendDataListener = (OnFragmentSendDataListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " должен реализовывать интерфейс OnFragmentInteractionListener");
        }
    }

    @MainThread
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if(!viewInit) {
            String[] str = getResources().getStringArray(R.array.product_list);
            productsName.addAll(Arrays.asList(str));
            for(int i = 0; i < database.size(); ++i) {
                products.add(database.get(productsName.get(i)));
            }
            viewInit = true;
        }
        ListView listView = view.findViewById(R.id.listView);
        EditText editText = view.findViewById(R.id.editText);

        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, productsName);
        listView.setAdapter(adapter);
        editText.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN)
                if(keyCode == KeyEvent.KEYCODE_ENTER) {
                    String name = editText.getText().toString();
                    editText.setText("");
                    InputMethodManager inputManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            editText.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        MainActivity main = (MainActivity) getActivity();
                        main.changeVisibility(true);
                        EditText e = main.findViewById(R.id.c_productNameChange);
                        e.setText(name);
                    } else {
                        MainActivity m = (MainActivity) getActivity();
                        m.createElementActivity(name);
                    }
                    return true;
                }
            return false;
        });
        listView.setOnItemClickListener((parent, item, pos, id) -> {
            MainActivity main = (MainActivity) getActivity();
            main.changeVisibility(false);
            fragmentSendDataListener.onSendData(productsName.get(pos), products.get(pos));
        });
    }

    public void addElement(Product p) {
        if(productsName.contains(p.name)) {
            return;
        }
        productsName.add(p.name);
        products.add(p);
        database.put(p.name, p);
        adapter.notifyDataSetChanged();
    }

    public void changeElement(String name, Product p) {
        if(!productsName.contains(name)) {
            return;
        }
        if(!Objects.equals(name, p.name) && productsName.contains(p.name)) {
            return;
        }
        int index = productsName.indexOf(name);
        products.remove(index);
        productsName.remove(index);
        database.remove(name);
        productsName.add(p.name);
        products.add(p);
        database.put(p.name, p);
        adapter.notifyDataSetChanged();
    }

    public void deleteElement(String str) {
        if(productsName.contains(str)) {
            products.remove(productsName.indexOf(str));
            productsName.remove(str);
            database.remove(str);
            adapter.notifyDataSetChanged();
        }
    }
}
