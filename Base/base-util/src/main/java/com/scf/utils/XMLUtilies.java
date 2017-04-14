package com.scf.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.converters.basic.DoubleConverter;
import com.thoughtworks.xstream.converters.basic.IntConverter;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * xml 工具类
 * @author wubin
 * @date 2016年8月3日 上午9:32:20 
 * @version V1.1.0
 */
public class XMLUtilies
{
    /**
     * xml转换为map
     * @author wubin
     * @param xmlString
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static Map<String, Object> getMapFromXML(String xmlString)
        throws ParserConfigurationException, IOException, SAXException
    {
        
        // 这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream is = null;
        if (xmlString != null && !xmlString.trim().equals(""))
        {
            is = new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
        }
        Document document = builder.parse(is);
        
        // 获取到document里面的全部结点
        NodeList allNodes = document.getFirstChild().getChildNodes();
        Node node;
        Map<String, Object> map = new HashMap<String, Object>();
        int i = 0;
        while (i < allNodes.getLength())
        {
            node = allNodes.item(i);
            if (node instanceof Element)
            {
                map.put(node.getNodeName(), node.getTextContent());
            }
            i++;
        }
        return map;
    }
    
    
    
    /**
     * 序列化
     * @author wubin
     * @param obj
     * @param dateFormat
     * @return
     */
    public static <T> String getXMLFromObject(T obj, String dateFormat) {
        if (StringUtils.isBlank(dateFormat)) {
            dateFormat = "yyyy-MM-dd HH:mm:ss";
        }
        //XStream xstream = new XStream(new DomDriver("UTF-8"));
        xStream.registerConverter(new DateConverter(dateFormat, new String[] { "yyyy/MM/dd HH:mm:ss" }, TimeZone.getTimeZone("GMT+8")));
        xStream.registerConverter(new DoubleConverter());
        xStream.processAnnotations(obj.getClass());
        String xml = xStream.toXML(obj);
        if(!StringUtils.isEmpty(xml)){
            xml = xml.replace("__", "_");
        }
        return xml;
    }


    /** 
     * 反序列化
     */
    @SuppressWarnings("unchecked")
    public static <T> T getObjectFromXML(String text, Class<T> clazz, String dateFormat) {
        if (StringUtils.isBlank(dateFormat)) {
            dateFormat = "yyyy-MM-dd HH:mm:ss";
        }
        XStream xstream = new XStream(new DomDriver("UTF-8"));
        xstream.registerConverter(new DateConverter(dateFormat, new String[] { "yyyy/MM/dd HH:mm:ss" }, TimeZone.getTimeZone("GMT+8")));
        xstream.registerConverter(new DoubleConverter());
        xstream.registerConverter(new IntConverter());
        xstream.ignoreUnknownElements();// 暂时忽略掉一些新增的字段
        xstream.processAnnotations(clazz);
        return (T) xstream.fromXML(text);
    }
    
    
    /**
     * 从xml文件反序列化到object
     * 
     * @param relativeClassPath 相对于classpath的路径
     * @param cls 反序列化的class对象
     * @return
     */
     @SuppressWarnings("unchecked")
     public static <T> T  getObjectFromXMLFile(String relativeClassPath,Class<T> cls, String dateFormat) {
         if (StringUtils.isBlank(dateFormat)) {
             dateFormat = "yyyy-MM-dd HH:mm:ss";
         }
         InputStream in = XMLUtilies.class.getClassLoader().getResourceAsStream(relativeClassPath);
         XStream xstream = new XStream(new DomDriver("UTF-8"));
         xstream.registerConverter(new DateConverter(dateFormat, new String[] { "yyyy/MM/dd HH:mm:ss" }, TimeZone.getTimeZone("GMT+8")));
         xstream.registerConverter(new DoubleConverter());
         xstream.registerConverter(new IntConverter());
         xstream.ignoreUnknownElements();// 暂时忽略掉一些新增的字段
         xstream.processAnnotations(cls);
         return (T)xstream.fromXML(in);
     }
    
    /**
     * 输出cdata，后续可以再加上进一步区分某些属性(自定义注解标注)需要cdata的功能
     */
    private static XStream xStream = new XStream(new XppDriver()
    {
        public HierarchicalStreamWriter createWriter(Writer out)
        {
            return new PrettyPrintWriter(out)
            {
                boolean cdata = true;
                
                @SuppressWarnings("rawtypes")
                public void startNode(String name, Class clazz)
                {
                    super.startNode(name, clazz);
                }
                
                protected void writeText(QuickWriter writer, String text)
                {
                    if (cdata)
                    {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    }
                }
            };
        }
    });
}
