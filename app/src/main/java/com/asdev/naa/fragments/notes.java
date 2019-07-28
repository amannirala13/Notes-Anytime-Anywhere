package com.asdev.naa.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asdev.naa.R;

import java.util.Objects;

import helper.noteListAdapter;

public class notes extends Fragment {

    private RecyclerView notesListHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notesListHolder = Objects.requireNonNull(getView()).findViewById(R.id.notes_list_holder);
       // notesListHolder.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()),0));
        notesListHolder.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        notesListHolder.setLayoutManager(llm);

        noteListAdapter nlAdapter = new noteListAdapter();
        notesListHolder.setAdapter(nlAdapter);
    }
}
