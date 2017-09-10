package com.team_bona.furymonkey.tensecondsplanktimer;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.team_bona.furymonkey.tensecondsplanktimer.Achieve.AchieveFragment;
import com.team_bona.furymonkey.tensecondsplanktimer.Calendar.CalendarFragment;
import com.team_bona.furymonkey.tensecondsplanktimer.Timer.TimerFragment;
import com.team_bona.furymonkey.tensecondsplanktimer.utils.FontClass;
import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;

public class MainActivity extends AppCompatActivity {
    public static FontClass getFontData;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TimerFragment mTimerFragment;
    private AchieveFragment mAchieveFragment;
    private CalendarFragment mCalendarFragment;

    private boolean isPassed = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.team_bona.furymonkey.tensecondsplanktimer.R.menu.menu_main, menu);
        return true;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0)
                return mTimerFragment;
            else if (position == 1)
                return mAchieveFragment;
            else
                return mCalendarFragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FontClass.bold = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/SpoqaHanSans_Bold.ttf");
        FontClass.regular = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/SpoqaHanSans_Regular.ttf");
        FontClass.thin = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/SpoqaHanSans_Thin.ttf");

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/SpoqaHanSans_Regular.ttf"))
                .addBold(Typekit.createFromAsset(this, "fonts/SpoqaHanSans_Bold.ttf"));

        setContentView(com.team_bona.furymonkey.tensecondsplanktimer.R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mTimerFragment = new TimerFragment();
        mAchieveFragment = new AchieveFragment();
        mCalendarFragment = new CalendarFragment();

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(com.team_bona.furymonkey.tensecondsplanktimer.R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTimerFragment.setViewPager(mViewPager);

        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(com.team_bona.furymonkey.tensecondsplanktimer.R.id.tabs);
        Menu m = bottomNavigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case com.team_bona.furymonkey.tensecondsplanktimer.R.id.menu_timer:
                        mViewPager.setCurrentItem(0);
                        break;
                    case com.team_bona.furymonkey.tensecondsplanktimer.R.id.menu_progress:
                        mViewPager.setCurrentItem(1);
                        break;
                    case com.team_bona.furymonkey.tensecondsplanktimer.R.id.menu_calendar:
                        mViewPager.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);

                if (position == 0)
                    isPassed = false;

                if (position >= 1 && !isPassed) {
                    isPassed = true;
                    mTimerFragment.stopTimer();
                    mAchieveFragment.setPassedTime(mTimerFragment.getTimerViewPassedTime());
                    mSectionsPagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = FontClass.regular;
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private class CustomTypefaceSpan extends TypefaceSpan {
        private final Typeface newType;

        public CustomTypefaceSpan(String family, Typeface type) {
            super(family);
            newType = type;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            applyCustomTypeFace(ds, newType);
        }

        @Override
        public void updateMeasureState(TextPaint paint) {
            applyCustomTypeFace(paint, newType);
        }

        private void applyCustomTypeFace(Paint paint, Typeface tf) {
            int oldStyle;
            Typeface old = paint.getTypeface();
            if (old == null) {
                oldStyle = 0;
            } else {
                oldStyle = old.getStyle();
            }

            int fake = oldStyle & ~tf.getStyle();
            if ((fake & Typeface.BOLD) != 0) {
                paint.setFakeBoldText(true);
            }

            if ((fake & Typeface.ITALIC) != 0) {
                paint.setTextSkewX(-0.25f);
            }

            paint.setTypeface(tf);
        }
    }
}
