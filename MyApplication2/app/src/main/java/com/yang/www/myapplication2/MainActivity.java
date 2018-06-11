package com.yang.www.myapplication2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "请点击右上角按钮", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        doSomeThing();
    }

    public void doSomeThing() {
        final TextView textView = (TextView) findViewById(R.id.textView4);

        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                System.out.println("main---------"+msg+"-------------" + Thread.currentThread().getName());
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(msg.obj.toString());
                    jsonObject=jsonObject.getJSONObject("data");
                    JSONObject today = (JSONObject) jsonObject.getJSONArray("forecast").get(0);
                    textView.setText(
                            "湿度："+jsonObject.getString("shidu")+"\n"+
                            "pm2.5："+jsonObject.getString("pm25")+"\n"+
                            "空气质量："+jsonObject.getString("quality")+"\n"+
                                    "天气："+today.getString("type")+"\n"+
                                    "风向："+today.getString("fx")+"\n"+
                                    "最高温度："+today.getString("high")+"\n"+
                                    "最低温度："+today.getString("low")+"\n");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        };

        EditText city = (EditText) findViewById(R.id.editText2);
        final Editable cont = city.getText();


        Button search = (Button) findViewById(R.id.button2);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String r = HttpClientUtil.sendGet("https://www.sojson.com/open/api/weather/json.shtml?city="+cont);
                        System.out.println("second----------------------" + Thread.currentThread().getName());
                        Message message = new Message();
                        message.obj =r;
                        mHandler.sendMessage(message);
                    }
                }).start();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "name:杨东泉\n NO.17214739", Toast.LENGTH_SHORT).show();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
