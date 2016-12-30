package com.example.administrator.myweather;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class weatherinfotask extends AsyncTask<String,Void,List<Map<String,Object>>>{
    private Activity context;
    private ProgressDialog prodialog;
    private String errormess="网络错误！";
    private ListView weatherinfo;
    private static String baseurl="http://v.juhe.cn/weather/index?format=2&cityname=";
    private static String key="&key=";
    public weatherinfotask(Activity context){
        this.context=context;
        prodialog=new ProgressDialog(context);
        prodialog.setMessage("获取天气中···");
        prodialog.setCancelable(false);
    }

    @Override
    protected List<Map<String, Object>> doInBackground(String... params) {
        List<Map<String,Object>>l=new ArrayList<Map<String,Object>>();
        try{
            HttpClient httpcl=new DefaultHttpClient();
            String url=baseurl+ URLEncoder.encode(params[0],"UTF-8")+key;
            HttpGet httpget=new HttpGet(url);
            HttpResponse httpre=httpcl.execute(httpget);
            if(httpre.getStatusLine().getStatusCode()==200){
                String jsonstr= EntityUtils.toString(httpre.getEntity(),"UTF-8");
                JSONObject jsondata=new JSONObject(jsonstr);
                if(jsondata.getInt("resultcode")==200){
                    JSONObject result=jsondata.getJSONObject("result");
                    JSONArray weatherlist=result.getJSONArray("future");
                    for(int i=0;i<7;i++){
                        Map<String,Object> item=new HashMap<String,Object>();
                        JSONObject weatobject=weatherlist.getJSONObject(i);
                        item.put("temperature",weatobject.getString("temperature"));
                        item.put("weather",weatobject.getString("weather"));
                        item.put("data",weatobject.getString("data"));
                        item.put("week",weatobject.getString("week"));
                        item.put("wind",weatobject.getString("wind"));
                        JSONObject wid=weatobject.getJSONObject("weather_id");
                        int weather_icon=wid.getInt("fa");
                        item.put("weather_icon",weathericon.weathericons[weather_icon]);
                        l.add(item);
                    }
                }else{
                    errormess="暂不支持该城市！";
                }
            }else{
                errormess="没有网络连接！";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        prodialog.show();
    }

    @Override
    protected void onPostExecute(List<Map<String, Object>> maps) {
        super.onPostExecute(maps);
        prodialog.dismiss();
        if (maps.size() > 0) {
            weatherinfo = (ListView) context.findViewById(R.id.weatherinfo);
            SimpleAdapter simadapter = new SimpleAdapter(context, maps, R.layout.weatheritem, new String[]{
                    "temperature", "weather", "data", "week", "weathericon"}, new int[]{
                    R.id.date, R.id.temperature, R.id.weather, R.id.week, R.id.weathericon
            });
            weatherinfo.setAdapter(simadapter);
        } else {
            Toast.makeText(context, errormess, Toast.LENGTH_LONG).show();
        }
    }

}
