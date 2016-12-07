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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    private TabPagerAdapter pagerAdapter;

    private TextView textTab1;
    private ImageView mail1;

    private ImageView HeaderIV;
    private TextView HeaderTxv;

    private String token;

    private DrawerListViewAdapter LvAdapter;

    private ListView listView;

    int c;

    // 플로팅 버튼 변수들 //
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;

    // OkHttp 통신 변수들 //
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
                   String aa = data.getStringExtra("a");

                   // 삭제버튼을 눌렀을 때 //
                   if(aa.equals("b")) {
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
                   // 백버튼,   백   ,  답장하기 눌럿을 때 //
                   } else if(aa.equals("a")) {
                       if(a==0) {
                           Fragment frag = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.homeViewPager + ":" + a);
                           Log.e("CALLED", "tag is : please inbox");
                           frag.onActivityResult(requestCode, resultCode, data);
                       }

                       if(a==1) {
                           Fragment frag = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.homeViewPager + ":" + a);
                           Log.e("CALLED11", "tag is : please outbox");
                           frag.onActivityResult(requestCode, resultCode, data);
                       }
                   }

               }
            }
        } else if(requestCode == REQUEST_CODE2) {
            if(resultCode == RESULT_OK) {
                if(data!=null) {
                    String content = data.getStringExtra("content");
                    String messageType = data.getStringExtra("messageType");

                    Fragment frag = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.homeViewPager + ":" + 1);
                    Log.e("CALLED99999", "send tag is : please go to outbox");
                    frag.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
//        else {
//            if(resultCode == RESULT_OK) {
//                if(data != null) {
//                    Fragment frag = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.homeViewPager + ":" + 1);
//                    Log.e("CALLED12351", "reply reply reply reply reply reply reply reply reply reply reply");
//                    frag.onActivityResult(requestCode, resultCode, data);
//                }
//            }
//        }
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

//        fab = (FloatingActionButton)findViewById(R.id.fab);
//        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
//        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);


//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int id = view.getId();
//                switch (id) {
//                    case R.id.fab:
//                        animateFAB();
//                        break;
//                    case R.id.fab1:
//                        Log.d("Raj", "Fab 1");
//                        break;
//                    case R.id.fab2:
//                        Log.d("Raj", "Fab 2");
//                        break;
//                }
////                Intent intent2 = new Intent(getApplicationContext(), Send_Mail.class);
////                intent2.putExtra("authorization", token);
////                startActivityForResult(intent2, REQUEST_CODE2);
//                //startActivity(intent2);
//            }
//        });


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
                Toast.makeText(getApplicationContext(),"Pos onTabSelected: "+tab.getPosition(),Toast.LENGTH_SHORT).show();
                if(tab.getPosition()==0) {
                    ImageView asdf = (ImageView) tab.getCustomView().findViewById(R.id.imageView5);
                    asdf.setImageResource(R.drawable.hometabreceive_on);
                    //tab.setCustomView(R.layout.custom_tab_layout);
                } else if(tab.getPosition()==1) {
                    ImageView asdf = (ImageView) tab.getCustomView().findViewById(R.id.imageView6);
                    asdf.setImageResource(R.drawable.hometabsend_on);
                    //tab.setCustomView(R.layout.custom_tab_layout2);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition()==0) {
                    ImageView asdf = (ImageView) tab.getCustomView().findViewById(R.id.imageView5);
                    asdf.setImageResource(R.drawable.hometabreceive_off);
                    //tab.setCustomView(R.layout.custom_tab_layout4);
                } else if(tab.getPosition()==1) {
                    ImageView asdf = (ImageView) tab.getCustomView().findViewById(R.id.imageView6);
                    asdf.setImageResource(R.drawable.tabsendoff);
                    //tab.setCustomView(R.layout.custom_tab_layout3);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });



    }

//    public void animateFAB() {
//        if (isFabOpen) {
//  //          fab.startAnimation(rotate_backward);
//            fab1.startAnimation(fab_close);
//            fab2.startAnimation(fab_close);
//            //fab3.startAnimation(fab_close);
//            fab1.setClickable(false);
//            fab2.setClickable(false);
//            //fab3.setClickable(false);
//            isFabOpen = false;
//            Log.d("Raj", "close");
//        } else {
////            fab.startAnimation(rotate_forward);
//            fab1.startAnimation(fab_open);
//            fab2.startAnimation(fab_open);
//            //fab3.startAnimation(fab_open);
//            fab1.setClickable(true);
//            fab2.setClickable(true);
//            //fab3.setClickable(true);
//            isFabOpen = true;
//            Log.d("Raj", "open");
//        }
//    }

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
