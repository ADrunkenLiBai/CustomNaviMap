package com.amap.navi.demo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviException;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.NextTurnTipView;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.navi.demo.R;

import java.lang.reflect.Method;
import java.util.ArrayList;


public class CustomRouteActivity extends BaseActivity implements AMapNaviListener {

    NaviLatLng wayPoint = new NaviLatLng(39.935041, 116.447901);
    NaviLatLng wayPoint1 = new NaviLatLng(39.945041, 116.447901);
    NaviLatLng wayPoint2 = new NaviLatLng(39.955041, 116.447901);
    NaviLatLng wayPoint3 = new NaviLatLng(39.965041, 116.447901);
    private NextTurnTipView nextTurnTipView;
    private TextView tvStepRetain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWayPointList = new ArrayList<NaviLatLng>();
        setContentView(R.layout.activity_basic_navi);
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);

        AMapNaviViewOptions options = mAMapNaviView.getViewOptions();

        options.setLayoutVisible(false);

        nextTurnTipView = findViewById(R.id.next_turn_tip_view);
        tvStepRetain = findViewById(R.id.text_step_retain);


        mWayPointList.add(wayPoint);
        mWayPointList.add(wayPoint1);
        mWayPointList.add(wayPoint2);
        mWayPointList.add(wayPoint3);

    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        /**
         * 方法: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); 参数:
         *
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         *  说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         *  注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
         */
        int strategy = 0;
        try {
            //再次强调，最后一个参数为true时代表多路径，否则代表单路径
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onNaviInfoUpdate(NaviInfo naviinfo) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(naviinfo.getIconData(), 0, naviinfo.getIconData().length, null);
        nextTurnTipView.setImageBitmap(bitmap);
        Log.d("当前剩余距离 ---- >", naviinfo.getCurStepRetainDistance() + "");
        tvStepRetain.setText(String.valueOf(naviinfo.getCurStepRetainDistance()));
    }

    @Override
    public void onCalculateRouteSuccess(int[] ids) {
//        如果根据获取的导航路线来自定义绘制
        RouteOverLay routeOverlay = new RouteOverLay(mAMapNaviView.getMap(), mAMapNavi.getNaviPath(), this);
        routeOverlay.setStartPointBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.r1));
        routeOverlay.setEndPointBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.b1));
        routeOverlay.setWayPointBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.b2));
        routeOverlay.setTrafficLine(false);
        try {
            routeOverlay.setWidth(30);
        } catch (AMapNaviException e) {
            //宽度须>0
            e.printStackTrace();
        }
        int color[] = new int[10];
        color[0] = Color.BLACK;
        color[1] = Color.RED;
        color[2] = Color.BLUE;
        color[3] = Color.YELLOW;
        color[4] = Color.GRAY;
        routeOverlay.addToMap(color, mAMapNavi.getNaviPath().getWayPointIndex());
//        routeOverlay.addToMap();

        mAMapNavi.startNavi(AMapNavi.EmulatorNaviMode);
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }


    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
        super.showCross(aMapNaviCross);

    }
}
