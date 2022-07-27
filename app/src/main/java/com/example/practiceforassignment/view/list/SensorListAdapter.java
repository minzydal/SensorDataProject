package com.example.practiceforassignment.view.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practiceforassignment.R;


import java.util.ArrayList;

public class SensorListAdapter extends RecyclerView.Adapter<SensorListAdapter.Holder> {

    private ArrayList<SensorListItem> items;
    boolean[] isChecked;
    private CheckPositionInterface checkPositionInterface;
    ArrayList<Integer> checkedItems = new ArrayList();

    public SensorListAdapter(Context context, CheckPositionInterface mCheckPositionInterface) {
        this.checkPositionInterface = mCheckPositionInterface;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recyclerviewitem, parent, false);
        return new Holder(view, checkPositionInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.setItem(items.get(position));
        holder.checkBox.setChecked(isChecked[position]);

        int pos = position;
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if(holder.checkBox.isChecked()) {
                    isChecked[pos] = true;
                    checkedItems.add(Integer.valueOf(holder.getAdapterPosition()));
                }else {
                    isChecked[pos] = false;
                    checkedItems.remove(Integer.valueOf(holder.getAdapterPosition()));
                }
            }
        });
        checkPositionInterface.onCheckPosition(checkedItems);
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void addItem(ArrayList<SensorListItem> list) {
        this.items = list;
        if (isChecked == null) {
            isChecked = new boolean[list.size()];
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView sensorName;
        TextView sensorData;
        TextView sensorUnit;
        CheckBox checkBox;
        LinearLayout itemArea;

        public Holder(@NonNull View itemView, CheckPositionInterface checkPositionInterface) {
            super(itemView);
            sensorName = itemView.findViewById(R.id.name_in_list);
            sensorData = itemView.findViewById(R.id.value_in_list);
            sensorUnit = itemView.findViewById(R.id.unit_in_list);
            checkBox = itemView.findViewById(R.id.checkbox);
            itemArea = itemView.findViewById(R.id.itemarea);
        }

        public void setItem(SensorListItem sensorListItem) {
            sensorName.setText(sensorListItem.getSensorName());
            sensorData.setText(sensorListItem.getSensorData());
            sensorUnit.setText(sensorListItem.getSensorUnit());
        }
    }

    public interface CheckPositionInterface {
         void onCheckPosition(ArrayList<Integer> checkList);
    }

}
