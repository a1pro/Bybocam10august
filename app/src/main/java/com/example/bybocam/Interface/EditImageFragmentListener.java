package com.example.bybocam.Interface;

public interface EditImageFragmentListener {
    void onBrghtnessChanges(int brightness);
    void onSaturationChanges(float saturation);
    void onConstrantsChanges(float Constrant);
    void onEditStarted();
    void onEditCompleted();
}
