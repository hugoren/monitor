package com.monitor;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.monitor.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class XinxiActivity extends Activity{
	 private TextView text =null,redis_text = null;
	 private Button rt =null,but=null,redis_but=null;
	 
	 protected void onCreate(Bundle savedInstanceState) {
	        // TODO Auto-generated method stub
	        super.onCreate(savedInstanceState);
	        this.setContentView(R.layout.xinxi);
	        text =(TextView) findViewById(R.id.text);
	        redis_text =(TextView) findViewById(R.id.redis_text);
	        rt =(Button) findViewById(R.id.rt);
	        but =(Button) findViewById(R.id.but);
	        redis_but =(Button) findViewById(R.id.redis_but);
	        
			//获取主机信息
			but.setOnClickListener(new OnClickListener(){
               
				private void show(String msg){
				   //调用显示Toast对话�?
				    Toast.makeText(XinxiActivity.this, msg, Toast.LENGTH_LONG).show();
				};
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
               	new Thread(){  
		            @Override  
		            public void run() {  
		            	Looper.prepare(); 
                   try{
                   String token = getIntent().getStringExtra("token");
                   HttpClient httpclient = new DefaultHttpClient();  
				   String zabbix_url = "http://192.168.12.196:80/api_jsonrpc.php"; 
                   JSONObject params = new JSONObject();
                   String output = "{hostid,name}";
                   String filter ="{[Zabbix server],[Linux server]}";
                   params.put("output", "extend");
                   params.put("filter",filter);
                   JSONObject json = new JSONObject().put("jsonrpc", "2.0")
			  				.put("method", "host.get").put("params", params)
			  				.put("id", 0).put("auth", token);
                   Log.v("json", json.toString());
                   HttpPost post = new HttpPost(zabbix_url);
			       post.setEntity(new StringEntity(json.toString(), "UTF-8"));
			       post.addHeader("Content-Type", "application/json; charset=utf-8");
			       HttpResponse response = httpclient.execute(post);
			       String  result = EntityUtils.toString(response.getEntity());
			       Log.v("result", result.toString());
			       JSONTokener jsonParser = new JSONTokener(result); 
			          JSONObject js1 = (JSONObject) jsonParser.nextValue(); 
			          Log.v("88",js1.getString("jsonrpc").toString());
			          
			          Bundle bundle =new Bundle();
			          Message message = new Message();
			          message.what = 0;
			          //将信息返回主线程，再在handleMessag设定显示
			          bundle.putString("jsonrpc", result.toString());
			          message.setData(bundle);
			          handler.sendMessage(message);
                   }
                   catch(Exception EX){
                	   EX.getStackTrace();
                   }
                   
                   Looper.loop();  
		            }  
		        }.start(); 
				  }
	        });
			redis_but.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
                  
					
				}
	        	
	        });
			
			//�?��按钮
	    	rt.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存�?
					 System.exit(0);
				}
	    		
	    		
	    	});

	 }
	 private Handler handler = new Handler() {  
		  
	        public void handleMessage(Message msg) {  
	            switch (msg.what) {  
	                case 0:
	                	String jsonrpc = msg.getData().getString("jsonrpc");
	                	text.setText(jsonrpc);
	                    break;  
	            }  
	        };  
	    };
}