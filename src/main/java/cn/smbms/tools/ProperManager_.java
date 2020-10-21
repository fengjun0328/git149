package cn.smbms.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ProperManager_ {
    private  static ProperManager_ properManager;
    Properties params;

    /**
     * 私有构造方法    外部不能创建对象
     */
    private ProperManager_(){
        params=new Properties();
        String configFile = "database.properties";
        InputStream is= ProperManager_.class.getClassLoader().getResourceAsStream(configFile);
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
    public  static ProperManager_ getInstance(){

        return  ProperManager_Helper.properManager_();
    }

    /**
     * 静态内部类
     */
    public static class ProperManager_Helper{
        /**
         * 提供一个方法，创建对象
         * @return
         */
        public  static  ProperManager_ properManager_(){
            return  new ProperManager_();
        }
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
