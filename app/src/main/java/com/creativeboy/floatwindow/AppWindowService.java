package com.creativeboy.floatwindow;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

public class AppWindowService extends Service {
    private static final String TAG="MyService";
    private WindowManager wm;
    private WindowManager.LayoutParams params;
    private LinearLayout linearLayout;
    private Button floatButton;
    public AppWindowService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate");
        createFloatWindow();
    }
    public void createFloatWindow() {
        wm = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        Log.d(TAG,"windowManger---->"+wm);
        //设置window type 下拉通知栏时不可见
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式 背景为透明
        params.format = PixelFormat.RGBA_8888;
        //设置窗口不可聚焦
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //设置悬浮窗停靠位置
        params.gravity = Gravity.LEFT|Gravity.TOP;
        params.x = 0;
        params.y = 0;
        //设置悬浮窗口的长和宽
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //获得悬浮窗布局
        linearLayout = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.float_view,null);
        //添加布局
        wm.addView(linearLayout,params);
        floatButton = (Button) linearLayout.findViewById(R.id.float_button);
        floatButton.setOnTouchListener(new View.OnTouchListener() {
            int lastX,lastY;
            int paramsX,paramsY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //移动前点击时位置坐标
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        Log.d(TAG,"lastX--->"+lastX);
                        Log.d(TAG,"lastY--->"+lastY);
                        paramsX = params.x;
                        paramsY = params.y;

                        break;

                    case MotionEvent.ACTION_MOVE:
                        //移动的距离
                        int dx = (int) (event.getRawX()-lastX);
                        int dy = (int) (event.getRawY()-lastY);
                        //当前窗口的位置
                        params.x = paramsX+dx;
                        params.y = paramsY+dy;

                        //更新悬浮窗位置
                        wm.updateViewLayout(linearLayout, params);
                        break;

                }
                //返回true将会进行消费 即点击事件将不会生效
                return false;
            }
        });
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"点击了按钮");
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
        if(linearLayout!=null) {
            //移除悬浮窗口
            wm.removeView(linearLayout);
        }
    }
}
