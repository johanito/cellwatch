package com.example.niden.cellwatchsharing.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.niden.cellwatchsharing.R;
import com.example.niden.cellwatchsharing.adapters.SpinnerTaskTypeAdapter;
import com.example.niden.cellwatchsharing.adapters.SpinnerTechnicianAdapter;
import com.example.niden.cellwatchsharing.database.FirebaseUserEntity;
import com.example.niden.cellwatchsharing.controllers.Task;
import com.example.niden.cellwatchsharing.utils.DatePickerUtils;
import com.example.niden.cellwatchsharing.utils.KeyboardUtils;
import com.example.niden.cellwatchsharing.utils.ToastUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import static com.example.niden.cellwatchsharing.database.DataQuery.QUERY_ONLY_TECHNICIAN;
import static com.example.niden.cellwatchsharing.database.DataQuery.QUERY_TASK_TYPE;
import static com.example.niden.cellwatchsharing.database.DataQuery.QUERY_TECHNICIAN;


/**
 * Created by niden on 16-Nov-17.
 * Admin side for inserting task
 */

public class CreateTaskFragment extends Fragment {

    private Activity referenceActivity;
    private EditText txTaskName, txDescription, txAddress, txSuburb, txClass;
    public static FirebaseDatabase database;
    private Task mTask = new Task();
    private Button mBtnStartDate, mBtnEndDate;
    private Spinner spinner, spinnerTech;
    private DatePickerDialog datePickerDialog;
    private View parentHolder;
    int duration = Snackbar.LENGTH_LONG;
    private LinearLayout parentLayout;
    SpinnerTaskTypeAdapter mSpinnerTaskTypeAdapter;
    SpinnerTechnicianAdapter mSpinnerTechAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.fragment_create_task_layout, container, false);
        setHasOptionsMenu(true);
        getActivity().setTitle("New Task");
        bindingViews();

        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideSoftKeyboard(v, referenceActivity);
            }
        });
        //SETUP SPINNER FOR SELECTING TYPE OF TASK
        mSpinnerTaskTypeAdapter = new SpinnerTaskTypeAdapter(referenceActivity, String.class, android.R.layout.simple_list_item_1, QUERY_TASK_TYPE);
        spinner.setAdapter(mSpinnerTaskTypeAdapter);


        //SETUP SPINNER FOR SELECTING TECHNICIAN
        mSpinnerTechAdapter = new SpinnerTechnicianAdapter(referenceActivity, FirebaseUserEntity.class, R.layout.item_spinner_technician, QUERY_ONLY_TECHNICIAN);
        spinnerTech.setAdapter(mSpinnerTechAdapter);

        mBtnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerUtils.openDatePicker(referenceActivity, datePickerDialog, mBtnStartDate, mBtnEndDate);
            }
        });
        mBtnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerUtils.openEndDatePicker(referenceActivity, datePickerDialog, mBtnStartDate, mBtnEndDate);
            }
        });
        return parentHolder;
    }

    private void bindingViews() {
        spinnerTech = (Spinner) parentHolder.findViewById(R.id.spinnerTechnician);
        mBtnStartDate = (Button) parentHolder.findViewById(R.id.btnStartDate);
        mBtnEndDate = (Button) parentHolder.findViewById(R.id.btnEndDate);
        txTaskName = (EditText) parentHolder.findViewById(R.id.editTextTaskName);
        txAddress = (EditText) parentHolder.findViewById(R.id.editTextAddress);
        txDescription = (EditText) parentHolder.findViewById(R.id.editTextDescription);
        txSuburb = (EditText) parentHolder.findViewById(R.id.editTextSuburb);
        txClass = (EditText) parentHolder.findViewById(R.id.editTextClass);
        spinner = (Spinner) parentHolder.findViewById(R.id.spinnerType);
        parentLayout = (LinearLayout) parentHolder.findViewById(R.id.layout_parent);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.create_task_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mTask.insertTask(txTaskName, txClass, txDescription, txAddress, txSuburb, spinner, spinnerTech);
        ToastUtils.showSnackbar(getView(), getString(R.string.txt_submit_task), duration);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new TaskFragment()).commit();
        return super.onOptionsItemSelected(item);
    }
}
