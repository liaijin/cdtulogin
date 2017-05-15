package com.example.cdtulogin;

import org.json.JSONArray;
import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 李艾劲 on 2017/4/21.
 */

public class Okhttp {
    private String user="";
    private String pass="";
    //请求登陆的网址
    private String url="http://10.110.6.11/eportal/webGateModeV2.do?method=login&param=true&";
    private boolean is=false;

    //同步的get请求  返回response
    public Response getResponse(String url) throws IOException {

        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .build();

        Call call=client.newCall(request);
        Response response =call.execute();
        return response;
    }
    //同步的get请求 返回String
    public String getString(String url) throws IOException {
       Response response=getResponse(url);
        return response.body().string();
    }
    //get请求
    public void getht(String id,String password,int x){
        user=id;
        pass=password;
        OkHttpClient client=new OkHttpClient();
        if(x==1){
            url=url+"&net_access_type=%D2%C6%B6%AF";
        }
        url=url+"&username="+id+"&pwd="+password;
        Request request=new Request.Builder()
                .url(url)
                .build();

        Call call=client.newCall(request);
        try {
            Response response =call.execute();

            Document document=Jsoup.parse(response.body().string());
            if(document.getElementById("toLogOut")!=null){
                Elements links = document.getElementsByTag("script");
                for(Element element:links){
                    Pattern p=Pattern.compile("Index='(.*)';d.contentDive.ifPhone");//正则表达式
                    Matcher m=p.matcher(element.data());
                    if(m.find()){
                        is=true;
                    }
                }
            }
            else{
                is=false;
            }

        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            System.exit(0);
        }
    }
    public void lixian(String str){
        OkHttpClient client=new OkHttpClient();
        String url="http://10.110.6.11/eportal/webGateModeV2.do?method=logout&userIndex=";
        url=url+str;
        Request request=new Request.Builder()
                .url(url)
                .build();

        Call call=client.newCall(request);
        call.enqueue(new Callback(){

                         @Override
                         public void onFailure(Call arg0, IOException arg1) {
                             // TODO 自动生成的方法存根
                             System.out.println("离线失败");
                         }
                         @Override
                         public void onResponse(Call arg0, Response arg1) throws IOException {
                             // TODO 自动生成的方法存根
                             //System.out.println(arg1.body().string());
                             System.out.println("离线成功");
                         }
                     }
        );
    }

    public void testget(){
        OkHttpClient client=new OkHttpClient();
        String linshi="http://www.baidu.com";

        Request request=new Request.Builder()
                .url(linshi)
                .build();

        Call call=client.newCall(request);
        try {
            Response response =call.execute();
            Pattern p=Pattern.compile("index.jsp(.*.)'</script>");//正则表达式
            Matcher m=p.matcher(response.body().string());
                if(m.find()){
                    this.url=this.url+m.group(1).substring(1);
                }
                else {
                }
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            System.exit(0);
        }
    }

    public boolean getis(){
        return is;
    }


}
