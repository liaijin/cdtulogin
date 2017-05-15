package com.example.cdtulogin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 李艾劲 on 2017/4/22.
 */
/*
*正则表达式
 *  */
public class Regex {

    public String regex(String str,String Expression ){
        Pattern p=Pattern.compile(Expression );//正则表达式
        Matcher m=p.matcher(str);
        if(m.find()){
           return m.group(1);

        }
        else
            return "";
    }
    public String getelementbyid(String str,String id){
        Document document= Jsoup.parse(str);
        if(document.getElementById(id)!=null){
            Elements links = document.getElementsByTag("script");
            for(Element element:links){
                Pattern p=Pattern.compile("Index='(.*)';d.contentDive.ifPhone");//正则表达式
                Matcher m=p.matcher(element.data());
                if(m.find()){
                    return m.group(1);
                }
            }
        }
        return "";
    }
}
