package com.example.practiceforassignment.viewmodel;

import android.content.Context;
import android.os.Environment;

import androidx.lifecycle.ViewModel;

import com.example.practiceforassignment.model.database.Repository;
import com.example.practiceforassignment.view.graph.SensorGraphItem;
import com.example.practiceforassignment.view.list.SensorListAdapter;
import com.example.practiceforassignment.view.list.SensorListItem;
import com.example.practiceforassignment.view.selection.SelectionAdapter;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DataViewModel extends ViewModel implements Repository.DataModelInterface, SensorListAdapter.CheckPositionInterface, SelectionAdapter.SendSelectionInterface {

    ViewInterface mViewInterface;
    Repository repository;
    String fileData;

    public DataViewModel(Context context, ViewInterface viewInterface) {
        mViewInterface = viewInterface;
        repository = new Repository(context, this);
    }

    public void stopGeneration(boolean isStop) {
        repository.stopGenerationThread(isStop);
    }

    public void startGeneration() {
        repository.startGeneration();
    }

    public ArrayList<SensorListItem> getRecordedData(int index) {
        return repository.getRecordedData().get(index).getDataList();
    }

    public int getRecordedDataLength() {
        return repository.getRecordedData().size() - 1;
    }

    public ArrayList<SensorGraphItem> getRecordedGraphData(int index){
        return repository.getRecordedData().get(index).getGraphList();
    }

    // Data capture : convert the type for saving
    public String getTextData(int index) {
        Object obj = repository.getRecordedData().get(index).getDataList();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        return json;
    }

    public String getGraphData(int index) {
        Object obj = repository.getRecordedData().get(index).getGraphList();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        return json;
    }

    // Device File Explorer
    public void writeFile() {
        String FileText = "list";
        String TimeStamp = new SimpleDateFormat("yyyy_MM_dd").format(new Date());
        String FileName = TimeStamp + FileText + ".txt";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), FileName);
        try {
            if(file.exists()){
                file.delete();
            }
            FileWriter writer = new FileWriter(file, false);
            fileData = getTextData(getRecordedDataLength());
            fileData = fileData.replace(",", "\n");
            fileData = fileData.replace("{", "");
            fileData = fileData.replace("}", "");
            fileData = fileData.replace("[", "");
            fileData = fileData.replace("]", "");
            fileData = fileData.replace("\"", " ");

            writer.write(fileData);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeGraphFile() {
        String FileText = "graph";
        String TimeStamp = new SimpleDateFormat("yyyy_MM_dd").format(new Date());
        String FileName = TimeStamp + FileText + ".txt";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), FileName);
        try {
            if(file.exists()){
                file.delete();
            }
            for (int i = 0; i < 100; i++) {
                FileText += i;
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file, false);
            fileData = getGraphData(getRecordedDataLength());
            fileData = fileData.replace(",", "\n");
            fileData = fileData.replace("{", "");
            fileData = fileData.replace("}", "");
            fileData = fileData.replace("[", "");
            fileData = fileData.replace("]", "");
            fileData = fileData.replace("\"", " ");
            fileData = fileData.replace("graphPointList :", "");

            writer.write(fileData);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateList(ArrayList<SensorListItem> list) {
        mViewInterface.onUpdateView(list);
    }

    @Override
    public void onUpdateGraph(ArrayList<SensorGraphItem> list) {
        mViewInterface.onUpdateGraphView(list);
    }

    //ListAdapter -> GraphFragment
    @Override
    public void onCheckPosition(ArrayList<Integer> checkList) {
        mViewInterface.getCheckPosition(checkList);
    }

    public void deleteOldData() {
        repository.deleteAllData();
    }

    public void startPlay() {
        repository.startPlayThread();
    }

    public void startRewind() {
        repository.startRewindThread();
    }

    public void startGraphPlay(){
        repository.startPlayGraphThread();
    }

    public void startGraphRewind(){
        repository.startRewindGraphThread();
    }

    public void stopButtonThread() {
        repository.stopButtonThread();
    }

    public void threadTime(int time) {
        repository.threadTime(time);
    }

    //SelectionAdapter -> GraphFragment
    @Override
    public void sendCheck(ArrayList<Integer> checkList) {
        mViewInterface.onSelection(checkList);
    }

    public interface ViewInterface {
        void onUpdateView(ArrayList<SensorListItem> list);
        void onUpdateGraphView(ArrayList<SensorGraphItem> list);
        void getCheckPosition(ArrayList<Integer> list);
        void onSelection(ArrayList<Integer> list);
    }
}
