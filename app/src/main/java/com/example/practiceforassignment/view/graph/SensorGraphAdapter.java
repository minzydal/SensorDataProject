package com.example.practiceforassignment.view.graph;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.practiceforassignment.R;

import java.util.ArrayList;
import java.util.HashMap;

public class SensorGraphAdapter extends BaseAdapter {

    private Context context;
    TextView maxValuetv, minValuetv, currentValuetv, nameInGraphtv;
    Button exitBtn;
    ArrayList<Boolean> isStop;
    private OnItemDelete onItemDelete;

    HashMap<Integer, String> maxValueMap;
    HashMap<Integer, String> minValueMap;
    HashMap<Integer, String> currentValueMap;
    HashMap<Integer, String> dataNameMap;

    int graphPoint;

    ArrayList<SensorGraphItem> items = new ArrayList<SensorGraphItem>();
    GraphView view = null;

    public SensorGraphAdapter(Context context, OnItemDelete onItemDelete) {
        this.context = context;
        this.onItemDelete = onItemDelete;

        maxValueMap = new HashMap<>();
        minValueMap = new HashMap<>();
        currentValueMap = new HashMap<>();
        dataNameMap = new HashMap<>();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(SensorGraphItem item) {
        items.add(item);
    }

    // get the data of graph point
    public void addGraphPoint(int position, int value) {
        graphPoint = value;
        if (isStop.get(position)) {
            items.get(position).addGraphPointList(value);
            notifyDataSetChanged();
        }
    }

    public void setGraphPoint(int position, int value) {
        graphPoint = value;
        if (isStop.get(position)) {
            items.get(position).setGraphPointList();
            notifyDataSetChanged();
        }
    }

    public void putValue(int position, String maxValue, String minValue, String currentValue, String nameOfData) {
        maxValueMap.put(position, maxValue);
        minValueMap.put(position, minValue);
        currentValueMap.put(position, currentValue);
        dataNameMap.put(position, nameOfData);
        notifyDataSetChanged();
    }


    // stop getting the data of graph point
    public void setStopAddGraphPoint(int position) {
        isStop.remove(position);
    }

    public void setStartAddGraphPoint(int position, int value) {
        addGraphPoint(position, value);
    }

    public void setBooleanArray(int count) {
        isStop = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            isStop.add(true);
        }
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.graphviewitem, parent, false);
        }

        final SensorGraphItem sensorGraphItem = items.get(position);

        view = convertView.findViewById(R.id.graph);
        view.setGetValueList(sensorGraphItem.graphPointList);
        maxValuetv = convertView.findViewById(R.id.maxValuetv);
        minValuetv = convertView.findViewById(R.id.minValuetv);
        currentValuetv = convertView.findViewById(R.id.currentValue);
        nameInGraphtv = convertView.findViewById(R.id.nameInGraph);
        exitBtn = convertView.findViewById(R.id.exitGraph);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.remove(getItem(position));
                setStopAddGraphPoint(position);
                onItemDelete.onDelete(position);
                notifyDataSetChanged();
            }
        });

        maxValuetv.setText(maxValueMap.get(position));
        minValuetv.setText(minValueMap.get(position));
        currentValuetv.setText(currentValueMap.get(position));
        nameInGraphtv.setText(sensorGraphItem.getNameOfValue());

        setStartAddGraphPoint(position, graphPoint);

        return convertView;
    }

    public GraphView getView() {
        return view;
    }


    public interface OnItemDelete {
        void onDelete(int delIndex);
    }
}