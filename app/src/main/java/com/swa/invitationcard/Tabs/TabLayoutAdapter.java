//package com.swa.invitationcard.Tabs;
//
//import android.content.Context;
//
//import androidx.annotation.Nullable;
//import androidx.annotation.StringRes;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentPagerAdapter;
//
//public class TabLayoutAdapter extends FragmentPagerAdapter {
//
//    @StringRes
//    private static final int[] TAB_TITLES = new int[]{"Cards", "Invitation Management"};
//    private final Context mContext;
//    public TabLayoutAdapter(Context context, FragmentManager fm) {
//        super(fm);
//        mContext = context;
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        // getItem is called to instantiate the fragment for the given page.
//        // Return a PlaceholderFragment (defined as a static inner class below).
//        Fragment fragmentSelected = null;
//        switch(position)
//        {
//            case 0:
//                fragmentSelected = new AttendanceSummary();
//                break;
//            case 1:
//                fragmentSelected = new LeaveHistory();
//                break;
//        }
//        return fragmentSelected;
////        return PlaceholderFragment.newInstance(position + 1);
//    }
//
//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return mContext.getResources().getString(TAB_TITLES[position]);
//    }
//
//    @Override
//    public int getCount() {
//        // Show 2 total pages.
//        return 2;
//    }
//}
