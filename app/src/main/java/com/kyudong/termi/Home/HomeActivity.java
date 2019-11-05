package com.kyudong.termi.Home;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.clans.fab.FloatingActionMenu;
import com.kyudong.termi.DrawerLayout.DrawerLVItem;
import com.kyudong.termi.DrawerLayout.DrawerListViewAdapter;
import com.kyudong.termi.ExplainCustomDialog;
import com.kyudong.termi.InBox.InBoxRvItem;
import com.kyudong.termi.InBox.Inbox;
import com.kyudong.termi.Login;
import com.kyudong.termi.MainActivity;
import com.kyudong.termi.R;
import com.kyudong.termi.Send_Mail;
import com.kyudong.termi.UserToken;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static final int REQUEST_CODE = 1003;
    private static final int REQUEST_CODE2 = 1004;

    private ViewPager explainViewpager;
    private ExplainViewPagerAdapter explainViewPagerAdapter;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Toolbar toolbar;
    private ImageView imageView;
    private NavigationView nav_View;
    private Menu navMenu;
    private SwitchCompat switcher;
    private TextView switcherTxv;
    private TabPagerAdapter pagerAdapter;
    private CoordinatorLayout fl_main;

    private ExplainCustomDialog dialog;

    // 날짜 관련 //
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
    private String today;
    private Date ChirstmasDate;
    private Date todayDate;
    private int compare;

    private TextView textTab1;
    private ImageView mail1;

    private ImageView HeaderIV;
    private TextView HeaderTxv;

    private String token;

    private DrawerListViewAdapter LvAdapter;

    private ListView listView;

    int c;

    FrameLayout frame;

    // 플로팅 버튼 변수들 //
    private Boolean isFabOpen = false;
    private FloatingActionMenu menu;
    private com.github.clans.fab.FloatingActionButton fab, fab1, fab2, fab3;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;

    // OkHttp 통신 변수들 //
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    SharedPreferences.Editor sEditor;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
               if(data != null) {

                   int a = data.getExtras().getInt("int");
                   String aa = data.getStringExtra("a");

                   // 삭제버튼을 눌렀을 때 //
                   if(aa.equals("b")) {
                       if(a==0) {
                           Fragment frag = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.homeViewPager + ":" + a);
                           frag.onActivityResult(requestCode, resultCode, data);
                       }
                       if(a==1) {
                           Fragment frag = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.homeViewPager + ":" + a);
                           frag.onActivityResult(requestCode, resultCode, data);
                       }
                   // 백버튼,   백   ,  답장하기 눌럿을 때 //
                   } else if(aa.equals("a")) {
                       if(a==0) {
                           Fragment frag = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.homeViewPager + ":" + a);
                           frag.onActivityResult(requestCode, resultCode, data);
                       }

                       if(a==1) {
                           Fragment frag = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.homeViewPager + ":" + a);
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
                    frag.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
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
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar, R.string.app_name, R.string.app_name);

        nav_View  = (NavigationView) findViewById(R.id.nav_view);
        nav_View.setNavigationItemSelectedListener(this);
        navMenu = nav_View.getMenu();
        MenuItem menuItem = navMenu.findItem(R.id.notice);
        View actionView = MenuItemCompat.getActionView(menuItem);

        switcher = (SwitchCompat) actionView.findViewById(R.id.switcher);
        switcherTxv = (TextView) actionView.findViewById(R.id.switcherTxv);

        switcher.setChecked(true);
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switcher.isChecked()) {
                    switcherTxv.setText("ON");
                } else if(!switcher.isChecked()) {
                    switcherTxv.setText("OFF");
                }
            }
        });

        if(Build.VERSION.SDK_INT>=21){
            getWindow().setStatusBarColor(Color.parseColor("#e0e0e0"));
        }

        menu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        fab1 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab2);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        menu.setClosedOnTouchOutside(true);

        menu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    //menu.set
                    fab1.setVisibility(View.VISIBLE);
                    fab2.setVisibility(View.VISIBLE);
                } else {
                    fab1.setVisibility(View.GONE);
                    fab2.setVisibility(View.GONE);
                }
            }
        });

        fab1.setOnClickListener(onButtonClick());
        fab2.setOnClickListener(onButtonClick());

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menu.isOpened()) {
                    menu.close(true);
                }
            }
        });


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
                    ImageView asdf = (ImageView) tab.getCustomView().findViewById(R.id.imageView5);
                    asdf.setImageResource(R.drawable.hometabreceive_on);
                } else if(tab.getPosition()==1) {
                    ImageView asdf = (ImageView) tab.getCustomView().findViewById(R.id.imageView6);
                    asdf.setImageResource(R.drawable.hometabsend_on);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition()==0) {
                    ImageView asdf = (ImageView) tab.getCustomView().findViewById(R.id.imageView5);
                    asdf.setImageResource(R.drawable.hometabreceive_off);
                } else if(tab.getPosition()==1) {
                    ImageView asdf = (ImageView) tab.getCustomView().findViewById(R.id.imageView6);
                    asdf.setImageResource(R.drawable.tabsendoff);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private View.OnClickListener onButtonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                today = df.format(date);

                try {
                    ChirstmasDate = df.parse("2016-12-23 00:00");
                    todayDate = df.parse(today);

                    compare = todayDate.compareTo(ChirstmasDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(v == fab1) {
                    if(compare > 0) {
                        Toast.makeText(HomeActivity.this, "이벤트 기간이 종료됐습니다!\n오리지널 터미를 이용해주세요!", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent2 = new Intent(getApplicationContext(), Send_Mail.class);
                        intent2.putExtra("authorization", token);
                        intent2.putExtra("isXmas", "true");
                        startActivityForResult(intent2, REQUEST_CODE2);
                        menu.close(true);
                    }
                } else if(v==fab2) {
                    Intent intent2 = new Intent(getApplicationContext(), Send_Mail.class);
                    intent2.putExtra("authorization", token);
                    intent2.putExtra("isXmas", "false");
                    startActivityForResult(intent2, REQUEST_CODE2);
                    menu.close(true);
                }
            }
        };
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.logout) {
            Toast.makeText(HomeActivity.this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();
            LogOutPost logOutPost = new LogOutPost();
            try {
                logOutPost.doPostRequest("http://52.78.240.168/api/signout", "A");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(id == R.id.delete) {
        } else if(id == R.id.notice) {
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
                                UserToken.setPreferences(getApplicationContext(), "token", "empty");

                                JSONObject jsonObject = new JSONObject(res);

                                int code = jsonObject.getInt("ResponseCode");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });

                }
            });
            return null;
        }
    }
}
