package com.fc.v2.util.AutoCode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.fc.v2.model.custom.autocode.AutoCodeConfig;
import com.fc.v2.model.custom.autocode.TableInfo;
import com.fc.v2.util.SnowflakeIdWorker;
import com.fc.v2.util.StringUtils;

import cn.hutool.core.date.DateTime;

/**
 * 自动生成 通用类
* @ClassName: AutoCodeUtil
* @author fuce
* @date 2019-11-20 22:05
 */
public class AutoCodeUtil {

	/**生成文件路径**/
	private static String targetPath = "c://";
	public static List<String> getTemplates(){
        List<String> templates = new ArrayList<String>();

        //java代码模板
        templates.add("auto_code/model/Entity.java.vm");
        templates.add("auto_code/model/EntityExample.java.vm");
        templates.add("auto_code/mapperxml/EntityMapper.xml.vm");
        templates.add("auto_code/service/EntityService.java.vm");
        templates.add("auto_code/mapper/EntityMapper.java.vm");
        templates.add("auto_code/controller/EntityController.java.vm");
        //前端模板
        templates.add("auto_code/html/list.html.vm");
        templates.add("auto_code/html/add.html.vm");
        templates.add("auto_code/html/edit.html.vm");
        //sql模板
        templates.add("auto_code/sql/menu.sql.vm");
        //templates.add("auto_code/说明.txt.vm");
        return templates;
    }
	
	
	/**
	 * 创建单表
	 * @param tableName 表名
	 * @param conditionQueryField  条件查询字段
	 * @param pid 父id
	 * @param sqlcheck 是否录入数据
	 * @param vhtml 生成html
	 * @param vController 生成controller
	 * @param vservice 生成service
	 * @param vMapperORdao 生成mapper or dao
	 * @author fuce
	 * @Date 2019年8月24日 下午11:44:54
	 */
	public static void autoCodeOneModel(TableInfo tableInfo,Boolean vhtml,Boolean vController,Boolean vService,Boolean vMapperORdao){
		AutoCodeConfig autoCodeConfig=new AutoCodeConfig();
		//设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
        Velocity.init(prop);
		
		Map<String, Object> map = new HashMap<>();
        //数据库表数据
		map.put("tableInfo",tableInfo);
        //字段集合
        map.put("beanColumns",tableInfo.getBeanColumns());
        //配置文件
        map.put("SnowflakeIdWorker", SnowflakeIdWorker.class);
        //class类路径
        map.put("parentPack", autoCodeConfig.getConfigkey("parentPack"));
        //作者
        map.put("author", autoCodeConfig.getConfigkey("author"));
        //时间
        map.put("datetime",new DateTime());
        
        
        VelocityContext context = new VelocityContext(map);
        
        //获取模板列表
        List<String> templates = getTemplates();
        if(vhtml!=true) {
        	templates.remove("auto_code/html/list.html.vm");
        	templates.remove("auto_code/html/add.html.vm");
        	templates.remove("auto_code/html/edit.html.vm");
        } 
        if (vController!=true) {
        	templates.remove("auto_code/controller/EntityController.java.vm");
		} 
        if (vService!=true) {
			templates.remove("auto_code/service/EntityService.java.vm");
		} 
        if (vMapperORdao!=true) {
			templates.remove("auto_code/model/Entity.java.vm");
			templates.remove("auto_code/model/EntityExample.java.vm");
        	templates.remove("auto_code/mapperxml/EntityMapper.xml.vm");
        	templates.remove("auto_code/mapper/EntityMapper.java.vm");
		}
        
        
        for (String template : templates) {
        	try {
        		if(template.contains("menu.sql.vm")) {
//        			if(sqlcheck==1) {//执行sql
//        				Template tpl = Velocity.getTemplate(template, "UTF-8" );
//            			StringWriter sw = new StringWriter(); 
//            			tpl.merge(context, sw);
//            			System.out.println(sw);
//            			executeSQL(sysUtilService, sw.toString());
//        			}else {//只输出
//        				Template tpl = Velocity.getTemplate(template, "UTF-8" );
//            			StringWriter sw = new StringWriter(); 
//            			tpl.merge(context, sw);
//            			System.out.println(sw);
//        			}
        			
        		}else {
        			String filepath=getCoverFileName(template,"a" ,tableInfo.getJavaTableName() ,autoCodeConfig.getConfigkey("parentPack"), "model","gen");
    		        Template tpl = Velocity.getTemplate(template, "UTF-8" );
    				File file = new File(filepath);
    				if (!file.getParentFile().exists())
    		            file.getParentFile().mkdirs();
    		        if (!file.exists())
    		            file.createNewFile();
					try (FileOutputStream outStream = new FileOutputStream(file);
						 OutputStreamWriter writer = new OutputStreamWriter(outStream, "UTF-8");
						 BufferedWriter sw = new BufferedWriter(writer)) {
						tpl.merge(context, sw);
						sw.flush();
						System.out.println("成功生成Java文件:" + filepath);
					}
        		}
	        	
        	} catch (IOException e) {
                try {
					throw new Exception("渲染模板失败，表名：" +"c"+"\n"+e.getMessage());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
            }
        }
	}
	
	
	

	/**
     * 获取覆盖路径
     */
    public static String getCoverFileName(String template,String classname,String className, String packageName, String moduleName,String controller) {
        
    	String packagePath =targetPath+File.separator+"src"+File.separator + "main" + File.separator + "java" + File.separator;
        String resourcesPath=targetPath+File.separator+"src"+File.separator + "main" + File.separator+"resources"+ File.separator;;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator;
        }

        if (template.contains("Entity.java.vm")) {//model.java
            return packagePath+moduleName +File.separator+ "auto" + File.separator + className + ".java";
        }
        if(template.contains("EntityExample.java.vm")) {//modelExample.java
        	return packagePath+moduleName +File.separator+ "auto" + File.separator + className + "Example.java";
        }
        
        if (template.contains("EntityMapper.java.vm")) {//daomapper.java
            return packagePath + "mapper" + File.separator + "auto" + File.separator + className + "Mapper.java";
        }
        if (template.contains("EntityMapper.xml.vm")) {//daomapper.xml
            return resourcesPath+"mybatis" + File.separator+"auto"+ File.separator + className + "Mapper.xml";
        }
        
        if (template.contains("EntityService.java.vm")) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }
        if(template.contains("EntityController.java.vm")) {
        	 return packagePath + "controller" + File.separator + controller + File.separator + className + "Controller.java";
        }
        if(template.contains("list.html.vm")) {
        	 return resourcesPath+"templates"+File.separator + controller+File.separator + classname+File.separator +"list.html";
        }
        if(template.contains("add.html.vm")) {
        	System.err.println(resourcesPath+"templates"+File.separator + controller+File.separator + classname+File.separator );
       	 	 return resourcesPath+"templates"+File.separator + controller+File.separator + classname+File.separator +"add.html";
        }
        if(template.contains("edit.html.vm")) {
       	 	return  resourcesPath+"templates"+File.separator + controller+File.separator + classname+File.separator +"edit.html";
        }
        return null;
    }
}
