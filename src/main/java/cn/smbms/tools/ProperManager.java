package cn.smbms.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ProperManager {
    private  static  ProperManager properManager;
    Properties params;

    /**
     * 私有构造方法    外部不能创建对象
     */
    private  ProperManager(){
        params=new Properties();
        String configFile = "database.properties";
        InputStream is= ProperManager.class.getClassLoader().getResourceAsStream(configFile);
        try {
            params.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 向外界提供一个公共的方法
     * @return
     */
    public synchronized static  ProperManager getInstance(){
        if(properManager==null){
            properManager=new ProperManager();//自己创建对象
        }
        return  properManager;
    }


    /**
     * 实例方法
     * @param key
     * @return
     */
    public String getValueByKey(String key){
        return params.getProperty(key);
    }
}
