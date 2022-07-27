package com.example.practiceforassignment.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.practiceforassignment.R;
import com.example.practiceforassignment.databinding.ActivityMainBinding;
import com.example.practiceforassignment.view.graph.GraphView;
import com.example.practiceforassignment.view.graph.SensorGraphFragment;
import com.example.practiceforassignment.view.graph.SensorGraphItem;
import com.example.practiceforassignment.view.list.SensorListFragment;
import com.example.practiceforassignment.view.list.SensorListItem;
import com.example.practiceforassignment.view.selection.SelectionFragment;
import com.example.practiceforassignment.viewmodel.DataViewModel;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements SensorListFragment.SendCheckListInterface, DataViewModel.ViewInterface, SelectionFragment.SendCheckListToMain {

    private ActivityMainBinding binding;

    private SensorListFragment mSensorListFragment;
    private SensorGraphFragment mSensorGraphFragment;
    private SelectionFragment mSelectionFragment;

    private FragmentManager mFragmentManager;
    private FragmentTransaction transaction;

    boolean stopOnClick = true;
    boolean graphOnClick = true;

    DataViewModel mViewModel;
    ArrayList<SensorListItem> getRecordedList;
    ArrayList<SensorGraphItem> getRecordedGraphList;

    ArrayList<Integer> mCheckedStateList; // From ListAdapter
    ArrayList<Integer> mSelectedList; // From SelectionAdapter
    int listTime = 1;

    public static Context context;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        context = this;
        mViewModel = new DataViewModel(getApplicationContext(), this);

        mSensorListFragment = new SensorListFragment();
        mFragmentManager = getSupportFragmentManager();
        transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.ll_content, mSensorListFragment).commitAllowingStateLoss();

        mCheckedStateList = new ArrayList<>();
        mSelectedList = new ArrayList<>();

        // file storage setting
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        // seekbar button event
        binding.progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (graphOnClick) {
                    getRecordedList = new ArrayList<>();
                    getRecordedList = mViewModel.getRecordedData(progress);
                    setProgressBarTimeText(progress, mViewModel.getRecordedDataLength());
                    mSensorListFragment.updateData(getRecordedList);
                }

                if (!graphOnClick || mSelectedList.size() != 0) {
                    // listTime = 리스트뷰에서 재생된 시간
                    getRecordedGraphList = new ArrayList<>();

                    if (count < mViewModel.getRecordedDataLength() - listTime + 1) {
                        getRecordedGraphList = mViewModel.getRecordedGraphData(listTime + progress);
                        setProgressBarTimeText(progress, mViewModel.getRecordedDataLength() - listTime);
                        mSensorGraphFragment.updateRecordedData(getRecordedGraphList);
                    } else {
                        getRecordedGraphList = mViewModel.getRecordedGraphData(progress);
                        setProgressBarTimeText(progress, mViewModel.getRecordedDataLength());
                        mSensorGraphFragment.updateRecordedData(getRecordedGraphList);
                    }
                    if (getRecordedGraphList != null) {
                        getRecordedGraphList.clear();
                    }
                }
                count++;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }


        });


        // stop button event
        binding.stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stopOnClick) { // Stop event
                    binding.datacapture.setVisibility(View.GONE);
                    binding.rewindDouble.setVisibility(View.VISIBLE);
                    binding.rewind.setVisibility(View.VISIBLE);
                    binding.pause.setVisibility(View.VISIBLE);
                    binding.play.setVisibility(View.VISIBLE);
                    binding.forward.setVisibility(View.VISIBLE);
                    binding.stopbtn.setText("START");
                    binding.progress.setVisibility(View.VISIBLE);
                    binding.progressbarBackground.setVisibility(View.VISIBLE);
                    binding.progressTime.setVisibility(View.VISIBLE);

                    mViewModel.stopGeneration(true);

                    // graphOnClick == true : ListFragment
                    if (graphOnClick) {
                        binding.progress.setMax(mViewModel.getRecordedDataLength());
                    }
                    // graphOnClick != true : GraphFragment
                    if (!graphOnClick || mSelectedList.size() != 0) {
                        if (count < mViewModel.getRecordedDataLength() - listTime + 1) {
                            binding.progress.setMax(mViewModel.getRecordedDataLength() - listTime);
                        } else {
                            binding.progress.setMax(mViewModel.getRecordedDataLength());
                        }
                    }
                    stopOnClick = false;
                } else { // Start event
                    binding.progress.setProgress(0);
                    mViewModel.deleteOldData();

                    binding.datacapture.setVisibility(View.VISIBLE);
                    binding.rewindDouble.setVisibility(View.GONE);
                    binding.rewind.setVisibility(View.GONE);
                    binding.pause.setVisibility(View.GONE);
                    binding.play.setVisibility(View.GONE);
                    binding.forward.setVisibility(View.GONE);
                    binding.stopbtn.setText("STOP");
                    binding.progress.setVisibility(View.GONE);
                    binding.progressbarBackground.setVisibility(View.GONE);
                    binding.progressTime.setVisibility(View.GONE);

                    mViewModel.startGeneration();
                    GraphView graphView = new GraphView(getApplicationContext());
                    graphView.setStartOnDraw();
                    stopOnClick = true;
                }
            }
        });

        // graph button event
        binding.graphbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (graphOnClick) {
                    // if you didn't check anything at the ListFragment
                    if (mCheckedStateList.size() == 0) {
                        mSelectionFragment = new SelectionFragment();
                        transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.ll_content, mSelectionFragment).commit();
                        // if you check something at the SelectFragment
                        if (mSelectedList.size() != 0) {
                            binding.graphbtn.setText("TEXT");
                            mSensorGraphFragment = new SensorGraphFragment();
                            mSensorGraphFragment.setCheckedList(mSelectedList);
                            transaction = mFragmentManager.beginTransaction();
                            transaction.replace(R.id.ll_content, mSensorGraphFragment).commit();
                            graphOnClick = false;
                        }
                    } else {
                        // If you check something at the ListFragment
                        binding.graphbtn.setText("TEXT");
                        mSensorGraphFragment = new SensorGraphFragment();
                        mSensorGraphFragment.setCheckedList(mCheckedStateList);
                        transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.ll_content, mSensorGraphFragment).commit();
                        graphOnClick = false;
                    }
                } else {
                    // back to be ListFragment
                    GraphView graphView = new GraphView(getApplicationContext());
                    graphView.setStopOnDraw();
                    binding.graphbtn.setText("GRAPH");
                    transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.ll_content, mSensorListFragment).commit();
                    graphOnClick = true;
                }
            }
        });

        binding.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (graphOnClick) {
                    mViewModel.threadTime(1000);
                    mViewModel.startPlay();
                }
                if (!graphOnClick) {
                    mViewModel.threadTime(1000);
                    mViewModel.startGraphPlay();
                }
            }
        });

        binding.forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (graphOnClick) {
                    mViewModel.threadTime(500);
                    mViewModel.startPlay();
                }
                if (!graphOnClick) {
                    mViewModel.threadTime(500);
                    mViewModel.startGraphPlay();
                }
            }
        });

        binding.rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (graphOnClick) {
                    mViewModel.threadTime(1000);
                    mViewModel.startRewind();
                }
                if (!graphOnClick) {
                    mViewModel.threadTime(1000);
                    mViewModel.startGraphRewind();
                }
            }
        });

        binding.rewindDouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (graphOnClick) {
                    mViewModel.threadTime(500);
                    mViewModel.startRewind();
                }
                if (!graphOnClick) {
                    mViewModel.threadTime(500);
                    mViewModel.startGraphRewind();
                }
            }
        });

        binding.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.stopButtonThread();
                binding.progress.setProgress(0);
            }
        });

        // data capture event
        binding.datacapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!graphOnClick) {
                    mViewModel.writeFile();
                    Toast.makeText(getApplicationContext(), "Captured Text data has been saved at the folder name 'DOWNLOAD'.", Toast.LENGTH_SHORT).show();
                }
                if (graphOnClick) {
                    mViewModel.writeGraphFile();
                    Toast.makeText(getApplicationContext(), "Captured Graph data has been saved at the folder name 'DOWNLOAD'.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setProgressBar(int progress) {
        binding.progress.setProgress(progress);
    }

    @SuppressLint("DefaultLocale")
    public void setProgressBarTimeText(int current, int total) {
        int currentHour, currentMin, currentSec;
        int totalHour, totalMin, totalSec;

        currentMin = current / 60;
        currentSec = current % 60;
        currentHour = currentMin / 60;

        totalMin = total / 60;
        totalSec = total % 60;
        totalHour = totalMin / 60;

        binding.progressTime.setText(String.format("%02d:%02d:%02d / %02d:%02d:%02d", currentHour, currentMin, currentSec, totalHour, totalMin, totalSec));
    }

    @Override
    public void onUpdateView(ArrayList<SensorListItem> list) {
        if (mSensorListFragment != null) {
            mSensorListFragment.updateData(list);
        }
    }

    @Override
    public void onUpdateGraphView(ArrayList<SensorGraphItem> list) {
        if (mSensorGraphFragment != null) {
            mSensorGraphFragment.updateData(list);
        }
    }

    @Override
    public void getCheckPosition(ArrayList<Integer> list) {
    }

    @Override
    public void onSelection(ArrayList<Integer> list) {
    }

    @Override
    public void sendArray(ArrayList<Integer> arrayList) {
        mCheckedStateList = arrayList;
    }

    @Override
    public void sendListTime(int time) {
        listTime = time;
    }

    @Override
    public void sendCheckList(ArrayList<Integer> list) {
        mSelectedList = list;
    }
}

