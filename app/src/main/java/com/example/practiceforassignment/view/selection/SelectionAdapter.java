package com.example.practiceforassignment.view.selection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practiceforassignment.R;

import java.util.ArrayList;

public class SelectionAdapter extends RecyclerView.Adapter<SelectionAdapter.Holder> {

    private ArrayList<SelectionItem> items;
    private boolean[] isCheckedConfirm;
    ArrayList<Integer> checkedItems = new ArrayList();
    SendSelectionInterface sendSelectionInterface;

    public SelectionAdapter(Context context, SendSelectionInterface mSelectionInterface) {
        this.sendSelectionInterface = mSelectionInterface;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.selection_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.setItem(items.get(position));
        holder.checkBox.setChecked(isCheckedConfirm[position]);

        int pos = position;

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {

                if (holder.checkBox.isChecked()) {
                    isCheckedConfirm[pos] = true;
                    checkedItems.add(Integer.valueOf(holder.getAdapterPosition()));
                } else {
                    isCheckedConfirm[pos] = false;
                    checkedItems.remove(Integer.valueOf(holder.getAdapterPosition()));
                }
            }
        });

        sendSelectionInterface.sendCheck(checkedItems);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void addItem(ArrayList<SelectionItem> list) {
        this.items = list;
        if (isCheckedConfirm == null) {
            isCheckedConfirm = new boolean[list.size()];
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

    // all selected
    public void selectAll(boolean isChecked) {
        for (int i = 0; i < isCheckedConfirm.length; i++) {
            isCheckedConfirm[i] = isChecked;
            notifyDataSetChanged();
        }
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView nameOfData;
        CheckBox checkBox;

        public Holder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox_select);
            nameOfData = itemView.findViewById(R.id.select_name);
        }

        public void setItem(SelectionItem selectionItem) {
            nameOfData.setText(selectionItem.getDataName());
        }
    }

    public interface SendSelectionInterface {
        void sendCheck(ArrayList<Integer> checkList);
    }
}
