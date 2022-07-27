//graph fragment : 그래프 띄워주는 프래그먼트

package com.example.practiceforassignment.view.graph;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.practiceforassignment.R;
import com.example.practiceforassignment.view.list.SensorListItem;
import com.example.practiceforassignment.viewmodel.DataViewModel;

import java.util.ArrayList;
import java.util.Arrays;

public class SensorGraphFragment extends Fragment implements SensorGraphAdapter.OnItemDelete, DataViewModel.ViewInterface {


    SensorGraphAdapter mSensorGraphAdapter;
    ListView mGraphListView;
    boolean isStop = true;
    ArrayList<SensorGraphItem> graphItems;
    ArrayList<Integer> mCheckedList = new ArrayList<Integer>();
    int count = 0;

    public static Context context;

    public SensorGraphFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGraphListView = view.findViewById(R.id.graphlist);
        mSensorGraphAdapter = new SensorGraphAdapter(getContext(), this);
        setGraphListItem();
        setIsstop();
        mGraphListView.setAdapter(mSensorGraphAdapter);
        context = this.getContext();
        graphItems = new ArrayList<>();

        setListViewHeightBasedOnChildren(mGraphListView);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.graph_layout, container, false);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    // realtime update of graph
    public void updateData(ArrayList<SensorGraphItem> list) {
        if (isStop) {
            if (mGraphListView != null) {
                for (int i = 0; i < mCheckedList.size(); i++) {
                    int finalI = i;
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int value = Integer.parseInt(list.get(finalI).value);
                            mSensorGraphAdapter.addGraphPoint(finalI, value);
                            maxValueArray[finalI] = Math.max(maxValueArray[finalI], value);
                            minValueArray[finalI] = Math.min(minValueArray[finalI], value);
                            graphItems.add(finalI, new SensorGraphItem(list.get(finalI).value, "MAX : " + maxValueArray[finalI] + "V", "MIN : " + minValueArray[finalI] + "V", list.get(finalI).currentValue, "DATA" + mCheckedList.get(finalI)));
                            mSensorGraphAdapter.putValue(finalI, graphItems.get(finalI).maxValue, graphItems.get(finalI).minValue, graphItems.get(finalI).currentValue, graphItems.get(finalI).nameOfValue);
                        }
                    });
                }
            }
        }
    }

    // realtime update of graph
    public void updateRecordedData(ArrayList<SensorGraphItem> list) {
        if (isStop) {
            if (mGraphListView != null) {
                for (int i = 0; i < mCheckedList.size(); i++) {
                    int finalI = i;
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // initialize addGraphPoint
                            int value = Integer.parseInt(list.get(finalI).value);
                            if (count == 0) {
                                mSensorGraphAdapter.setGraphPoint(finalI, value);
                            }
                            mSensorGraphAdapter.addGraphPoint(finalI, value);
                            maxValueArray[finalI] = Math.max(maxValueArray[finalI], value);
                            minValueArray[finalI] = Math.min(minValueArray[finalI], value);
                            graphItems.add(finalI, new SensorGraphItem(list.get(finalI).value, "MAX : " + maxValueArray[finalI] + "V", "MIN : " + minValueArray[finalI] + "V", list.get(finalI).currentValue, "DATA" + mCheckedList.get(finalI)));
                            mSensorGraphAdapter.putValue(finalI, graphItems.get(finalI).maxValue, graphItems.get(finalI).minValue, graphItems.get(finalI).currentValue, graphItems.get(finalI).nameOfValue);
                        }
                    });
                }
            }
        }
        count++;
    }


    // add items as you as checked
    public void setGraphListItem() {
        if (getActivity() != null) {
            for (int i = 0; i < mCheckedList.size(); i++) {
                int finalI = i;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSensorGraphAdapter.addItem(new SensorGraphItem("", "", "", "", "DATA " + String.valueOf(mCheckedList.get(finalI))));
                    }
                });
            }
        }
    }

    private int[] maxValueArray;
    private int[] minValueArray;

    // get the check list from ListFragment
    public void setCheckedList(ArrayList<Integer> mCheckedStateList) {
        mCheckedList = mCheckedStateList;

        maxValueArray = new int[mCheckedStateList.size()];
        minValueArray = new int[mCheckedStateList.size()];
        Arrays.fill(maxValueArray, Integer.MIN_VALUE);
        Arrays.fill(minValueArray, Integer.MAX_VALUE);
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
    }

    public void setIsstop() {
        mSensorGraphAdapter.setBooleanArray(mCheckedList.size());
    }

    @Override
    public void onDelete(int delIndex) {
        mCheckedList.remove(delIndex);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {

        int totalHeight = 0;

        for (int i = 0; i < mSensorGraphAdapter.getCount(); i++) {
            View listItem = mSensorGraphAdapter.getView(i, null, mGraphListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = mGraphListView.getLayoutParams();
        params.height = totalHeight + (mGraphListView.getDividerHeight() * (mSensorGraphAdapter.getCount()) - 1);
        mGraphListView.setLayoutParams(params);
    }
}

