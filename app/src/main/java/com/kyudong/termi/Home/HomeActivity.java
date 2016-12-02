package com.kyudong.termi.Home;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kyudong.termi.DrawerLayout.DrawerLVItem;
import com.kyudong.termi.DrawerLayout.DrawerListViewAdapter;
import com.kyudong.termi.InBox.InBoxRvItem;
import com.kyudong.termi.InBox.Inbox;
import com.kyudong.termi.R;
import com.kyudong.termi.Send_Mail;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static final int REQUEST_CODE = 1003;
    private static final int REQUEST_CODE2 = 1004;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Toolbar toolbar;
    private ImageView imageView;
    private NavigationView nav_View;
    private FloatingActionButton fab;
    private TabPagerAdapter pagerAdapter;

    private TextView textTab1;
    private ImageView mail1;

    private ImageView HeaderIV;
    private TextView HeaderTxv;

    private String token;

    private DrawerListViewAdapter LvAdapter;

    private ListView listView;

    int c;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    SharedPreferences sPref;
    SharedPreferences.Editor sEditor;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                if(data != null) {
                    int position = data.getExtras().getInt("pos");
                    int a = data.getExtras().getInt("int");

                    if(a==0) {
                        Fragment frag = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.homeViewPager + ":" + a);
                        Log.e("CALLED", "tag is : please inbox");
                        frag.onActivityResult(requestCode, resultCode, data);
                    }

                    if(a==1) {
                        Fragment frag = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.homeViewPager + ":" + a);
                        Log.e("CALLED", "tag is : please outbox");
                        frag.onActivityResult(requestCode, resultCode, data);
                    }
                }
            }
        } else if(requestCode == REQUEST_CODE2) {
            if(resultCode == RESULT_OK) {
                if(data!=null) {
                    Fragment frag = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.homeViewPager + ":" + 1);
                    Log.e("CALLED", "send tag is : please go to outbox");
                    frag.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
        Log.e("CALLED", "OnActivity Result");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        if(intent != null) {

            token = intent.getStringExtra("authorization");
        }

        imageView = (ImageView) findViewById(R.id.menuImage);
        toolbar = (Toolbar)findViewById(R.id.homeToolbar);
        setSupportActionBar(toolbar);

        nav_View  = (NavigationView) findViewById(R.id.nav_view);
        nav_View.setNavigationItemSelectedListener(this);

        if(Build.VERSION.SDK_INT>=21){
            getWindow().setStatusBarColor(Color.parseColor("#e0e0e0"));
        }

        fab = (FloatingActionButton)findViewById(R.id.fab);

        //fab.setBackgroundResource(R.drawable.fab_shape_rectangle_266_rectangle_267_triangle_12);
        //fab.setImageResource(R.drawable.fab_shape_rectangle_266_rectangle_267_triangle_12);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), Send_Mail.class);
                intent2.putExtra("authorization", token);
                startActivityForResult(intent2, REQUEST_CODE2);
                //startActivity(intent2);
            }
        });


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar, R.string.app_name, R.string.app_name);

        mDrawerToggle.setDrawerIndicatorEnabled(false);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        mTabLayout = (TabLayout)findViewById(R.id.homeTabLayout);

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout inboxRelative = (RelativeLayout)inflater.inflate(R.layout.custom_tab_layout, null);
        RelativeLayout outboxRelative = (RelativeLayout)inflater.inflate(R.layout.custom_tab_layout2,null);

        mTabLayout.addTab(mTabLayout.newTab().setCustomView(R.layout.custom_tab_layout));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(R.layout.custom_tab_layout2));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        mViewPager = (ViewPager)findViewById(R.id.homeViewPager);
        pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(),mTabLayout.getTabCount());
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mViewPager.setOffscreenPageLimit(3);

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0) {
                    ImageView imageView = (ImageView) tab.getCustomView().findViewById(R.id.mail1);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.logout) {
            Toast.makeText(HomeActivity.this, "로그아웃", Toast.LENGTH_SHORT).show();
            LogOutPost logOutPost = new LogOutPost();
            try {
                logOutPost.doPostRequest("http://52.78.240.168/api/signout", "A");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(id == R.id.delete) {
            Toast.makeText(HomeActivity.this, "계정삭제", Toast.LENGTH_SHORT).show();
        } else if(id == R.id.notice) {
            Toast.makeText(HomeActivity.this, "알림", Toast.LENGTH_SHORT).show();
        }

        mDrawerLayout.closeDrawer(GravityCompat.END);

        return true;
    }

    public class LogOutPost {

        String doPostRequest(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .header("Content-Type", "application/json")
                    .addHeader("authorization", token)
                    .url(url)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    final String res = response.body().string();


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                SharedPreferences sPref = getSharedPreferences("a", MODE_PRIVATE);
                                SharedPreferences.Editor sEditor = sPref.edit();

                                sEditor.putString("authorization", "");
                                sEditor.commit();

                                JSONObject jsonObject = new JSONObject(res);

                                int code = jsonObject.getInt("ResponseCode");
                                Toast.makeText(getApplicationContext(), "responseCode : " + code, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });
            return null;
        }
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        mDrawerToggle.onConfigurationChanged(newConfig);
//    }
//
//    @Override
//    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        mDrawerToggle.syncState();
//    }



}
