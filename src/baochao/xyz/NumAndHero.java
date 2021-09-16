package baochao.xyz;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

public class NumAndHero {
    public static HashMap numAndHero() {

        System.out.print("获取资源列表中……");

        //连接英雄编号页面
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;

        StringBuffer stringBuffer = new StringBuffer();

        try {
            url = new URL("https://pvp.qq.com/web201605/herolist.shtml");
            //连接到这个http资源
            httpURLConnection = (HttpURLConnection) url.openConnection();
            //获取输入流
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "gbk");

            char[] buffer = new char[1024];
            int len = 0;
            while ((len = inputStreamReader.read(buffer)) != -1) {
                stringBuffer.append(Arrays.copyOf(buffer, len));
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStreamReader == null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream == null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        //截取字符串
        int begin = stringBuffer.indexOf("输入英雄不存在，请重新输入");
        int over = stringBuffer.indexOf("<!-- 英雄介绍 E -->");
        String body = stringBuffer.substring(begin, over);

        HashMap<String,String> hashMap = new HashMap<>();

        //提取英雄名字和对应编号
        int index;
        while ((index = body.indexOf("yxzj/img201606/heroimg/")) != -1) {
            index = index + "yxzj/img201606/heroimg/".length();
            int oncesOver = body.indexOf("</a></li>");
            String cishu = body.substring(index,oncesOver);

            String heroNum = cishu.substring(0,3);

            int nameBegin = cishu.indexOf("alt=\"");
            String name = cishu.substring(nameBegin+"alt=\"".length());
            int nameOver = name.indexOf('"');
            name = name.substring(0,nameOver);

            hashMap.put(name,heroNum);

            body = body.substring(oncesOver+"</a></li>".length());
        }

        System.out.println("\t完成！");

        return hashMap;
    }
}
