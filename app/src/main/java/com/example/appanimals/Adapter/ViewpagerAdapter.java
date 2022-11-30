package com.example.appanimals.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appanimals.Fragment.Home;
import com.example.appanimals.Fragment.NoteFragment;
import com.example.appanimals.Fragment.ProfileFragment;
import com.example.appanimals.Fragment.SearchFragment;

public class ViewpagerAdapter extends FragmentStateAdapter {
    public ViewpagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Home();
            case 1:
                return new NoteFragment();
            case 2:
                return new SearchFragment();
            case 3:
                return new ProfileFragment();
            default:
                return new Home();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
