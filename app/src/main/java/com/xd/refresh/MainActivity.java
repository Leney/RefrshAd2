package com.xd.refresh;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xd.refresh.manager.Constance;
import com.xd.refresh.manager.thread.AdTask;
import com.xd.refresh.manager.thread.ThreadPoolManager;
import com.xd.refresh.util.Tools;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Tools.getScreenWidhtAndHeight(MainActivity.this);

        Button testBtn = (Button) findViewById(R.id.begin_test_btn);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdTask adTask1 = new AdTask(Constance.TTGY_AD_UNIT_IDS, Constance.KDXF_APP_ID, Constance.KDXF_APP_NAME, Constance.KDXF_APP_PACKAGE_NAME, 1);
//                AdTask adTask2 = new AdTask(Constance.TTGY_AD_UNIT_IDS, Constance.KDXF_APP_ID, Constance.KDXF_APP_NAME, Constance.KDXF_APP_PACKAGE_NAME, 2);
//                AdTask adTask3 = new AdTask(Constance.TTGY_AD_UNIT_IDS, Constance.KDXF_APP_ID, Constance.KDXF_APP_NAME, Constance.KDXF_APP_PACKAGE_NAME, 3);
//		AdTask adTask4 = new AdTask(Constance.TTGY_AD_UNIT_IDS, Constance.KDXF_APP_ID, Constance.KDXF_APP_NAME, Constance.KDXF_APP_PACKAGE_NAME, 4);
//		AdTask adTask5 = new AdTask(Constance.TTGY_AD_UNIT_IDS, Constance.KDXF_APP_ID, Constance.KDXF_APP_NAME, Constance.KDXF_APP_PACKAGE_NAME, 5);

                ThreadPoolManager.getInstance().addTask(adTask1);
//                ThreadPoolManager.getInstance().addTask(adTask2);
//                ThreadPoolManager.getInstance().addTask(adTask3);
//		ThreadPoolManager.getInstance().addTask(adTask4);
//		ThreadPoolManager.getInstance().addTask(adTask5);

            }
        });
    }
}
