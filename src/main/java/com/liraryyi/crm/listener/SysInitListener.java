package com.liraryyi.crm.listener;

import com.liraryyi.crm.settings.dao.DicTypeDao;
import com.liraryyi.crm.settings.dao.DicValueDao;
import com.liraryyi.crm.settings.domain.DicType;
import com.liraryyi.crm.settings.domain.DicValue;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

/**
 * 监听器在web.xml中最先被执行，因此在监听器方法执行时，无法进行bean的自动注入；!!!!!!!!!!! 这真的是最坑的一点，因此跳过service层
 * 最后采用这个方法:
 * https://blog.csdn.net/ccccc_chuang/article/details/107749124
 */

public class SysInitListener implements ServletContextListener {

    /**
     * 这个监听器是为了从数据库中拿到数据字典的值，并在web项目启动时，把它保存到全局作用域对象中，方便读取
     *
     * 该方法是用来监听上下文域对象的方法，当服务器启动，上下文域对象创建
     * 对象创建完毕后，马上执行该方法
     *
     * event:该参数能够取得监听的对象
     */
    public void contextInitialized(ServletContextEvent event){

        ApplicationContext context =
                WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());

        //通过getbean得到值
        DicTypeDao dicTypeDao =  context.getBean(DicTypeDao.class);
        DicValueDao dicValueDao = context.getBean(DicValueDao.class);

        //取得当前web的全局作用域对象
        ServletContext application = event.getServletContext();

        /*
        数据字典里的数据应按照typeCode进行分类，以后读取也是按照typeCode进行读取
        所以
        业务层根据typeCode拿到数据放到List里，再一起放进map里传回来
         */
        Map<String,List<DicValue>> map = new HashMap<>();

        List<DicType> list = dicTypeDao.getDicTypeList();

        for (DicType dicType:list){

            String code = dicType.getCode();

            List dicValueList =  dicValueDao.getDicValueByType(code);

            map.put(code, dicValueList);
        }

        //全局作用域对象中以键值对的方式进行保存，我们应该以（typeCode,List<DicValue>）的形式保存
        //用keySet()方法拿到map集合中的key（typeCode）部分
        Set<String> set =map.keySet();

        //遍历set集合，通过set中的key再拿到value，并通过setAttribute方法保存到全局作用域对象中
        for (String key:set){
            application.setAttribute(key,map.get(key));
        }

        //-----------------------------------------------------------------------
        //数据字典处理完毕后，处理stage2possibility.properties文件
        /*
           处理步骤：
           1.解析该文件，将属性文件中的键值对关系处理成为java中键值对的关系(map)
           2.Map<String(阶段stage),String(可能性possibility)> map2
             map2.put("资质审查", 10)
         */
        Map<String,String> map2 = new HashMap<>();

        //解析properties文件
        ResourceBundle resourceBundle = ResourceBundle.getBundle("conf.Stage2Possibility");

        Enumeration<String> e = resourceBundle.getKeys();

        while (e.hasMoreElements()){
            //阶段
            String key = e.nextElement();

            //可能性
            String value = resourceBundle.getString(key);

            map2.put(key,value);
        }

        application.setAttribute("map2",map2);
    }
}
