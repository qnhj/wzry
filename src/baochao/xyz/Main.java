package baochao.xyz;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Main {
    public static void main(String[] args) {



        String path = "E:/wzry";
        File file = new File(path);
        if (!file.exists()){
            file.mkdirs();
        }

        //获取英雄和数字对应关系
        HashMap<String,String> hashMap = NumAndHero.numAndHero();

        Iterator i = hashMap.entrySet().iterator();
        while (i.hasNext()){
            Map.Entry entry = (Map.Entry)i.next();
            //下载图片
            Downloads.begin(path,entry);
        }

        System.out.println("\n图片已保存在"+path);
    }
}
