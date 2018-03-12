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

$(generate);

function generate()
{
    var $APIDOC = $("#api_doc");
    var ID = 0;
    var doctxt = '';
    
    /* API Description */
    $APIDOC.append("<div class=\"api-description\"><h1>" + APIDATA.api_description.title + "</h1><p>" + APIDATA.api_description.summary + "</p></div>");

    /* API List */
    $.each(APIDATA.api_list, function(i, api) 
    {
        ID = i + 1;
        
        /* API ITEM */
        doctxt += "<div class=\"api-item api-item-" + api["api_summary"].method + "\">";
        
        /* API SUMMARY */
        doctxt += "<div class=\"api-summary\" data-target=\"#api-detail-" + api["api_summary"].method + "-" + ID + "\" data-toggle=\"collapse\">";
        doctxt += "<span class=\"api-method api-method-" + api["api_summary"].method + "\">" + api["api_summary"].method + "</span>";
        doctxt += "<div class=\"api-path\">" + api["api_summary"].path + "</div>";
        doctxt += "<span class=\"api-title\">" + api["api_summary"].title + "</span></div>";
        
        /* API DETAIL */
        doctxt += "<div id=\"api-detail-" + api["api_summary"].method + "-" + ID + "\" class=\"api-detail-" + api["api_summary"].method + " collapse\">";
        
        /* API REQUEST */
        doctxt += "<div class=\"api-request\"><div class=\"api-tab-header\"><h4>Request</h4></div><div class=\"api-tab\"><table><colgroup><col width=\"20%\"/><col width=\"80%\"/></colgroup><thead><tr><th>Name</th><th>Description</th></tr></thead><tbody><tr><td>Header</td><td><pre>";
        doctxt += api["api_request"].header + "</pre></td></tr><tr><td>Body</td><td><ul class=\"tab\"><li id=\"model-li-req-" + ID + "\" class=\"active\" onclick=\"display('model', 'req-" + ID + "')\"><a>Model</a></li>";
        doctxt += "<li id=\"example-li-req-" + ID + "\" onclick=\"display('example', 'req-" + ID + "')\"><a>Example</a></li></ul><pre id=\"model-pre-req-" + ID + "\">" + api["api_request"].model + "</pre><pre id=\"example-pre-req-" + ID + "\" class=\"example\">" + api["api_request"].example + "</pre></td></tr></tbody></table></div></div>";

        /* API RESPONSE */
        doctxt += "<div class=\"api-response\"><div class=\"api-tab-header\"><h4>Response</h4></div><div class=\"api-tab\"><table><colgroup><col width=\"20%\"/><col width=\"80%\"/></colgroup><thead><tr><th>Status</th><th>Description</th></tr></thead><tbody>";
        
        $.each(api["api_response"], function(j, rep){
            doctxt += "<tr><td>" + rep["status"] + "</td><td><pre>" + rep["message"] + "</pre><ul class=\"tab\"><li id=\"model-li-rep-" + ID + j + "-" + rep["code"] + "\" class=\"active\" onclick=\"display('model', 'rep-" + ID + j + "-" + rep["code"] + "')\"><a>Model</a></li>";
            doctxt += "<li id=\"example-li-rep-" + ID + j + "-" + rep["code"] + "\" onclick=\"display('example', 'rep-" + ID + j + "-" + rep["code"] + "')\"><a>Example</a></li></ul><pre id=\"model-pre-rep-" + ID + j + "-" + rep["code"] + "\">" + rep["model"] + "</pre><pre id=\"example-pre-rep-" + ID + j + "-" + rep["code"] + "\" class=\"example\">" + rep["example"] + "</pre></td></tr>";
        });
        
        doctxt += "</tbody></table></div></div></div></div>";
    });
    
    $APIDOC.append("<div id=\"api-list\" class=\"api-list\">" + doctxt + "</div>");
}

function display(item, id)
{
    if(item == 'model')
    {
        $("#model-pre-" + id).css('display', 'block'); 
        $("#example-pre-" + id).css('display', 'none');
        
        $("#model-li-" + id).attr("class", "active");
        $("#example-li-" + id).attr("class", "");
    }

    if(item == 'example')
    {
        $("#model-pre-" + id).css('display', 'none'); 
        $("#example-pre-" + id).css('display', 'block');
        
        $("#model-li-" + id).attr("class", "");
        $("#example-li-" + id).attr("class", "active");
    }

}