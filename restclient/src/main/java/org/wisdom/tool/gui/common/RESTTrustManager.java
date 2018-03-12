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
package org.wisdom.tool.gui.common;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/** 
* @ClassName: RESTTrustManager 
* @Description: REST trust manager 
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@outlook.com 
* @Date: Jul 26, 2017 5:32:58 PM 
* @Version: Wisdom RESTClient V1.2 
*/
public class RESTTrustManager implements TrustManager, X509TrustManager
{
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
    {
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
    {
    }

    public X509Certificate[] getAcceptedIssuers()
    {
        return null;
    }
}
