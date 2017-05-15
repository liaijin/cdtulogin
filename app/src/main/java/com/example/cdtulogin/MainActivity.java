package com.example.cdtulogin;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String idstr;
    private String passtr;
    private  ImageView zhuangtai;
    private String urlus="http://10.110.6.11/eportal/webGateModeV2.do?method=login&param=true";
    private String offurl="";
    private CheckBox remenberpass;
    private RadioGroup radioGroup;
    private RadioButton yidong;
    private RadioButton xiaoyuan;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        editor=pref.edit();
        progressDialog=new ProgressDialog(MainActivity.this);//创建进度条
        final EditText id = (EditText) findViewById(R.id.idedit);//获取账号
        final EditText pass = (EditText) findViewById(R.id.passedit);//获取密码
        Button shangxian = (Button) findViewById(R.id.shangxian);//获取上线按钮
        Button xiaxian = (Button) findViewById(R.id.xiaxian);//获取下线按钮
        zhuangtai= (ImageView) findViewById(R.id.zhuangtai);//获取图片
        remenberpass= (CheckBox) findViewById(R.id.remenber_pass);//获取记住密码
        radioGroup= (RadioGroup) findViewById(R.id.radio);//获取单选按钮组
        yidong= (RadioButton) findViewById(R.id.yidongchukou);//获取单选校园按钮
        xiaoyuan= (RadioButton) findViewById(R.id.xiaoyuanchukou);//获取单选出按钮
        boolean isRemember=pref.getBoolean("remember_password",false);
        boolean isrenzheng=pref.getBoolean("renzhengfangshi",true);
        String account=pref.getString("account","");
        String password=pref.getString("password","");
        id.setText(account);
        pass.setText(password);
        if(isRemember)
            remenberpass.setChecked(true);
        if(isrenzheng)
            yidong.setChecked(true);//默认设置 选择移动
        else
            xiaoyuan.setChecked(true);//否则校园
        jianche();
        shangxian.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                showProgressDialog("正在登陆中....",true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Okhttp ok = new Okhttp();
                        idstr=id.getText().toString();
                        passtr=pass.getText().toString();
                        if(yidong.isChecked())//判断选择的是哪个方式
                            urluser(idstr,passtr,1);
                        else
                            urluser(idstr,passtr,2);

                        System.out.print(urlus);
                        String html = "";
                        try {
                            html=ok.getString(urlus);
                            System.out.print(html);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Regex re=new Regex();
                        final String st= re.getelementbyid(html,"toLogOut");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(!st.equals("")) {
                                    if(remenberpass.isChecked()){//检查是否被选中
                                        editor.putString("password",passtr);
                                        editor.putBoolean("remember_password",true);
                                    }
                                    else {
                                        editor.putString("password", "");
                                        editor.putBoolean("remember_password",false);
                                    }
                                    if(yidong.isChecked()){
                                        editor.putBoolean("renzhengfangshi",true);
                                    }
                                    else {
                                        editor.putBoolean("renzhengfangshi",false);
                                    }

                                    editor.putString("account",idstr);

                                    progressDialog.dismiss();//关闭进度条
                                    disProgressDialog();
                                    showToast("登陆成功");
                                    zhuangtai.setImageResource(R.drawable.shangxian);
                                    offurl="http://10.110.6.11/eportal/webGateModeV2.do?method=logout&userIndex="
                                            +st;
                                    editor.putString("offurl",offurl);
                                    editor.apply();
                                }
                                else
                                    showToast("登陆失败");
                            }
                        });
                    }
                }).start();
            }
        });
        xiaxian.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                offurl=pref.getString("offurl","");
                if(!offurl.equals("")){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final Okhttp ok=new Okhttp();
                            String html= null;
                            try {
                                html = ok.getString(offurl);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            final String finalHtml = html;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (!finalHtml.equals("")) {
                                        showToast("下线成功");
                                        zhuangtai.setImageResource(R.drawable.xiaxian);

                                    }
                                }
                            });

                        }
                    }).start();
                }
            }
        });
    }


    public void jianche(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Okhttp ok=new Okhttp();
                String html= null;
                try {
                    html = ok.getString("http://www.baidu.com");
                } catch (IOException e) {
                    e.printStackTrace();
                }



                Regex re=new Regex();
                final String st= re.regex(html,"index.jsp(.*.)'</script>");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!st.equals("")) {
                            zhuangtai.setImageResource(R.drawable.xiaxian);
                            urlus=urlus+st.substring(1);
                        }
                        else
                            zhuangtai.setImageResource(R.drawable.shangxian);
                    }
                });


            }
        }).start();

    }

    public void urluser(String id,String pass,int x){

        if(x==1){
            urlus=urlus+"&net_access_type=%D2%C6%B6%AF";
        }
        urlus=urlus+"&username="+id+"&pwd="+pass;


    }

    //显示一个showtoats
    public void showToast(String str){
        Toast toast=Toast.makeText(this,str,Toast.LENGTH_SHORT);
        toast.show();
    }
    //登陆加载
    public void showProgressDialog(String message,boolean bool){
        progressDialog.setMessage(message);
        progressDialog.setCancelable(bool);
        progressDialog.show();
    }
    //加载取消
    public void disProgressDialog(){
        progressDialog.dismiss();
    }
}
