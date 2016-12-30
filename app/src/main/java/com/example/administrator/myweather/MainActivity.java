package com.example.administrator.myweather;

import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {
    private MainActivity seft;
    private Map<String,List<String>> citymap;
    private Spinner provincespinner;
    private Spinner cityspinner;
    private AlertDialog choosedialog;
    private LinearLayout chooselayout;
    private ImageButton settingbtn;
    private ImageButton refreshbtn;
    private void initchoosedialog(){
        choosedialog=new AlertDialog.Builder(seft).setTitle("选择城市").setPositiveButton("确定",new citylistenner()).setNegativeButton("取消",null).create();
    }
    private class citylistenner implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String cityname=cityspinner.getSelectedItem().toString();
            TextView cityna=(TextView) seft.findViewById(R.id.city);
            cityna.setText(cityname);
            new weatherinfotask(seft).execute(cityname);
        }
    }
    private  void initprovinces(){
        AssetManager assmanager=getAssets();
        saxhandler handler=new saxhandler();
        InputStream input=null;
        try{
            input=assmanager.open("City.xml");
            SAXParserFactory factory=SAXParserFactory.newInstance();
            SAXParser parser=factory.newSAXParser();
            parser.parse(input,handler);
            citymap=handler.getCitymap();
        }catch(IOException e){
            e.printStackTrace();
        }catch(ParserConfigurationException e){
            e.printStackTrace();
        }catch (SAXException e){
            e.printStackTrace();
        }finally {
            if(input!=null){
                try{
                    input.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seft=this;
        setContentView(R.layout.activity_main);

    }
}
