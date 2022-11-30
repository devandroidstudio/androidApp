package com.example.appanimals.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appanimals.Fragment.SingInFragment;
import com.example.appanimals.Fragment.SingUpFragment;

public class ViewpagerIntro2Adapter extends FragmentStateAdapter {
    public ViewpagerIntro2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new SingInFragment();
            case 1:
                return new SingUpFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
