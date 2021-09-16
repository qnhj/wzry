package baochao.xyz;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Downloads {
    public static void begin(String path, Map.Entry entry){
        //拼接url
        URL url = getUrl(entry.getValue().toString());
        //获取皮肤列表名称
        List<String> nameList = getNumName(url);
        //下载图片
        int i = 1;
        for(String name : nameList){

            System.out.print("下载："+entry.getKey()+"-"+name+"……");

            File file = new File(path+"/"+entry.getKey()+"-"+name+".jpg");
            HttpURLConnection httpURLConnection =null;
            InputStream inputStream =null;
            FileOutputStream fileOutputStream = null;

            try {
                URL url1 = new URL("https://game.gtimg.cn/images/yxzj/img201606/skin/hero-info/"+entry.getValue()+"/"+entry.getValue()+"-bigskin-"+i+".jpg");
                httpURLConnection  = (HttpURLConnection)url1.openConnection();
                inputStream = httpURLConnection.getInputStream();

                fileOutputStream = new FileOutputStream(file);

                //缓冲区
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) !=-1){
                    fileOutputStream.write(buffer,0,len);
                }
                fileOutputStream.flush();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (inputStream != null) {
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
            i++;

            System.out.println("\t 完成！");
        }

    }

    private static URL getUrl(String num){
        URL url = null;
        try {
            url = new URL("https://pvp.qq.com/web201605/herodetail/"+num+".shtml");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static List getNumName(URL url){
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;

        StringBuffer stringBuffer = new StringBuffer();

        try {
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
        int begin = stringBuffer.indexOf("<ul class=\"pic-pf-list pic-pf-list3\"");
        int over = stringBuffer.indexOf("<p>皮肤名称</p></li>-->");
        String body = stringBuffer.substring(begin, over);

        begin = body.indexOf("data-imgname=\"");
        over = body.indexOf("\">");

        body = body.substring(begin+"data-imgname=\"".length(),over-"\">".length());

        List<String> list = new ArrayList<>();

        while (body.indexOf('&') != -1){
            String name = body.substring(0,body.indexOf('&'));

            list.add(name);

            if (body.indexOf('|') == -1){
                break;
            }
            body = body.substring(body.indexOf('|')+1);
            
        }
        

        return list;
    }

}
