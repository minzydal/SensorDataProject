// 로컬 DB인 Room과 View Model 간의 브릿지 역할
package com.example.practiceforassignment.model.database;

import android.content.Context;

import androidx.room.Room;

import com.example.practiceforassignment.view.MainActivity;
import com.example.practiceforassignment.view.graph.SensorGraphItem;
import com.example.practiceforassignment.view.list.SensorListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Repository {
    private final int TOTAL_DATA_SIZE = 50;

    private ArrayList<SensorListItem> mSensorDataList = new ArrayList<>(TOTAL_DATA_SIZE);
    private ArrayList<SensorGraphItem> mSensorGraphDataList = new ArrayList<>(TOTAL_DATA_SIZE);
    private SensorDB mSensorDatabase;
    GenerateDataThread mGenerateThread;
    PlayThread playThread;
    RewindThread rewindThread;
    PlayGraphThread playGraphThread;
    RewindGraphThread rewindGraphThread;
    DataModelInterface mInterface;
    SensorDataList sensorDataList = new SensorDataList();

    ArrayList<Integer> graphValueList = new ArrayList<>();

    int max = Integer.MIN_VALUE;
    int min = Integer.MAX_VALUE;

    int threadTime = 1000;

    public Repository(Context context, DataModelInterface interfaceModel) {
        mInterface = interfaceModel;

        //generate DB
        mSensorDatabase = Room.databaseBuilder(context, SensorDB.class, "sensorDB").allowMainThreadQueries()
                .build();
        if (mSensorDatabase.dao().getAll().size() != 0) {
            mSensorDatabase.dao().deleteAll();
        }
        mGenerateThread = new GenerateDataThread();
        mGenerateThread.start();

        playThread = new PlayThread();
        rewindThread = new RewindThread();

        playGraphThread = new PlayGraphThread();
        rewindGraphThread = new RewindGraphThread();
    }

    public void stopGenerationThread(boolean isStop) {
        mGenerateThread.setStopGeneration(!isStop);
    }

    public void startGeneration() {
        mGenerateThread = new GenerateDataThread();
        mGenerateThread.start();
    }

    public void deleteAllData() {
        if (mSensorDatabase.dao().getAll().size() != 0) {
            mSensorDatabase.dao().deleteAll();
        }
    }


    //generate random data of sensor
    public class GenerateDataThread extends Thread {
        private boolean mIsRunning;

        public GenerateDataThread() {
            mIsRunning = true;
        }

        @Override
        public void run() {
            while (mIsRunning) {
                graphValueList.clear();
                mSensorGraphDataList.clear();
                mSensorDataList.clear();

                max = 0;
                min = 10;

                //generate sensor data and store
                for (int i = 0; i <= TOTAL_DATA_SIZE; i++) {
                    String value = String.valueOf(new Random().nextInt(10) + 1);
                    mSensorDataList.add(new SensorListItem(i, String.format("DATA " + i), value, "V"));
                    graphValueList.add(Integer.valueOf(value));

                    int graphValue = graphValueList.get(graphValueList.size() - 1);

                    max = Math.max(max, graphValue);
                    min = Math.min(min, graphValue);

                    // max value, min value of graph data
                    if (max < graphValueList.get(i)) {
                        max = graphValueList.get(i);
                    }
                    if (min >= graphValueList.get(i)) {
                        min = graphValueList.get(i);
                    }
                    mSensorGraphDataList.add(i, new SensorGraphItem(String.valueOf(graphValueList.get(i)), String.valueOf(max), String.valueOf(min), String.valueOf(graphValueList.get(i)), "DATA " + i));
                }

                sensorDataList.setDataList(mSensorDataList);
                sensorDataList.setGraphList(mSensorGraphDataList);
                mSensorDatabase.dao().insert(sensorDataList);

                mInterface.onUpdateList(mSensorDataList);
                mInterface.onUpdateGraph(mSensorGraphDataList);
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        public void setStopGeneration(boolean isRunning) {
            mIsRunning = isRunning;
        }
    }


    // button thread
    public void startPlayThread() {
        if (playThread.isAlive() || rewindThread.isAlive()) {
            playThread.interrupt();
            rewindThread.interrupt();
        }
        playThread = new PlayThread();
        playThread.start();
    }

    public void startRewindThread() {
        if (playThread.isAlive() || rewindThread.isAlive()) {
            playThread.interrupt();
            rewindThread.interrupt();
        }
        rewindThread = new RewindThread();
        rewindThread.start();
    }

    public void startPlayGraphThread() {
        if (playGraphThread.isAlive() || rewindGraphThread.isAlive()) {
            playGraphThread.interrupt();
            rewindGraphThread.interrupt();
        }
        playGraphThread = new PlayGraphThread();
        playGraphThread.start();
    }

    public void startRewindGraphThread() {
        if (playGraphThread.isAlive() || rewindGraphThread.isAlive()) {
            playGraphThread.interrupt();
            rewindGraphThread.interrupt();
        }
        rewindGraphThread = new RewindGraphThread();
        rewindGraphThread.start();
    }


    public void stopButtonThread() {
        if (playThread.isAlive() || rewindThread.isAlive() || playGraphThread.isAlive() || rewindGraphThread.isAlive()) {
            playThread.interrupt();
            rewindThread.interrupt();
            playGraphThread.interrupt();
            rewindGraphThread.interrupt();
        }
    }

    // button control
    class PlayThread extends Thread {
        @Override
        public void run() {
            try {
                for (int i = 0; i < getRecordedData().size(); i++) {
                    ((MainActivity) MainActivity.context).setProgressBar(i);
                    Thread.sleep(threadTime);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class RewindThread extends Thread {
        @Override
        public void run() {
            try {
                for (int i = getRecordedData().size() - 1; i >= 0; i--) {
                    ((MainActivity) MainActivity.context).setProgressBar(i);
                    Thread.sleep(threadTime);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class PlayGraphThread extends Thread {
        @Override
        public void run() {
            try {
                for (int i = 0; i < getRecordedData().size(); i++) {
                    ((MainActivity) MainActivity.context).setProgressBar(i);
                    Thread.sleep(threadTime);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class RewindGraphThread extends Thread {
        @Override
        public void run() {
            try {
                for (int i = getRecordedData().size(); i >= 0; i--) {
                    ((MainActivity) MainActivity.context).setProgressBar(i);
                    Thread.sleep(threadTime);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void threadTime(int thread_time) {
        threadTime = thread_time;
    }

    public List<SensorDataList> getRecordedData() {
        return mSensorDatabase.dao().getAll();
    }

    public interface DataModelInterface {
        void onUpdateList(ArrayList<SensorListItem> list);
        void onUpdateGraph(ArrayList<SensorGraphItem> list);
    }

}
