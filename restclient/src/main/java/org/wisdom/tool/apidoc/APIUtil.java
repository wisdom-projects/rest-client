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
package org.wisdom.tool.apidoc;

import java.awt.Desktop;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.codec.Charsets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.ReasonPhraseCatalog;
import org.apache.http.impl.EnglishReasonPhraseCatalog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wisdom.tool.cache.RESTCache;
import org.wisdom.tool.constant.RESTConst;
import org.wisdom.tool.gui.util.UIUtil;
import org.wisdom.tool.model.APIDoc;
import org.wisdom.tool.model.APIItem;
import org.wisdom.tool.model.APIReq;
import org.wisdom.tool.model.APIRsp;
import org.wisdom.tool.model.APISum;
import org.wisdom.tool.model.HttpHist;
import org.wisdom.tool.model.HttpHists;
import org.wisdom.tool.model.HttpMethod;
import org.wisdom.tool.util.RESTUtil;

/** 
* @ClassName: APIDocUtil 
* @Description: API document utility 
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@outlook.com 
* @Date: 2017-7-17 PM 1:11:29 
* @Version: Wisdom RESTClient V1.2 
*/
public final class APIUtil
{
    private static Logger log = LogManager.getLogger(APIUtil.class);
    
    /**
    * 
    * @Title      : getAPIDoc 
    * @Description: Get API document 
    * @Param      : @param hists
    * @Param      : @return 
    * @Return     : APIDoc
    * @Throws     :
     */
    public static APIDoc getAPIDoc(Collection<HttpHist> hists)
    {
        APIDoc doc = null;
        InputStream is = RESTUtil.getInputStream(RESTConst.APIDOC_JSON);
        doc = RESTUtil.toOject(is, APIDoc.class);
        RESTUtil.close(is);

        if (null == doc || CollectionUtils.isEmpty(doc.getApiLst()))
        {
            return doc;
        }

        if (CollectionUtils.isEmpty(hists))
        {
            return doc;
        }

        List<APIItem> apiLst = doc.getApiLst();
        apiLst.clear();

        Map<String, List<APIRsp>> rsps = new LinkedHashMap<String, List<APIRsp>>();
        for (HttpHist hist : hists)
        {
            APISum sumry = new APISum(hist.getReq());
            List<APIRsp> rspLst = rsps.get(sumry.apiKey());
            if (null == rspLst)
            {
                rspLst = new ArrayList<APIRsp>();
                rsps.put(sumry.apiKey(), rspLst);

                APIReq req = new APIReq(hist.getReq());
                APIItem item = new APIItem(sumry, req, rspLst);
                apiLst.add(item);
            }

            rspLst.add(new APIRsp(hist.getRsp()));
        }

        for (APIItem item : apiLst)
        {
            delDup(item.getRepLst());
            sort(item.getRepLst());
        }

        return doc;
    }
    
    /**
    * 
    * @Title: getAPIDoc 
    * @Description: Get API Document Object 
    * @param @return
    * @return APIDoc 
    * @throws
     */
    public static synchronized APIDoc getAPIDoc()
    {
        Collection<HttpHist> hists = RESTCache.getHists().values();
        APIDoc doc = getAPIDoc(hists);
        return doc;
    }
    
    /**
    * 
    * @Title: apiDoc 
    * @Description: Generate API document 
    * @param @param doc 
    * @return void
    * @throws
     */
    public static synchronized void apiDoc(APIDoc doc)
    {
        try
        {
            // Update JS
            InputStream is = RESTUtil.getInputStream(RESTConst.APIDOC_DATA_JS);
            String jsTxt = IOUtils.toString(is, Charsets.UTF_8);
            String jsonDoc = RESTUtil.tojsonText(doc);
            jsTxt = StringUtils.replace(jsTxt, RESTConst.LABEL_APIDOC_DATA, jsonDoc);
            FileUtils.write(new File(RESTUtil.replacePath(RESTConst.APIDOC_DATA_JS)), jsTxt, Charsets.UTF_8);
            RESTUtil.close(is);
            
            // Copy JS
            is = RESTUtil.getInputStream(RESTConst.APIDOC_JS);
            FileUtils.copyInputStreamToFile(is, new File(RESTUtil.replacePath(RESTConst.APIDOC_JS)));
            RESTUtil.close(is);
            
            is = RESTUtil.getInputStream(RESTConst.APIDOC_BTSTRAP_JS);
            FileUtils.copyInputStreamToFile(is, new File(RESTUtil.replacePath(RESTConst.APIDOC_BTSTRAP_JS)));
            RESTUtil.close(is);
            
            is = RESTUtil.getInputStream(RESTConst.REPORT_JQUERY);
            FileUtils.copyInputStreamToFile(is, new File(RESTUtil.replacePath(RESTConst.APIDOC_JQUERY)));
            RESTUtil.close(is);
            
            // Copy HTML
            is = RESTUtil.getInputStream(RESTConst.APIDOC_HTML);
            FileUtils.copyInputStreamToFile(is, new File(RESTUtil.replacePath(RESTConst.APIDOC_HTML)));
            RESTUtil.close(is);
            
            // Copy CSS
            is = RESTUtil.getInputStream(RESTConst.APIDOC_CSS);
            FileUtils.copyInputStreamToFile(is, new File(RESTUtil.replacePath(RESTConst.APIDOC_CSS)));
            RESTUtil.close(is);
            
            is = RESTUtil.getInputStream(RESTConst.APIDOC_BTSTRAP_CSS);
            FileUtils.copyInputStreamToFile(is, new File(RESTUtil.replacePath(RESTConst.APIDOC_BTSTRAP_CSS)));
            RESTUtil.close(is);
            
            // Copy LOGO
            is = RESTUtil.getInputStream(RESTConst.LOGO);
            String apath = RESTUtil.getPath(RESTConst.APIDOC);
            String logoPath = StringUtils.replaceOnce(RESTConst.LOGO, RESTConst.WISDOM_TOOL, apath);
            FileUtils.copyInputStreamToFile(is, new File(logoPath));
            RESTUtil.close(is);

            try
            {
                // Open API document
                Desktop.getDesktop().open(new File(RESTUtil.replacePath(RESTConst.APIDOC_HTML)));
            }
            catch(Exception e)
            {
                UIUtil.showMessage(RESTConst.MSG_APIDOC, RESTConst.API_DOCUMENT);
            }

        }
        catch(Throwable e)
        {
            log.error("Failed to generate API document.", e);
        }
    }
    
    /**
    * 
    * @Title: getShortPath 
    * @Description: Get URL short path 
    * @param @param url
    * @param @return 
    * @return String
    * @throws
     */
    public static String getShortPath(String url)
    {
        if (StringUtils.isEmpty(url))
        {
            return StringUtils.EMPTY;
        }

        url = StringUtils.removeStartIgnoreCase(url, RESTConst.HTTP_HEAD);
        url = StringUtils.removeStartIgnoreCase(url, RESTConst.HTTPS_HEAD);
        url = "/" + StringUtils.substringAfter(url, "/");
        String path = StringUtils.substringBefore(url, "?");
        return path;
    }
    
    /**
    * 
    * @Title: getReqPath 
    * @Description: Update URL to add parameters 
    * @param @param url
    * @param @return 
    * @return String
    * @throws
     */
    public static String getReqPath(String url)
    {
        if (StringUtils.isEmpty(url))
        {
            return StringUtils.EMPTY;
        }

        StringBuilder sbUrl = new StringBuilder();
        String[] subUrls = StringUtils.split(getShortPath(url), '/');
        for (String subUrl : subUrls)
        {
            if (StringUtils.isNotEmpty(subUrl) && StringUtils.isNumeric(subUrl))
            {
                sbUrl.append('/').append(RESTConst.PATH_PARAM);
                continue;
            }
            sbUrl.append('/').append(subUrl);
        }

        // Parameters
        String paramStr = StringUtils.substringAfter(url, "?");
        if (StringUtils.isEmpty(paramStr))
        {
            return sbUrl.toString();
        }

        sbUrl.append('?');
        String attrName = StringUtils.EMPTY;
        String[] params = StringUtils.split(paramStr, '&');
        for (String param : params)
        {
            attrName = StringUtils.substringBefore(param, "=");
            sbUrl.append(attrName)
                 .append('=').append(StringUtils.replace(RESTConst.PATH_PARAM, "id", attrName))
                 .append('&');
        }

        return StringUtils.removeEnd(sbUrl.toString(), "&");
    }
    
    /**
    * 
    * @Title: isAlpha 
    * @Description: Check if the string is made up of letters or underscores 
    * @param @param str
    * @param @return 
    * @return boolean
    * @throws
     */
    public static boolean isAlpha(String str)
    {
        if (null == str)
        {
            return false;
        }

        String rmStr = StringUtils.remove(str, "_");
        if (StringUtils.isAlpha(str) || StringUtils.isAlpha(rmStr))
        {
            return true;
        }

        return false;
    }
    
    /**
    * 
    * @Title: getTitle 
    * @Description: Get title name 
    * @param @param mthd
    * @param @param url
    * @param @return 
    * @return String
    * @throws
     */
    public static String getTitle(HttpMethod mthd, String url)
    {
        String title = StringUtils.EMPTY;
        if (null == mthd || StringUtils.isEmpty(url))
        {
            return title;
        }

        String objName = StringUtils.EMPTY;
        String[] subUrls = StringUtils.split(url, "/");
        int len = subUrls.length;
        for (int i = len - 1; i >= 0; i--)
        {
            if (isAlpha(subUrls[i]))
            {
                objName = subUrls[i];
                break;
            }
        }

        String optName = StringUtils.EMPTY;
        switch (mthd)
        {
            case GET:
                optName = "Query";
                break;
            case POST:
                optName = "Create";
                break;
            case PUT:
                optName = "Update";
                break;
            case DELETE:
                optName = "Delete";
                break;
    
            default:
                optName = StringUtils.EMPTY;
                break;
        }

        title = optName + " " + objName;
        return title;
    }
    
    /**
    * 
    * @Title: headerStr 
    * @Description: header map to string 
    * @param @param hdr
    * @param @return 
    * @return String
    * @throws
     */
    public static String headerStr(Map<String, String> hdr)
    {
        if (MapUtils.isEmpty(hdr))
        {
            return StringUtils.EMPTY;
        }

        StringBuilder sb = new StringBuilder();
        Set<Entry<String, String>> es = hdr.entrySet();
        for (Entry<String, String> e : es)
        {
            sb.append(e.toString().replaceFirst("=", " : "))
              .append(RESTUtil.lines(1));
        }
        return sb.toString();
    }
    
    /**
    * 
    * @Title: getReason 
    * @Description: Get HTTP status reason 
    * @param @param code
    * @param @return 
    * @return String
    * @throws
     */
    public static String getReason(int code)
    {
        ReasonPhraseCatalog catalog = EnglishReasonPhraseCatalog.INSTANCE;
        String reason = StringUtils.EMPTY;

        try
        {
            reason = catalog.getReason(code, Locale.getDefault());
            if (StringUtils.isEmpty(reason))
            {
                return StringUtils.EMPTY;
            }
        }
        catch(Exception e)
        {
            log.error(e.getMessage(), e);
        }

        return reason;
    }
    
    /**
    * 
    * @Title: sort 
    * @Description: Sort HTTP response 
    * @param @param rspLst 
    * @return void
    * @throws
     */
    public static void sort(List<APIRsp> rspLst)
    {
        Collections.sort(rspLst, new Comparator<APIRsp>()
        {
            public int compare(APIRsp o1, APIRsp o2)
            {
                return o1.getCode().compareTo(o2.getCode());
            }
        });
    }
    
    /**
    * 
    * @Title: delDup 
    * @Description: Remove duplicate elements 
    * @param @param rspLst 
    * @return void
    * @throws
     */
    public static void delDup(List<APIRsp> rspLst)
    {
        if (CollectionUtils.isEmpty(rspLst))
        {
            return;
        }

        Map<String, APIRsp> rsps = new LinkedHashMap<String, APIRsp>();
        for (APIRsp rsp : rspLst)
        {
            rsps.put(rsp.getCode() + rsp.getExample(), rsp);
        }

        rspLst.clear();
        rspLst.addAll(rsps.values());
    }
    
    /**
    * 
    * @Title      : loadDoc 
    * @Description: Load API doc 
    * @Param      : @param path
    * @Param      : @return 
    * @Return     : APIDoc
    * @Throws     :
     */
    public static APIDoc loadDoc(String path)
    {
        APIDoc doc = null;
        HttpHists hists = RESTUtil.loadHist(path);
        if (null == hists)
        {
            doc = getAPIDoc(null);
            return doc;
        }

        doc = getAPIDoc(hists.getHists());
        return doc;
    }
}
