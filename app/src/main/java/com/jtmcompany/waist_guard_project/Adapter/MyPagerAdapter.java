package com.jtmcompany.waist_guard_project.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.jtmcompany.waist_guard_project.Fragment.BodyInfoFragment;
import com.jtmcompany.waist_guard_project.Fragment.FriendInfoFragment;
import com.jtmcompany.waist_guard_project.Fragment.NotiInfoFragment;

import java.util.ArrayList;
import java.util.List;

//뷰페이저 어댑터
public class MyPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragments=new ArrayList<>();
        List<String> titles_list=new ArrayList<>();
        public MyPagerAdapter(@NonNull FragmentManager fm, boolean isGuardian) {
            super(fm);

            //사용자라면
            if(!isGuardian) {
                titles_list.add("생체정보");
                titles_list.add("친구목록");
                titles_list.add("알림");

                fragments.add(new BodyInfoFragment());
                fragments.add(new FriendInfoFragment());
                fragments.add(new NotiInfoFragment());
            }
            //보호자라면
            else if(isGuardian){
                titles_list.add("친구목록");
                titles_list.add("알림");

                fragments.add(new FriendInfoFragment());
                fragments.add(new NotiInfoFragment());
            }
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }


        @Override
        public CharSequence getPageTitle(int position) { return titles_list.get(position); }

}
