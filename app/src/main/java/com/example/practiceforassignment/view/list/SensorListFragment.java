//list fragment : 리스트뷰에 데이터 뿌려주는 프래그먼트

package com.example.practiceforassignment.view.list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practiceforassignment.R;
import com.example.practiceforassignment.view.graph.SensorGraphItem;
import com.example.practiceforassignment.viewmodel.DataViewModel;

import java.util.ArrayList;


public class SensorListFragment extends Fragment implements DataViewModel.ViewInterface {

    RecyclerView mRecyclerView;
    SensorListAdapter mSensorListAdapter;
    SendCheckListInterface sendCheckListInterface;
    int listTime = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_layout, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mSensorListAdapter = new SensorListAdapter(getContext(), this::getCheckPosition);
        mRecyclerView.setAdapter(mSensorListAdapter);

        this.sendCheckListInterface = (SendCheckListInterface) getActivity();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    // Main & recorded
    public void updateData(ArrayList<SensorListItem> list) {
        if (mRecyclerView != null) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSensorListAdapter.addItem(list);
                        listTime++;
                        sendCheckListInterface.sendListTime(listTime);
                    }
                });
            }
        }
    }

    // Listener to send MainActivity class
    public interface SendCheckListInterface {
        void sendArray(ArrayList<Integer> arrayList);
        void sendListTime(int time);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onUpdateView(ArrayList<SensorListItem> list) {

    }

    @Override
    public void onUpdateGraphView(ArrayList<SensorGraphItem> list) {

    }

    @Override
    public void getCheckPosition(ArrayList<Integer> list) {
        sendCheckListInterface.sendArray(list);
    }

    @Override
    public void onSelection(ArrayList<Integer> list) {

    }
}
