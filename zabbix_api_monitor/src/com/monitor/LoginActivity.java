package com.monitor;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.monitor.R;

import android.app.Activity;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity{
	 protected static final String TAG = null;
	 private EditText user =null,passwd = null;
	 private Button login =null;	
	 private String username =null, password =null;
	 
	 
	 protected void onCreate(Bundle savedInstanceState) {
	        // TODO Auto-generated method stub
	        super.onCreate(savedInstanceState);
	        this.setContentView(R.layout.login);
	        user =(EditText) findViewById(R.id.username);
	        passwd =(EditText) findViewById(R.id.password);
	        login =(Button) findViewById(R.id.login_but);
	       

			
	        login.setOnClickListener(new OnClickListener(){
	        		        	
				public void onClick(View v) {
					
					username =user.getText().toString().trim();
					password =passwd.getText().toString().trim();
					
             	    if (username==null || username.equals("") ){
             			showUser();
             		  } else if (password==null ||password.equals("")){
             			showPassword();
             		  }else{
					new Thread(){  
			            @Override  
			            public void run() {  
			            	Looper.prepare(); 
                          try{
						  HttpClient httpclient = new DefaultHttpClient();  
				          String zabbix_url = "http://192.168.12.196:80/api_jsonrpc.php"; 
				          String auth =null;
				          //封装json用户信息
		 		          JSONObject params = new JSONObject();
							params.put("user",username);
							params.put("password",password);
						 //封装json请求参数
				          JSONObject json = new JSONObject().put("jsonrpc", "2.0")
				  				.put("method", "user.login").put("params", params)
				  				.put("id", 0);
				          HttpPost httppost = new HttpPost(zabbix_url);
				          httppost.setEntity(new StringEntity(json.toString(), "UTF-8"));
				          httppost.addHeader("Content-Type", "application/json; charset=utf-8");
				          HttpResponse response = httpclient.execute(httppost);
				          //检验状态码，如果成功接收数据  
				          int code = response.getStatusLine().getStatusCode(); 
				          //Log.v("code", Integer.toString(code));
				          /**
				           * 这段代码处理逻辑：１.先判断json是否包含key-result
				           * 2.因为json要是不包含某个关键字会报异常
				           * 3.如果code==200，下一步判断是否包含result,若在，下一步获取result值
				           */
				          String  result = EntityUtils.toString(response.getEntity());
				           if (code == 200) {
				        	   //判断返回值json数据是否包含key－result
				        	   if(result.contains("result")) {
							          JSONTokener jsonParser = new JSONTokener(result); 
							          JSONObject js = (JSONObject) jsonParser.nextValue(); 
			                      	   Log.v("auth", js.getString("result"));
			                      	   auth=js.getString("result").toString();
				        		    	 Intent intent = new Intent();
				                          intent.setClass(LoginActivity.this, XinxiActivity.class);
				                          intent.putExtra("token", auth);  
				                          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				                          startActivity(intent); 
				        	      }
				        	    else showLogin();
                              }
				           else showServer();
                    }
                    catch (IOException e) {  
                        e.printStackTrace();  
                    } catch (JSONException e) {
						e.printStackTrace();
					}  
                    catch (ParseException e) {  
                        e.printStackTrace();  
                    }
                    handler.sendEmptyMessage(0);
                    Looper.loop();  
			            }  
			        }.start();
             		  }
				}   
	        });

	 }
 	private void showUser(){
			   //调用显示Toast对话框
 		       Toast toast=Toast.makeText(LoginActivity.this, "请输入用户名,谢谢", Toast.LENGTH_SHORT); 
 		       toast.setGravity(Gravity.CENTER, 0, -200);  
 		       toast.show();  
			};
    private void showPassword(){
				   //调用显示Toast对话框
	 		       Toast toast=Toast.makeText(LoginActivity.this, "请输入密码,谢谢", Toast.LENGTH_SHORT); 
	 		       toast.setGravity(Gravity.CENTER, 0, 0);  
	 		       toast.show();  
				};
    private void showServer(){
					   //调用显示Toast对话框
		 		       Toast toast=Toast.makeText(LoginActivity.this, "服务器连接不上，请稍后再连!", Toast.LENGTH_SHORT); 
		 		       toast.setGravity(Gravity.CENTER, 0, 0);  
		 		       toast.show();  
					};
    private void showLogin(){
						   //调用显示Toast对话框
			 		       Toast toast=Toast.makeText(LoginActivity.this, "用户名或密码不正确!", Toast.LENGTH_SHORT); 
			 		       toast.setGravity(Gravity.CENTER, 0, 0);  
			 		       toast.show();  
						};
 	
   //定义检查输入框是否符合标准
	 private Handler handler = new Handler() {  
		  
	        public void handleMessage(Message msg) {  
	            switch (msg.what) {  
	                case 0:  
	                  
	                    break;  
	            }  
	        };  
	    };
}

