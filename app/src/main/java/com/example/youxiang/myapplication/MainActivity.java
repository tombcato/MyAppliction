package com.example.youxiang.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.example.youxiang.myapplication.animators.LandingAnimator;
import com.example.youxiang.myapplication.explosionfield.ExplosionField;
import com.example.youxiang.myapplication.headerfooterrecyclerview.RecyclerViewHeaderFooterAdapter;
import com.example.youxiang.myapplication.helper.OnStartDragListener;
import com.example.youxiang.myapplication.helper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;


public class MainActivity extends AppCompatActivity implements OnStartDragListener{

    private ArrayList<Model> mDatas;
    private RecyclerView mRecyclerView;
    private ItemTouchHelper helper;
    private MainRecyclerAdapter mAdapter;
    private PtrClassicFrameLayout mPtrFrame;
    private ExplosionField mExplosionField;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview_horizontal);
        initDrawLayout();
        initToolsBar();
        initPtrFrame();

        initImageLoader();

        initDatas();

        mExplosionField = ExplosionField.attach2Window(this);
//        addListener(findViewById(R.id.root));

        //得到控件
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview_horizontal);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        GridLayoutManager gridManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(gridManager);
        mRecyclerView.setHasFixedSize(true);

        ExampleIntermediary intermediary = new ExampleIntermediary(this,mDatas);
//
        final RecyclerViewHeaderFooterAdapter adapter = new RecyclerViewHeaderFooterAdapter(gridManager,intermediary);
        adapter.addHeader(ExampleHeaderFooter.getView(this, "Header"));
        adapter.addFooter(ExampleHeaderFooter.getView(this, "Footer"));

        intermediary.setOnItemClickLitener(new ExampleIntermediary.OnItemClickLitener() {

            @Override
            public void onItemClick(View view, int position) {
                if(position == RecyclerView.NO_POSITION || adapter.isHeader(position) || adapter.isFooter(position)) return;
                Toast.makeText(MainActivity.this, position - adapter.getHeaderSize() + "", Toast.LENGTH_SHORT)
                        .show();
                Bitmap bm = BitmapFactory.decodeResource(getResources(), mDatas.get(position - adapter.getHeaderSize()).i);
                Palette.Builder builder = Palette.from(bm);
                builder.generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {

                        Palette.Swatch vibrant = palette.getDarkVibrantSwatch();//有活力

                        if (vibrant != null) {
                            mRecyclerView.setBackgroundColor(vibrant.getRgb());
                            toolbar.setBackgroundColor(vibrant.getRgb());
                        }
                    }
                });

            }

            @Override
            public boolean onItemLongClick(View view, int position) {
//                onStartDrag((RecyclerView.ViewHolder)view);
                return true;
            }

            @Override
            public void onItemTextClick(final View view, int position) {
                mExplosionField.explode(view);
                mExplosionField.setOnExplosionListener(new ExplosionField.ExplosionListener() {
                    @Override
                    public void onExplosionDone() {
                        mExplosionField.resetView(view);
                    }
                });
                adapter.onItemDismiss(position);
            }
        });

        mRecyclerView.setItemAnimator(new LandingAnimator());
        mRecyclerView.getItemAnimator().setAddDuration(500);
        mRecyclerView.getItemAnimator().setRemoveDuration(500);
        mRecyclerView.setAdapter(adapter);

        SimpleItemTouchHelperCallback callback = new SimpleItemTouchHelperCallback(adapter);
        helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);


    }



    private void initToolsBar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("Toolbar");//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setBackgroundColor(Color.LTGRAY);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("youxiang");
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.hello_world, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void initDrawLayout() {
        mDrawerLayout  = (DrawerLayout) findViewById(R.id.drawerlayout);
    }

    private void initDatas() {

//    	 mDatas = new ArrayList<Integer>(Arrays.asList(R.drawable.icon_home_welcome_1,
//                 R.drawable.icon_home_welcome_2, R.drawable.icon_home_welcome_3, R.drawable.icon_home_welcome_4,
//                 R.drawable.icon_home_welcome_5, R.drawable.icon_home_welcome_6, R.drawable.icon_home_welcome_7,
//                 R.drawable.icon_home_welcome_1,R.drawable.icon_home_welcome_2, R.drawable.icon_home_welcome_3,
//                 R.drawable.icon_home_welcome_4));
        mDatas = new ArrayList<Model>();
        mDatas.add(new Model(R.drawable.icon_home_welcome_1,1));
        mDatas.add(new Model(R.drawable.icon_home_welcome_2,2));
        mDatas.add(new Model(R.drawable.icon_home_welcome_3,3));
        mDatas.add(new Model(R.drawable.icon_home_welcome_4,4));
        mDatas.add(new Model(R.drawable.icon_home_welcome_5,5));
        mDatas.add(new Model(R.drawable.icon_home_welcome_6,6));
        mDatas.add(new Model(R.drawable.icon_home_welcome_7,7));
        mDatas.add(new Model(R.drawable.icon_home_welcome_1,8));
        mDatas.add(new Model(R.drawable.icon_home_welcome_2,9));
        mDatas.add(new Model(R.drawable.icon_home_welcome_3,10));
        mDatas.add(new Model(R.drawable.icon_home_welcome_4,11));
        mDatas.add(new Model(R.drawable.icon_home_welcome_5,12));
        mDatas.add(new Model(R.drawable.icon_home_welcome_6,13));
        mDatas.add(new Model(R.drawable.icon_home_welcome_7,14));
        mDatas.add(new Model(R.drawable.icon_home_welcome_1,15));
        mDatas.add(new Model(R.drawable.icon_home_welcome_2,16));
        mDatas.add(new Model(R.drawable.icon_home_welcome_3,17));
	}

    public class Model{
        public final  int i;
        public final  int id;

        public Model(int i,int id){
            this.i = i;
            this.id = id;
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        helper.startDrag(viewHolder);
    }


    public void initImageLoader(){
    }
    public void initPtrFrame(){
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.store_house_ptr_frame);
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(300);
        mPtrFrame.setDurationToCloseHeader(1000);
        mPtrFrame.setPullToRefresh(false);
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        mPtrFrame.setEnabledNextPtrAtOnce(true);
        PtrClassicDefaultHeader defaultHeader = new PtrClassicDefaultHeader(this);
        final StoreHouseHeader header = new StoreHouseHeader(this);
        header.setTextColor(Color.parseColor("#33b5e5"));
        header.setBackgroundColor(Color.CYAN);
        header.initWithString("TOMBCAT");
        mPtrFrame.setHeaderView(defaultHeader);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();
                    }
                }, 1800);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return true;
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
    }
}
