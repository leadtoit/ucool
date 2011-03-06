<%@ page contentType="text/html;charset=gbk" language="java" %>
<%@ page import="common.ConfigCenter" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="gbk"/>
    <title>ucool 配置页</title>
    <script type="text/javascript" src="http://a.tbcdn.cn/s/kissy/1.1.5/kissy-min.js"></script>
    <link rel="stylesheet" href="http://a.tbcdn.cn/s/kissy/1.1.5/cssbase/base-min.css" />
    <style type="text/css">
        body{
            background-color:#f3f1e4;
        }
        a, a:hover {
            text-decoration:none;
        }
        #page {
            margin: 0 auto;
        }
        #header {
            background-color:#edead8;
            height:86px;
            border-bottom:solid 1px #d0cbaf;
        }
        #content, #footer {
            width: 950px;
        }
        #header .top{
            width: 950px;
            margin:0 auto;
            position:relative;
        }
        #header .version {
            position:absolute;
            right: 0;
            bottom:0;
            color:#996;
            font-size:14px;
        }
        #header h1{
            color: #54a700;
            font-size:42px;
            line-height:86px;
            font-weight:normal;
        }
        #content .box {
            margin-top:10px;
        }
        #content .box .hd {
            color:#54a700;
            font-size:16px;
            border-bottom:solid 1px #dcdcdc;
            position:relative;
        }
        #content .box .hd h3 {
            position:relative;
            top:7px;
            background-color:#f3f1e4;
            display:inline;
            padding-right: 5px;
        }
        #content .box table {
            width:760px;
            font-size:14px;
            margin-top: 20px;
        }
        #content .box table th {
            text-align:right;
            width:50%;
            height:32px;
            color:#666;
            line-height:32px;
            font-weight:normal;
        }
        #content .box table td {
            text-align:left;
            color:#999;
            height:32px;
            line-height:32px;
            padding-left:10px;
        }
        #content .switch table td {
            height:37px;
            line-height:37px;
        }
        #content .switch td {
            font-size:12px;
        }
        #content .switch .op{
            width:70px;
        }
        #content .switch .note {
            padding-left:0;
        }
        #content .box table td input {
            width:50px;
            height:25px;
        }
        .switch-open, .switch-close {
            background:url(http://img02.taobaocdn.com/tps/i2/T1zcFQXgxwXXXXXXXX-68-73.png) #f3f1e4 no-repeat 0 0;
            cursor:pointer;
            height:34px;
            width:53px;
            vertical-align:middle;
            display:inline-block;
        }
        .switch-open {
            background-position:-7px -3px;
        }
        .switch-close {
            background-position:-7px -38px;
        }
        #footer, #footer a {
            text-align:center;
            margin-top:10px;
            height:50px;
            line-height:50px;
            color:#996;
            background-color:#edead8;
        }
        #footer .help {
            float:left;
            margin-left: 25px;
        }
        #footer .author {
            float:right;
        }
        #footer .author li{
            float:left;
            margin-right: 30px;
        }
    </style>
</head>
<body>
<%
    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
    ConfigCenter configCenter = (ConfigCenter) wac.getBean("configCenter");
%>
<div id="page">
    <div id="header">
        <div class="top">
            <h1>ucool config page</h1>
            <a class="version" href="http://code.google.com/p/ucool">版本：0.1</a>
        </div>
    </div>
    <div id="content">
        <div class="box switch">
            <div class="hd"><h3>SWITCH</h3></div>
            <div class="bd">
                <table>
                    <tr>
                        <th>DEBUG MODE：</th>
                        <td class="op"><a class="<%=configCenter.getStateStringStyle(configCenter.getUcoolAssetsDebug())%>" id="assetsdebugswitch"></a></td>
                        <td class="note">切换所有js和css都显示源码</td>
                    </tr>
                    <tr>
                        <th>BIND PRE-RELEASE ENV：</th>
                        <td class="op"><a class="<%=configCenter.getStateStyle(configCenter.isPrepub())%>" id="bindPrepub"></a></td>
                        <td class="note">切换到预发环境</td>
                    </tr>
                    <tr>
                        <th>USE ASSETS：</th>
                        <td class="op"><a class="<%=configCenter.getStateStyle(configCenter.isEnableAssets())%>" id="enableAssets"></a></td>
                        <td class="note">使用服务器上的assets目录</td>
                    </tr>
                    <%--<tr>--%>
                        <%--<th>RELEASE CACHE：</th>--%>
                        <%--<td class="op"><input type="button" value="CLEAR" id="cleanOnlineCache"/></td>--%>
                        <%--<td class="note"></td>--%>
                    <%--</tr>--%>
                </table>
            </div>
        </div>
        <div class="box">
            <div class="hd"><h3>PROFILE</h3></div>
            <div class="bd">
                <table>
                    <tr>
                        <th>DAILY DOMAIN：</th>
                        <td><%=configCenter.getUcoolDailyDomain()%></td>
                    </tr>
                    <tr>
                        <th>RELEASE DOMAIN：</th>
                        <td><%=configCenter.getUcoolOnlineDomain()%></td>
                    </tr>
                    <tr>
                        <th>DAILY IP：</th>
                        <td><%=configCenter.getUcoolDailyIp()%></td>
                    </tr>
                    <tr>
                        <th>RELEASE IP：</th>
                        <td><%=configCenter.getUcoolOnlineIp()%></td>
                    </tr>
                    <tr>
                        <th>PRE-RELEASE IP：</th>
                        <td><%=configCenter.getUcoolPrepubIp()%></td>
                    </tr>
                    <tr class="separator"><td colspan="2"></td></tr>
                    <tr>
                        <th>COMBO SPLITTER：</th>
                        <td><%=configCenter.getUcoolComboDecollator()%></td>
                    </tr>
                    <tr class="separator"><td colspan="2"></td></tr>
                    <%--<tr>--%>
                        <%--<th>AUTO FLUSH CACHE：</th>--%>
                        <%--<td><%=configCenter.getUcoolCacheAutoClean()%></td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                        <%--<th>CACHE FLUSH PERIOD：</th>--%>
                        <%--<td><em><%=configCenter.getUcoolCacheCleanPeriod()%></em>h</td>--%>
                    <%--</tr>--%>
                    <tr class="separator"><td colspan="2"></td></tr>
                    <tr>
                        <th>ASSETS DIR：</th>
                        <td><%=configCenter.getUcoolAssetsRoot()%></td>
                    </tr>
                    <%--<tr>--%>
                        <%--<th>RELEASE CACHE DIR：</th>--%>
                        <%--<td><%=configCenter.getUcoolCacheRoot()%></td>--%>
                    <%--</tr>--%>
                </table>
            </div>
        </div>
    </div>
    <div id="footer">
        <span class="help">(?) help</span>
        <ul class="author">
            <li><a href="mailto:zhangting@taobao.com">开发：张挺(zhangting@taobao.com)</a></li>
            <li><a href="mailto:wuxuan@taobao.com">设计：悟玄(wuxuan@taobao.com)</a></li>
        </ul>
    </div>
</div>
<script type="text/javascript">
    KISSY.ready(function(S) {
        var Event = S.Event;
        var DOM = S.DOM;
        S.app('UCOOL');
        S.namespace('Pz');

        UCOOL.Pz = function() {

            var _change = function(pid, success, curState) {
                if (success === 'ok') {
                    _switchChange('#' + pid);
                }
            };

            var _doOnce = function(pid, success, time) {
                var el = S.get('#' + pid);
                var curParent = DOM.parent(el);
                DOM.next(curParent).innerHTML = '在' + time + (success === 'ok' ? '清理成功' : '清理失败');
            };

            var _switchChange = function(el) {
                DOM.toggleClass(el, 'switch-close');
                DOM.toggleClass(el, 'switch-open');
            };

            return {
                init:function() {
                    Event.on('#assetsdebugswitch', 'click', function(e) {
                        var scriptNode = S.getScript("pzbg.jsp?" + "pid=assetsdebugswitch&callback=UCOOL.Pz.change&t=" + new Date(), {
                            success:function(){
                                DOM.remove(scriptNode);
                            }
                        });
                    });
                    Event.on('#cleanOnlineCache', 'click', function(e) {
                        var scriptNode = S.getScript("pzbg.jsp?" + "pid=cleanOnlineCache&callback=UCOOL.Pz.doOnce&t=" + new Date(), {
                            success:function(){
                                DOM.remove(scriptNode);
                            }
                        });
                    });
                    Event.on('#bindPrepub', 'click', function(e) {
                        var scriptNode = S.getScript("pzbg.jsp?" + "pid=bindPrepub&callback=UCOOL.Pz.change&t=" + new Date(), {
                            success:function(){
                                DOM.remove(scriptNode);
                            }
                        });
                    });
                    Event.on('#enableAssets', 'click', function(e) {
                        var scriptNode = S.getScript("pzbg.jsp?" + "pid=enableAssets&callback=UCOOL.Pz.change&t=" + new Date(), {
                            success:function(){
                                DOM.remove(scriptNode);
                            }
                        });
                    });
                },

                change:function(pid, success, curState) {
                    _change(pid, success, curState);
                },
                doOnce:function(pid, success, time) {
                    _doOnce(pid, success, time);
                }
            }
        }();

        UCOOL.Pz.init();
    });
</script>
</body>
</html>