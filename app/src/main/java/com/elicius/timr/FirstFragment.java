package com.elicius.timr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<View> arrayList = new ArrayList<>();
        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.timer_view, null);
        arrayList.add(view1);

        RecyclerView recyclerView = ((MainActivity) getContext()).findViewById(R.id.timer_recyclerView);
        recyclerView.addFocusables(arrayList, View.FOCUS_DOWN);
    }
}