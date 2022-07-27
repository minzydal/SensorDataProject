package com.example.practiceforassignment.view.selection;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practiceforassignment.R;
import com.example.practiceforassignment.view.graph.SensorGraphItem;
import com.example.practiceforassignment.view.list.SensorListItem;
import com.example.practiceforassignment.viewmodel.DataViewModel;

import java.util.ArrayList;

public class SelectionFragment extends Fragment implements DataViewModel.ViewInterface {
    RecyclerView mSelectionRecyclerView;
    SelectionAdapter mSelectionAdapter;

    SendCheckListToMain sendCheckListToMain;

    Button selectAllBtn, clearBtn;

    private int TOTAL_SIZE = 50;
    ArrayList<SelectionItem> list = new ArrayList<>();
    public static Context context;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Recycler setting
        mSelectionRecyclerView = (RecyclerView) view.findViewById(R.id.select_recyclerview);
        mSelectionRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mSelectionRecyclerView.setLayoutManager(mLayoutManager);
        mSelectionRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSelectionAdapter = new SelectionAdapter(getContext(), this::onSelection);
        mSelectionRecyclerView.setAdapter(mSelectionAdapter);


        // add the name of data list
        for (int i = 0; i <= TOTAL_SIZE; i++) {
            list.add(new SelectionItem("DATA " + i));
        }
        mSelectionAdapter.addItem(list);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.selection_layout, container, false);
        super.onViewCreated(view, savedInstanceState);
        this.sendCheckListToMain = (SendCheckListToMain) getActivity();

        selectAllBtn = view.findViewById(R.id.selectAllBtn);
        selectAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectionAdapter.selectAll(true);
                Toast.makeText(getActivity(), "You have selected all the list.", Toast.LENGTH_SHORT).show();
            }
        });

        clearBtn = view.findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectionAdapter.selectAll(false);
                Toast.makeText(getActivity(), "You have cleared all the list.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onUpdateView(ArrayList<SensorListItem> list) {
    }

    @Override
    public void onUpdateGraphView(ArrayList<SensorGraphItem> list) {

    }

    @Override
    public void getCheckPosition(ArrayList<Integer> list) {

    }

    @Override
    public void onSelection(ArrayList<Integer> list) {
        sendCheckListToMain.sendCheckList(list);
    }


    public interface SendCheckListToMain {
        void sendCheckList(ArrayList<Integer> list);
    }
}
