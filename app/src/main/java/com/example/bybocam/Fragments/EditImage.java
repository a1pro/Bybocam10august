package com.example.bybocam.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.example.bybocam.Interface.EditImageFragmentListener;
import com.example.bybocam.R;


public class EditImage extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private EditImageFragmentListener listener;
    SeekBar seekBar_brightNess, seekBar_constrant, seekBar_saturation;

    public void setListener(EditImageFragmentListener listener) {
        this.listener = listener;
    }

    public EditImage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_image, container, false);


        seekBar_brightNess=view.findViewById(R.id.seekar_brightness);

        seekBar_constrant=view.findViewById(R.id.seekar_constrant);

        seekBar_saturation=view.findViewById(R.id.seekar_saturation);

        // keeping brightness value b/w -100 / +100
        seekBar_brightNess.setMax(200);
        seekBar_brightNess.setProgress(100);

        // keeping contrast value b/w 1.0 - 3.0
        seekBar_constrant.setMax(20);
        seekBar_constrant.setProgress(0);

        // keeping saturation value b/w 0.0 - 3.0
        seekBar_saturation.setMax(30);
        seekBar_saturation.setProgress(10);

        seekBar_brightNess.setOnSeekBarChangeListener(this);
        seekBar_constrant.setOnSeekBarChangeListener(this);
        seekBar_saturation.setOnSeekBarChangeListener(this);

        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        if (listener != null) {

            if (seekBar.getId() == R.id.seekar_brightness) {
                // brightness values are b/w -100 to +100
                listener.onBrghtnessChanges(progress - 100);
            }

            if (seekBar.getId() == R.id.seekar_constrant) {
                // converting int value to float
                // contrast values are b/w 1.0f - 3.0f
                // progress = progress > 10 ? progress : 10;
                progress += 10;
                float floatVal = .10f * progress;
                listener.onConstrantsChanges(floatVal);
            }

            if (seekBar.getId() == R.id.seekar_saturation) {
                // converting int value to float
                // saturation values are b/w 0.0f - 3.0f
                float floatVal = .10f * progress;
                listener.onSaturationChanges(floatVal);
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (listener != null)
            listener.onEditStarted();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (listener != null)
            listener.onEditCompleted();
    }

    public void resetControls() {
        seekBar_brightNess.setProgress(100);
        seekBar_constrant.setProgress(0);
        seekBar_saturation.setProgress(10);
    }
}
