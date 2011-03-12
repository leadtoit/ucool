<%@ page contentType="text/html;charset=gbk" language="java" %>
<%@ page import="common.ConfigCenter" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="common.PersonConfig" %>
<%@ page import="biz.file.FileEditor" %>
<%@ page import="java.util.List" %>
<%@ page import="web.handler.impl.PersonConfigHandler" %>
<%@ page import="common.tools.DirSyncTools" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="gbk"/>
    <meta http-equiv="Expires" content="0">
    <meta http-equiv="kiben" content="no-cache">
    <title>ucool个人配置页</title>
    <script type="text/javascript" src="http://a.tbcdn.cn/s/kissy/1.1.6/kissy-min.js"></script>
    <link rel="stylesheet" href="http://a.tbcdn.cn/s/kissy/1.1.6/cssbase/base-min.css" />
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
            position:relative;
        }
        #header .top{
            width: 950px;
            margin:0 auto;
            position:relative;
        }
        #header .version {
            position:absolute;
            color:#996;
            font-size:14px;
            left: 350px;
            top: 50px;
        }
        #header .new {
            position:absolute;
            right:0;
            bottom:0;
            color: #999966;
        }
        #header .new i {
            width:16px;
            height:16px;
            background-color: #feab1b;
            color: #fff;
            font-style: normal;
            display: inline-block;
            text-align:center;
            font-weight:bold;
            margin-right: 5px;
            font-size: 11px;
            outline: none;
            -moz-border-radius:8px;
            -webkit-border-radius: 8px;
            border-radius: 8px;
        }
        #header h1, #header h1 a{
            color: #54a700;
            font-size:42px;
            line-height:86px;
            font-weight:normal;
        }
        #content {
            min-height:400px;
        }
        #content .box {
            margin-bottom:10px;
        }
        #content .box .hd {
            color:#54a700;
            font-size:16px;
            border-bottom:solid 1px #dcdcdc;
            position:relative;
            margin-bottom:15px;
        }
        #content .box .hd h3 {
            position:relative;
            top:7px;
            background-color:#f3f1e4;
            display:inline;
            padding-right: 5px;
        }
        #content .box .bd {
            padding-left:50px;
        }
        #content .box table {
            width:850px;
            margin-top: 20px;
        }
        #content .box table th {
            text-align:left;
            height:32px;
            color:#666;
            line-height:32px;
            font-weight:normal;
            width:30%;
            font-size: 14px;
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
        .hidden {
            display:none;
        }
        #dir select {
            border: 1px solid #000000;
            width:150px;
        }
        .lab {
            background-color: #0b0;
            color:#fff;
            display: inline-block;
            text-align: center;
            width: 25px;
            font-size: 10px;
            line-height: 12px;
            -moz-border-radius:1px;
            -webkit-border-radius:1px;
            border-radius:1px;
        }
        .howtouse {
            color: #f60;
            margin: auto 5px;
        }
    </style>
</head>
<body>
<%
    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
    ConfigCenter configCenter = (ConfigCenter) wac.getBean("configCenter");
    PersonConfigHandler personConfigHandler  = (PersonConfigHandler) wac.getBean("personConfigHandler");
    PersonConfig personConfig = personConfigHandler.doHandler(request);
    FileEditor fileEditor = (FileEditor) wac.getBean("fileEditor");
    DirSyncTools dirSyncTools = (DirSyncTools) wac.getBean("dirSyncTools");
%>
<div id="page">
    <div id="header">
        <div class="top">
            <h1><a href="http://wiki.ued.taobao.net/doku.php?id=user:zhangting:tools:ucool-pro:start">ucool config page</a></h1>
            <a class="version" href="http://code.google.com/p/ucool">ucool-pro version：0.3 beta</a>
            <a class="new" href="http://wiki.ued.taobao.net/doku.php?id=user:zhangting:tools:ucool-pro:history:start"><i>?</i>What's new？</a>
        </div>
    </div>
    <div id="content">
        <div class="box">
            <div class="hd"><h3>INFO</h3></div>
            <div class="bd">
                <table>
                    <tr>
                        <th>当前的机器名（唯一）：</th>
                        <td><%=request.getRemoteHost()%>
                        </td>
                    </tr>
                    <tr>
                        <th>请选择一个绑定目录：</th>
                        <td>
                            <div id="dir">
                                <select name="root-bind" id="root-bind" autocomplete="off">
                                    <%
                                        List<String> assetsSubDirs = fileEditor.getAssetsSubDirs();
                                        String curDirName = "";
                                        if (!personConfig.isNewUser()) {
                                            curDirName = personConfig.getUserDO().getName();
                                            if (dirSyncTools.sync(configCenter.getWebRoot() + personConfig.getUcoolAssetsRoot(), personConfig)) {
                                                personConfig.getUserDO().setName("");
                                                curDirName = "";
                                            }
                                        }
                                        String rootName = "", subName = "";
                                        if (!curDirName.isEmpty()) {
                                            String[] dirs = curDirName.split("/");
                                            if (dirs.length > 1) {
                                                rootName = dirs[0];
                                                subName = dirs[1];
                                            }
                                        }
                                        out.print("<option value='-1'>无绑定目录</option>");
                                        if (assetsSubDirs.size() > 0) {
                                            for (String assetsSubDir : assetsSubDirs) {
                                                if (assetsSubDir.equals(rootName)) {
                                                    out.print("<option selected='selected' value=" + assetsSubDir + ">");
                                                } else {
                                                    out.print("<option value=" + assetsSubDir + ">");
                                                }
                                                out.print(assetsSubDir);
                                                out.print("</option>");
                                            }
                                        }
                                    %>
                                </select>　/　
                                <select name="dir-bind" id="dir-bind" autocomplete="off">
                                    <%
                                        List<String> subDirs = fileEditor.getAssetsSubDirs(rootName);
                                        out.print("<option value='-1'>无绑定子目录</option>");
                                        if (subDirs.size() > 0) {
                                            for (String subDir : subDirs) {
                                                if (subDir.equals(subName)) {
                                                    out.print("<option selected='selected' value=" + subDir + ">");
                                                } else {
                                                    out.print("<option value=" + subDir + ">");
                                                }
                                                out.print(subDir);
                                                out.print("</option>");
                                            }
                                        }
                                    %>
                                </select>
                                <div id="message" style="display:none"><img
                                        src='http://img02.taobaocdn.com/tps/i2/T1JSdAXd0nXXXXXXXX-32-32.gif'/></div>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        <div id="J_BoxSwitch" class="box switch <%=personConfig.personConfigValid()?"":"hidden"%>">
            <div class="hd"><h3>SWITCH</h3></div>
            <div class="bd">
                <table>
                    <tr>
                        <th>DEBUG 模式：</th>
                        <td class="op"><a class="<%=configCenter.getStateStyle(personConfig.isUcoolAssetsDebug())%>" id="assetsdebugswitch"></a></td>
                        <td class="note">打开后切换所有js和css都显示源码</td>
                    </tr>
                    <tr>
                        <th>绑定预发环境：</th>
                        <td class="op"><a class="<%=configCenter.getStateStyle(personConfig.isPrepub())%>" id="bindPrepub"></a></td>
                        <td class="note">打开后切换到预发环境</td>
                    </tr>
                    <tr>
                        <th>使用 Assets 目录：</th>
                        <td class="op"><a class="<%=configCenter.getStateStyle(personConfig.isEnableAssets())%>" id="enableAssets"></a></td>
                        <td class="note">打开后启用服务器上的assets目录中的文件</td>
                    </tr>
                    <tr>
                        <th>启用本地combo：<sup class="lab">lab</sup></th>
                        <td class="op"><a class="<%=configCenter.getStateStyle(personConfig.isEnableLocalCombo())%>" id="enableLocalCombo"></a></td>
                        <td class="note">根据根目录的配置文件combo.properties可以将一个文件以combo的形式拆分<a href="http://wiki.ued.taobao.net/doku.php?id=user:zhangting:tools:ucool-pro:ucool-local-combo" class="howtouse">how to use?</a></td>
                    </tr>
                    <%--<tr>--%>
                        <%--<th>RELEASE CACHE：</th>--%>
                        <%--<td class="op"><input type="button" value="CLEAR" id="cleanOnlineCache"/></td>--%>
                        <%--<td class="note"></td>--%>
                    <%--</tr>--%>
                </table>
            </div>
        </div>
        <%--
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
                    <tr>
                        <th>ASSETS ROOT DIR：</th>
                        <td><%=configCenter.getUcoolAssetsRoot()%></td>
                    </tr>
                </table>
            </div>
        </div>--%>
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
        var Event = S.Event,
        DOM = S.DOM;
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

            var _bindDir = function (pid, success, data) {
                DOM.hide('#message');
                if (success === 'ok') {
                    //switch config
                    if(data !== 'cancel') {
                        DOM.show('#J_BoxSwitch');
                        _updateConfig(data);
                    }
                } else {
                    DOM.hide('#J_BoxSwitch');
                }
            };

            var _loadSub = function (pid, success, data) {
                DOM.hide('#message');
                if (success === 'ok') {
                    var selectEl = S.get('#dir-bind');
                    if(S.UA.ie) {
                        selectEl.add(new Option('无绑定子目录', -1));
                    } else {
                        selectEl.appendChild(new Option('无绑定子目录', -1));
                    }
                    if(data) {
                        var subDirs = data.split(';');
                        for (var i = 0; i < subDirs.length; i++) {
                            if (S.UA.ie) {
                                selectEl.add(new Option(subDirs[i], subDirs[i]));
                            } else {
                                selectEl.appendChild(new Option(subDirs[i], subDirs[i]));
                            }
                        }
                    }
                } else {
                    DOM.hide('#J_BoxSwitch');
                }
            };

            var _updateConfig = function(config) {
                S.get('#assetsdebugswitch').className='';
                S.get('#bindPrepub').className='';
                S.get('#enableAssets').className='';
                S.get('#enableLocalCombo').className='';
                if(ConfigParser.isEnableDebug(config)) {
                    DOM.addClass('#assetsdebugswitch', 'switch-open');
                } else {
                    DOM.addClass('#assetsdebugswitch', 'switch-close');
                }
                if(ConfigParser.isEnablePrepub(config)) {
                    DOM.addClass('#bindPrepub', 'switch-open');
                } else {
                    DOM.addClass('#bindPrepub', 'switch-close');
                }
                if(ConfigParser.isEnableAssets(config)) {
                    DOM.addClass('#enableAssets', 'switch-open');
                } else {
                    DOM.addClass('#enableAssets', 'switch-close');
                }
                if(ConfigParser.isEnableLocalCombo(config)) {
                    DOM.addClass('#enableLocalCombo', 'switch-open');
                } else {
                    DOM.addClass('#enableLocalCombo', 'switch-close');
                }
            };

            var ConfigParser = {
                isEnableDebug : function(config) {
                    return (config & 1) == 1;
                },
                isEnablePrepub : function(config) {
                    return (config & 2) == 2;
                },
                isEnableAssets : function(config) {
                    return (config & 4) == 4;
                },
                isEnableLocalCombo : function(config) {
                    return (config & 8) == 8;
                }
            };

            return {
                init:function() {
                    Event.on('#assetsdebugswitch', 'click', function(e) {
                        S.getScript("ppzbg.jsp?" + "pid=assetsdebugswitch&callback=UCOOL.Pz.change&t=" + new Date());
                    });
                    Event.on('#cleanOnlineCache', 'click', function(e) {
                        S.getScript("ppzbg.jsp?" + "pid=cleanOnlineCache&callback=UCOOL.Pz.doOnce&t=" + new Date());
                    });
                    Event.on('#bindPrepub', 'click', function(e) {
                        S.getScript("ppzbg.jsp?" + "pid=bindPrepub&callback=UCOOL.Pz.change&t=" + new Date());
                    });
                    Event.on('#enableAssets', 'click', function(e) {
                        S.getScript("ppzbg.jsp?" + "pid=enableAssets&callback=UCOOL.Pz.change&t=" + new Date());
                    });
                    Event.on('#enableLocalCombo', 'click', function(e) {
                        S.getScript("ppzbg.jsp?" + "pid=enableLocalCombo&callback=UCOOL.Pz.change&t=" + new Date());
                    });
                    Event.on('#root-bind', 'change', function(e) {
                        DOM.hide('#J_BoxSwitch');
                        DOM.show('#message');
                        S.get('#dir-bind').options.length = 0;
                        var selectRootEl = S.get('#root-bind');
                        var selectSubEl = S.get('#dir-bind');
                        if(selectRootEl.options[selectRootEl.selectedIndex].value == -1) {
                            S.getScript("ppzbg.jsp?" + "pid=bindDir&callback=UCOOL.Pz.bindDir&rootDir="+selectRootEl.options[selectRootEl.selectedIndex].value+"&subDir=-1&t=" + new Date());
                        } else {
                            S.getScript("ppzbg.jsp?" + "pid=loadSub&callback=UCOOL.Pz.loadSub&rootDir="+selectRootEl.options[selectRootEl.selectedIndex].value+"&t=" + new Date());
                        }
                    });
                    Event.on('#dir-bind', 'change', function(e) {
                        DOM.hide('#J_BoxSwitch');
                        DOM.show('#message');
                        var selectRootEl = S.get('#root-bind');
                        var selectSubEl = S.get('#dir-bind');
                        S.getScript("ppzbg.jsp?" + "pid=bindDir&callback=UCOOL.Pz.bindDir&rootDir="+selectRootEl.options[selectRootEl.selectedIndex].value+"&subDir="+selectSubEl.options[selectSubEl.selectedIndex].value+"&t=" + new Date(), null);
                    });
                },

                change:function(pid, success, curState) {
                    _change(pid, success, curState);
                },
                doOnce:function(pid, success, time) {
                    _doOnce(pid, success, time);
                },
                bindDir:function(pid, success, data) {
                    _bindDir(pid, success, data);
                },
                loadSub:function(pid, success, data) {
                    _loadSub(pid, success, data);
                }
            }
        }();

        UCOOL.Pz.init();
    });
</script>
</body>
</html>
