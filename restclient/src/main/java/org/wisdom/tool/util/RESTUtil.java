/*
 * Copyright 2017-present, Yudong (Dom) Wang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wisdom.tool.util;

import java.awt.SplashScreen;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.wisdom.tool.cache.RESTCache;
import org.wisdom.tool.constant.RESTConst;
import org.wisdom.tool.model.Cause;
import org.wisdom.tool.model.Causes;
import org.wisdom.tool.model.Charsets;
import org.wisdom.tool.model.ErrCode;
import org.wisdom.tool.model.HttpHist;
import org.wisdom.tool.model.HttpHists;
import org.wisdom.tool.model.HttpRsp;
import org.wisdom.tool.model.Results;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.SourceFormatter;

/** 
* @ClassName: RESTUtil 
* @Description: Rest utility 
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@qq.com 
* @Date: 2017-07-22 PM 10:42:57 
* @Version: Wisdom RESTClient V1.3 
*/
public class RESTUtil
{
    private static Logger log = LogManager.getLogger(RESTUtil.class);
    
    /**
    * 
    * @Title: toOject 
    * @Description: Json file to object 
    * @param @param jf
    * @param @param clas
    * @param @return     
    * @return T    
    * @throws
     */
    public static <T> T toOject(File jf, Class<T> clas)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            return (T) mapper.readValue(jf, clas);
        }
        catch(Exception e)
        {
            log.error("Change json file [" + jf.getName() + "] to object failed.", e);
        }

        return null;
    }

    /**
    * 
    *
    * @Title: toOject 
    * @Description: Json file input stream to object  
    * @param @param is
    * @param @param clas
    * @param @return 
    * @return T
    * @throws
     */
    public static <T> T toOject(InputStream is, Class<T> clas)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            return (T) mapper.readValue(is, clas);
        }
        catch(Exception e)
        {
            log.error("Change json file input stream to object failed.", e);
        }

        return null;
    }
    
    /**
    * 
    * @Title: toOject 
    * @Description: Json content to object 
    * @param @param content
    * @param @param clas
    * @param @return     
    * @return T    
    * @throws
     */
    public static <T> T toOject(String content, Class<T> clas)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            return (T) mapper.readValue(content, clas);
        }
        catch(Exception e)
        {
            log.error("Change json text [" + content + "] to object failed.", e);
        }

        return null;
    }

    /**
    * 
    * @Title: tojsonText 
    * @Description: Instance to json string 
    * @param @param instance
    * @param @return     
    * @return String    
    * @throws
     */
    public static <T> String tojsonText(T instance)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(instance);
        }
        catch(Exception e)
        {
            log.error("Write object [" + instance + "] as json string failed.", e);
        }

        return StringUtils.EMPTY;
    }

    /**
    * 
    * @Title: toJsonFile 
    * @Description: Instance to json file 
    * @param @param jf
    * @param @param instance
    * @param @return     
    * @return void    
    * @throws
     */
    public static <T> void toJsonFile(File jf, T instance)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            mapper.writerWithDefaultPrettyPrinter().writeValue(jf, instance);
        }
        catch(Exception e)
        {
            log.error("Write object [" + instance + "] as json file [" + jf.getName() + "] failed.", e);
        }
    }
    
    /**
    * 
    * @Title: getComparator 
    * @Description: New a comparator 
    * @param @return 
    * @return Comparator<String>
    * @throws
     */
    private static Comparator<String> getComparator()
    {
        Comparator<String> c = new Comparator<String>()
        {
            public int compare(String o1, String o2)
            {
                return o1.compareTo(o2);
            }
        };
        
        return c;
    }
    
    /**
    * 
    * @Title: sort 
    * @Description: Sort JSON element 
    * @param @param e 
    * @return void
    * @throws
     */
    public static void sort(JsonElement e)
    {
        if (e.isJsonNull())
        {
            return;
        }

        if (e.isJsonPrimitive())
        {
            return;
        }

        if (e.isJsonArray())
        {
            JsonArray a = e.getAsJsonArray();
            for (Iterator<JsonElement> it = a.iterator(); it.hasNext();)
            {
                sort(it.next());
            }
            return;
        }

        if (e.isJsonObject())
        {
            Map<String, JsonElement> tm = new TreeMap<String, JsonElement>(getComparator());
            for (Entry<String, JsonElement> en : e.getAsJsonObject().entrySet())
            {
                tm.put(en.getKey(), en.getValue());
            }

            for (Entry<String, JsonElement> en : tm.entrySet())
            {
                e.getAsJsonObject().remove(en.getKey());
                e.getAsJsonObject().add(en.getKey(), en.getValue());
                sort(en.getValue());
            }
            return;
        }
    }
    
    /**
    * 
    * @Title: sort 
    * @Description: Sort JSON string by key 
    * @param @param jsonStr
    * @param @return 
    * @return String
    * @throws
     */
    public static String sort(String jsonStr)
    {
        Gson g = new GsonBuilder().setPrettyPrinting().create();
        JsonParser p = new JsonParser();
        JsonElement e = p.parse(jsonStr);

        sort(e);

        return g.toJson(e);
    }
        
    /**
    * 
    * @Title: compare 
    * @Description: Compare two json text 
    * @param @param jtxt1
    * @param @param jtxt2
    * @param @return     
    * @return boolean    
    * @throws
     */
    public static boolean compare(String jstr1, String jstr2)
    {
        JsonParser p = new JsonParser();
        JsonElement e1 = p.parse(jstr1);
        JsonElement e2 = p.parse(jstr2);

        sort(e1);
        sort(e2);

        return e1.equals(e2);
    }

    /**
    * 
    * @Title: isJson 
    * @Description: Check if it is JSON string 
    * @param @param content
    * @param @return 
    * @return boolean
    * @throws
     */
    public static boolean isJson(String json)
    {
        if (StringUtils.isEmpty(json))
        {
            return false;
        }

        if (!StringUtils.contains(json, "{"))
        {
            return false;
        }

        JsonParser p = new JsonParser();
        try
        {
            p.parse(json);
        }
        catch(JsonSyntaxException e)
        {
            log.debug("Bad json format: " + lines(1) + json);
            return false;
        }

        return true;
    }

    /**
    * 
    * @Title: isXml 
    * @Description: Check if it is XML string  
    * @param @param xml
    * @param @return 
    * @return boolean
    * @throws
     */
    public static boolean isXml(String xml)
    {
        if (StringUtils.isEmpty(xml))
        {
            return false;
        }

        try
        {
            DocumentHelper.parseText(xml);
        }
        catch(DocumentException e)
        {
            log.debug("Bad xml format: " + lines(1) + xml);
            return false;
        }

        return true;
    }

    /**
    * 
    * @Title: isHtml 
    * @Description: Check if it is HTML string 
    * @param @param html
    * @param @return 
    * @return boolean
    * @throws
     */
    public static boolean isHtml(String html)
    {
        if (StringUtils.isEmpty(html))
        {
            return false;
        }

        if (StringUtils.containsIgnoreCase(html, RESTConst.HTML_LABEL))
        {
            return true;
        }

        return false;
    }

    /**
    * 
    * @Title: prettyJson 
    * @Description: Pretty JSON formatter 
    * @param @param json
    * @param @return 
    * @return String
    * @throws
     */
    public static String prettyJson(String json)
    {
        if (StringUtils.isBlank(json))
        {
            return StringUtils.EMPTY;
        }

        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(json);
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        String prettyJson = gson.toJson(je);
        return prettyJson;
    }

    /**
    * 
    * @Title: prettyXml 
    * @Description: Pretty XML formatter  
    * @param @param xml
    * @param @return 
    * @return String
    * @throws
     */
    public static String prettyXml(String xml)
    {
        XMLWriter xw = null;

        if (StringUtils.isBlank(xml))
        {
            return StringUtils.EMPTY;
        }

        try
        {
            Document doc = DocumentHelper.parseText(xml);

            // Format
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding(Charsets.UTF_8.getCname());

            // Writer
            StringWriter sw = new StringWriter();
            xw = new XMLWriter(sw, format);
            xw.write(doc);
            return sw.toString();
        }
        catch(Exception e)
        {
            log.error("Failed to format xml.", e);
        } 
        finally
        {
            close(xw);
        }

        return StringUtils.EMPTY;
    }

    /**
    * 
    * @Title: prettyHtml 
    * @Description: Pretty HTML formatter   
    * @param @param html
    * @param @return 
    * @return String
    * @throws
     */
    public static String prettyHtml(String html)
    {
        if (StringUtils.isBlank(html))
        {
            return StringUtils.EMPTY;
        }

        try
        {
            // Writer
            StringWriter sw = new StringWriter();
            new SourceFormatter(new Source(html))
            .setIndentString("    ")
            .setTidyTags(true)
            .setCollapseWhiteSpace(true)
            .writeTo(sw);
            return sw.toString();
        }
        catch(Exception e)
        {
            log.error("Failed to format html.", e);
        } 

        return StringUtils.EMPTY;
    }

    /**
    * 
    * @Title: format 
    * @Description: Format text 
    * @param @param txt
    * @param @return 
    * @return String
    * @throws
     */
    public static String format(String txt)
    {
        if (StringUtils.isBlank(txt))
        {
            return StringUtils.EMPTY;
        }

        if (isJson(txt))
        {
            return prettyJson(txt);
        }

        if (isHtml(txt))
        {
            return prettyHtml(txt);
        }

        if (isXml(txt))
        {
            return prettyXml(txt);
        }

        return txt;
    }

    /**
    * 
    * @Title: sleep 
    * @Description: Thread sleep
    * @param @param millis 
    * @return void
    * @throws
     */
    public static void sleep(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch(InterruptedException e)
        {
            log.error("Sleep interrupted.", e);
        }
    }

    /**
    * 
    * @Title: lines 
    * @Description: Get lines 
    * @param @param num
    * @param @return 
    * @return String
    * @throws
     */
    public static String lines(int num)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++)
        {
            sb.append("\r\n");
        }
        return sb.toString();
    }
    
    /**
    * 
    * @Title: nowDate 
    * @Description: Get now date 
    * @param @return 
    * @return String
    * @throws
     */
    public static String nowDate()
    {
        SimpleDateFormat fmat = new SimpleDateFormat(RESTConst.DATE_FORMAT);
        return fmat.format(new Date());
    }

    /**
    * 
    * @Title: getInputStream 
    * @Description: get input stream 
    * @param @param path
    * @param @return 
    * @return InputStream
    * @throws
     */
    public static InputStream getInputStream(String path)
    {
        return RESTUtil.class.getClassLoader().getResourceAsStream(path);
    }

    /**
    * 
    * @Title: close 
    * @Description: Close input stream 
    * @param @param is 
    * @return void
    * @throws
     */
    public static void close(InputStream is)
    {
        if (null == is)
        {
            return;
        }
        try
        {
            is.close();
            is = null;
        }
        catch(IOException e)
        {
            log.error("Failed to close input stream", e);
        }
    }

    /**
    * 
    * @Title: getCause 
    * @Description: Get test failure/error cause 
    * @param @param code
    * @param @param args
    * @param @return 
    * @return Cause
    * @throws
     */
    public static Cause getCause(ErrCode code, String... args)
    {
        Cause c = new Cause();
        if (null == code)
        {
            return c;
        }

        Causes cs = RESTCache.getCauses();
        if (null == cs || MapUtils.isEmpty(cs.getCauses()))
        {
            return c;
        }

        c = new Cause(cs.getCauses().get(code.getCode()));
        c.setCode(code);
        if (null == args)
        {
            return c;
        }

        String msgStr = c.getMsgEnUS();
        for (int i = 0; i < args.length; i++)
        {
            msgStr = msgStr.replaceFirst("<" + (i + 1) + ">", "[ " + args[i] + " ]");
        }
        c.setMsgEnUS(msgStr);

        msgStr = c.getMsgZhCN();
        for (int i = 0; i < args.length; i++)
        {
            msgStr = msgStr.replaceFirst("<" + (i + 1) + ">", "[ " + args[i] + " ]");
        }
        c.setMsgZhCN(msgStr);
        return c;
    }
    
    /**
    * 
    * @Title: testResult 
    * @Description: Set test result 
    * @param @param hist
    * @param @param oldRsp
    * @param @param newRsp 
    * @return void
    * @throws
     */
    public static void testResult(HttpHists hists, HttpHist hist, HttpRsp newRsp)
    {
        Cause cs = null;

        HttpRsp oldRsp = hist.getRsp();
        String oldBdy = oldRsp.getBody();
        String newBdy = newRsp.getBody();

        if (null == oldBdy)
        {
            oldBdy = StringUtils.EMPTY;
        }

        if (null == newBdy)
        {
            newBdy = StringUtils.EMPTY;
        }
    
        Integer oldSC = oldRsp.getStatusCode();
        Integer newSC = newRsp.getStatusCode();

        hist.setResult(Results.PASS);
        hist.getRsp().setDate(newRsp.getDate());
        hist.getRsp().setTime(newRsp.getTime());
        hist.getRsp().setRawTxt("");
        hist.getRsp().setBody("");
        hist.getRsp().setHeaders(null);
        hist.getReq().setRawTxt("");
        hist.getReq().setBody("");
        hist.getReq().setHeaders(null);
        hist.getReq().setCookies(null);

        if (null == oldSC || null == newSC)
        {
            hist.setResult(Results.ERROR);
            cs = RESTUtil.getCause(ErrCode.HTTP_REQUEST_FAILED);
            hist.setCause(cs.toString());
            hists.countErr();
            return;
        }

        if (!oldSC.equals(newSC))
        {
            hist.setResult(Results.FAILURE);
            cs = RESTUtil.getCause(ErrCode.INCONSISTENT_STATUS, String.valueOf(newSC), String.valueOf(oldSC));
            hist.setCause(cs.toString());
            hists.countFail();
            return;
        }

        if (!hist.getAssertBdy())
        {
            cs = RESTUtil.getCause(ErrCode.SUCCESS);
            hist.setCause(cs.toString());
            hists.countPass();
            return;
        }

        boolean isFailed = false;
        if (isJson(oldBdy))
        {
            if (!diff(oldBdy, newBdy, hist.getExcludedNodes()))
            {
                isFailed = true;
            }
        }
        else
        {
            if (!oldBdy.equals(newBdy))
            {
                isFailed = true;
            }
        }

        if (isFailed)
        {
            hist.setResult(Results.FAILURE);
            cs = RESTUtil.getCause(ErrCode.INCONSISTENT_BODY);
            hist.setCause(cs.toString());
            hists.countFail();
            return;
        }

        cs = RESTUtil.getCause(ErrCode.SUCCESS);
        hist.setCause(cs.toString());
        hists.countPass();
    }

    /**
    * 
    * @Title: rerunResult 
    * @Description: Set rerun result 
    * @param @param hist
    * @param @param oldRsp
    * @param @param newRsp 
    * @return void
    * @throws
     */
    public static void rerunResult(HttpHists hists, HttpHist hist, HttpRsp newRsp)
    {
        Cause cs = null;

        HttpRsp oldRsp = hist.getRsp();
        String oldBdy = oldRsp.getBody();
        String newBdy = newRsp.getBody();
        String newMsg = newRsp.getMessage();

        if (null == oldBdy)
        {
            oldBdy = StringUtils.EMPTY;
        }

        if (null == newBdy)
        {
            newBdy = StringUtils.EMPTY;
        }
    
        Integer oldSC = oldRsp.getStatusCode();
        Integer newSC = newRsp.getStatusCode();

        hist.setResult(Results.PASS);
        hist.getRsp().setDate(newRsp.getDate());
        hist.getRsp().setTime(newRsp.getTime());
        hist.getRsp().setStatusCode(newSC);
        hist.getRsp().setMessage(newMsg);
        hist.getRsp().setRawTxt("");
        hist.getRsp().setBody("");
        hist.getRsp().setHeaders(null);
        hist.getReq().setRawTxt("");
        hist.getReq().setBody("");
        hist.getReq().setHeaders(null);
        hist.getReq().setCookies(null);

        if (null == oldSC || null == newSC)
        {
            hist.setResult(Results.ERROR);
            cs = RESTUtil.getCause(ErrCode.HTTP_REQUEST_ERR);
            hist.setCause(cs.toString());
            hists.countErr();
            return;
        }

        if (RESTConst.HTTP_OK != newSC)
        {
            hist.setResult(Results.FAILURE);
            hist.setCause(newMsg);
            hists.countFail();
            return;
        }

        cs = RESTUtil.getCause(ErrCode.SUCCESS);
        hist.setCause(cs.toString());
        hists.countPass();
    }

    /**
    * 
    * @Title: replacePath 
    * @Description: replace file path 
    * @param @param path
    * @param @return 
    * @return String
    * @throws
     */
    public static String replacePath(String path)
    {
        return StringUtils.replaceOnce(path, 
               RESTConst.WISDOM_TOOL,
               RESTConst.WORK + File.separatorChar);
    }

    /**
    * 
    * @Title: getReportPath 
    * @Description: get report path 
    * @param @return
    * @return String 
    * @throws
     */
    public static String getPath(String subPath)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(RESTConst.WORK).append(File.separatorChar);

        if (StringUtils.isNotEmpty(subPath))
        {
            sb.append(subPath).append(File.separatorChar);
        }

        return sb.toString();
    }
    
    /**
    * 
    * @Title: close 
    * @Description: Close writer 
    * @param @param w 
    * @return void
    * @throws
     */
    public static void close(XMLWriter xw)
    {
        if (null == xw)
        {
            return;
        }
        try
        {
            xw.close();
            xw = null;
        }
        catch(IOException e)
        {
            log.error("Failed to close writer.", e);
        }
    }
    
    /**
    * 
    * @Title: dup 
    * @Description: Get duplicate string by specified number and chars 
    * @param @param num
    * @param @return 
    * @return String
    * @throws
     */
    public static String dup(int num, String chars)
    {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < num; i++)
        {
            sb.append(chars);
        }

        return sb.toString();
    }
    
    /**
    *
    * @Title: jsonTree 
    * @Description: To generate a JSON tree 
    * @param @param e
    * @param @param layer
    * @param @param sb 
    * @return layer
    * @throws
     */
    public static void jsonTree(JsonElement e, int layer, StringBuilder sb)
    {
        if (e.isJsonNull())
        {
            return;
        }

        if (e.isJsonPrimitive())
        {
            return;
        }

        if (e.isJsonArray())
        {
            JsonArray ja = e.getAsJsonArray();
            if (ja.size() > 0)
            {
                jsonTree(ja.get(0), layer, sb);
            }
            return;
        }

        if (e.isJsonObject())
        {
            String line = RESTConst.LINE;
            String type = RESTConst.UNKNOWN;
            String spaces = "    ";
            String vertLine = "│   ";
            
            String indent = dup(layer, spaces);

            layer++;
            if (layer <= 0)
            {
                line = "   ";
            }

            Set<Entry<String, JsonElement>> es = e.getAsJsonObject().entrySet();
            for (Entry<String, JsonElement> en : es)
            {
                indent = dup(layer, spaces);
                if (layer >= 2)
                {
                    indent = dup(1, spaces) + dup(layer - 1, vertLine);
                }

                sb.append(indent).append(line).append(en.getKey()).append(" [");
                
                if (en.getValue().isJsonArray())
                {
                    type = Array.class.getSimpleName();
                }
                else if (en.getValue().isJsonObject())
                {
                    type = Object.class.getSimpleName();
                }
                else if (en.getValue().isJsonPrimitive())
                {
                    JsonPrimitive jp = en.getValue().getAsJsonPrimitive();
                    if (jp.isBoolean())
                    {
                        type = Boolean.class.getSimpleName();
                    }
                    else if (jp.isNumber())
                    {
                        type = Number.class.getSimpleName();
                    }
                    else if (jp.isString())
                    {
                        type = String.class.getSimpleName();
                    }
                }
                else if (en.getValue().isJsonNull())
                {
                    type = null + "";
                }

                sb.append(type.toLowerCase()).append("]").append(lines(1));
                jsonTree(en.getValue(), layer, sb);
            }
        }
    }
    
    /**
    * 
    * @Title: xmlTree 
    * @Description: To generate a XML tree 
    * @param @param e
    * @param @param layer
    * @param @param sb 
    * @return void
    * @throws
     */
    public static void xmlTree(Element e, int layer, StringBuilder sb)
    {
        if (e.nodeCount() <= 0)
        {
            return;
        }

        String spaces = "    ";
        String vertLine = "│   ";
        String line = RESTConst.LINE;
        String type = RESTConst.UNKNOWN;
        String indent = dup(layer, spaces);

        layer++;
        if (layer <= 0)
        {
            line = "   ";
        }

        @SuppressWarnings("unchecked")
        List<Element> es = e.elements();
        for (Element ce : es)
        {
            indent = dup(layer, spaces);
            if (layer >= 2)
            {
                indent = dup(1, spaces) + dup(layer - 1, vertLine);
            }

            if (!ce.elements().isEmpty() || ce.attributeCount() > 0)
            {
                type = Object.class.getSimpleName();
            }
            else if (StringUtils.isNotEmpty(ce.getText()) && StringUtils.isNumeric(ce.getStringValue()))
            {
                type = Number.class.getSimpleName();
            }
            else
            {
                type = String.class.getSimpleName();
            }

            /* Element */
            sb.append(indent).append(line)
              .append(ce.getName()).append(" [")
              .append(type.toLowerCase())
              .append("]").append(lines(1));

            /* Attributes */
            if (ce.attributeCount() > 0)
            {
                indent = dup(layer + 1, spaces);
                if (layer + 1 >= 2)
                {
                    indent = dup(1, spaces) + dup(layer, vertLine);
                }

                @SuppressWarnings("unchecked")
                List<Attribute> as = ce.attributes();
                for (Attribute a : as)
                {
                    if (StringUtils.isNotEmpty(ce.getText()) && StringUtils.isNumeric(a.getValue()))
                    {
                        type = Number.class.getSimpleName();
                    }
                    else
                    {
                        type = String.class.getSimpleName();
                    }
                    
                    sb.append(indent).append(RESTConst.LINE)
                      .append(a.getName()).append(" [")
                      .append(type.toLowerCase())
                      .append("]").append(lines(1));
                }
            }

            xmlTree(ce, layer, sb);
        }
    }

    /**
    * 
    * @Title: toModel 
    * @Description: Change to JSON model 
    * @param @param json
    * @param @return 
    * @return String
    * @throws
     */
    public static String toModel(String txt)
    {
        if (StringUtils.isEmpty(txt))
        {
            return StringUtils.EMPTY;
        }

        if (isHtml(txt))
        {
            return txt;
        }

        int layer = -1;
        StringBuilder sb = new StringBuilder();

        /* JSON */
        if (isJson(txt))
        {
            JsonParser p = new JsonParser();
            JsonElement e = p.parse(txt);

            jsonTree(e, layer, sb);

            return sb.toString();
        }

        /* XML */
        if (isXml(txt))
        {
            try
            {
                txt = StringUtils.replaceOnce(txt, "?>", "?><root>") + "</root>";
                Document doc = DocumentHelper.parseText(txt);
                xmlTree(doc.getRootElement(), layer, sb);
                return sb.toString();
            }
            catch(Exception e)
            {
                log.error("Bad xml format: " + lines(1) + txt);
            }
        }

        return sb.toString();
    }
    
    /**
    * 
    * @Title      : loadHist 
    * @Description: Load history 
    * @Param      : @param path if not specified, will use default. 
    * @Param      : @return 
    * @Return     : HttpHists
    * @Throws     :
     */
    public static HttpHists loadHist(String path)
    {
        File fhist = null;
        if (StringUtils.isNotEmpty(path))
        {
            fhist = new File(path);
            if (!fhist.exists())
            {
                System.out.println("The historical file " + path + " does not exist, will use default " + RESTConst.HTTP_HIST_JSON);
                fhist = new File(RESTConst.HTTP_HIST_JSON);
            }
        }
        else
        {
            fhist = new File(RESTConst.HTTP_HIST_JSON);
        }

        if (!fhist.exists())
        {
            System.out.println("The historical file " + path + " does not exist.");
            return null;
        }

        HttpHists hists = RESTUtil.toOject(fhist, HttpHists.class);
        if (null == hists || CollectionUtils.isEmpty(hists.getHists()))
        {
            System.out.println("No historical cases.");
        }
        return hists;
    }
    
    /**
    * 
    * @Title      : printUsage 
    * @Description: print usage
    * @Param      :  
    * @Return     : void
    * @Throws     :
     */
    public static void printUsage()
    {
        try
        {
            InputStream is = RESTUtil.getInputStream(RESTConst.WISDOM_TOOL_USAGE);
            String jsTxt = IOUtils.toString(is, Charsets.UTF_8.getCname());
            RESTUtil.close(is);
            System.out.println(jsTxt);
        }
        catch(Exception e)
        {
            log.error("Failed to read help file.", e);
        }
    }
    
    /**
    * 
    * @Title      : closeSplashScreen 
    * @Description: close splash screen 
    * @Param      :  
    * @Return     : void
    * @Throws     :
     */
    public static void closeSplashScreen()
    {
        SplashScreen ss = SplashScreen.getSplashScreen();
        if (null == ss)
        {
            return;
        }
        try
        {
            ss.close();
        }
        catch(Exception e)
        {
            // Ignore this exception
        }
    }
    
    /**
    * 
    * @Title      : excludeNode 
    * @Description: Exclude JSON nodes 
    * @Param      : @param e
    * @Param      : @param path
    * @Param      : @param exclNodes, nodes to be excluded 
    * @Return     : void
    * @Throws     :
     */
    private static void jsonTree(JsonElement e, String path, List<String> exclNodes)
    {
        if (e.isJsonNull())
        {
            return;
        }

        if (e.isJsonPrimitive())
        {
            return;
        }

        if (e.isJsonArray())
        {
            JsonArray ja = e.getAsJsonArray();
            if (null != ja)
            {
                for (JsonElement ae : ja)
                {
                    jsonTree(ae, path, exclNodes);
                }
            }
            return;
        }

        if (e.isJsonObject())
        {
            Map<String, JsonElement> tm = new LinkedHashMap<String, JsonElement>();
            for (Entry<String, JsonElement> en : e.getAsJsonObject().entrySet())
            {
                tm.put(en.getKey(), en.getValue());
            }

            for (Entry<String, JsonElement> en : tm.entrySet())
            {
                String nodeKey = path + "|" + en.getKey();
                if (CollectionUtils.isNotEmpty(exclNodes) && exclNodes.contains(nodeKey))
                {
                    e.getAsJsonObject().remove(en.getKey());
                    continue;
                }
                jsonTree(en.getValue(), nodeKey, exclNodes);
            }
        }
    }
    
    /**
    * 
    * @Title      : diff 
    * @Description: Differ JSON nodes 
    * @Param      : @param json1
    * @Param      : @param json2
    * @Param      : @param excludedNodes
    * @Param      : @return 
    * @Return     : boolean
    * @Throws     :
     */
    public static boolean diff(String json1, String json2, List<String> exclNodes)
    {
        if (CollectionUtils.isEmpty(exclNodes))
        {
            return json1.equals(json2);
        }

        JsonParser p = new JsonParser();
        JsonElement e1 = p.parse(json1);
        JsonElement e2 = p.parse(json2);

        jsonTree(e1, "", exclNodes);
        jsonTree(e2, "", exclNodes);

        return e1.equals(e2);
    }
}
