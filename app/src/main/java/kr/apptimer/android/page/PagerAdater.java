package kr.apptimer.android.page;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdater extends FragmentPagerAdapter{
    public PagerAdater(@NonNull FragmentManager fm) {
        super(fm);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return First.instance();
            case 1:
                return Second.instance();
            case 2:
                return Third.instance();
            case 3:
                return Forth.instance();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return 4;
    }
}

