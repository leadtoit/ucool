<%@ page contentType="text/html;charset=gbk" language="java" %>
<%@ page import="common.ConfigCenter" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="common.PersonConfig" %>
<%@ page import="biz.file.FileEditor" %>
<%@ page import="java.util.List" %>
<%@ page import="web.handler.impl.PersonConfigHandler" %>
<%@ page import="common.tools.DirSyncTools" %>
<%@ page import="dao.UserDAO" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="gbk"/>
    <meta http-equiv="Expires" content="0">
    <meta http-equiv="kiben" content="no-cache">
    <title>ucool个人配置页</title>
    <link rel="shortcut icon" href="http://www.taobao.com/favicon2.ico" type="image/x-icon" />
    <script type="text/javascript" src="http://assets.taobaocdn.com/s/kissy/1.2.0/kissy-min.js"></script>
    <link rel="stylesheet" href="http://assets.taobaocdn.com/s/kissy/1.1.6/cssbase/base-min.css" />
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
        .new i {
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
            width:330px;
            height:18px;
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
        a {
            color: #f60;
            margin: auto 5px;
        }
        .load {
            background:url(http://img02.taobaocdn.com/tps/i2/T1ob1cXlFsXXXXXXXX-16-16.gif) #f3f1e4 no-repeat 0 2px;
        }
        .icon-success, .icon-del,.icon-save,.icon-load, .icon-add, .icon-cancel, .icon-ok {
            background:url(http://img02.taobaocdn.com/tps/i2/T1RdKeXkVXXXXXXXXX-16-124.png) no-repeat 0 0 transparent;
            display: inline-block;
            width:18px;
            height:18px;
            line-height: 18px;
            text-indent: -9999px;
            vertical-align: middle;
        }
        .icon-del {
            background-position: 0 -18px;
        }
        .icon-save {
            background-position: 0 -36px;
        }
        .icon-success {
            background-position: 0 -52px;
        }
        .icon-add {
            background-position: 0 -70px;
        }
        .icon-cancel {
            background-position: 0 -88px;
        }
        .icon-ok {
            background-position: 0 -106px;
        }
        .status {
            display: inline-block;
            width:18px;
            height:18px;
        }

        .msg-ipad {
            width:120px;
            height:120px;
            display: inline-block;
            opacity: 0.5;
            position:absolute;
            z-index: 9999;
            background-color: #000;
            border-radius: 5px;
            color:#fff;
            text-align: center;
            line-height: 120px;
            font-weight: bold;
        }
        .loading {position:absolute;z-index:10000;}
        .loading .mask{left:0;top:0;width:100%;height:100%;background:url(http://img01.taobaocdn.com/tps/i1/T1rOeeXjpuXXXXXXXX-20-20.png);_background:#000;_filter:alpha(opacity=20);}
        .loading .icon{position:absolute;width:220px;height:19px;left:50%;top:50%;margin:-19px 0 0 -110px;padding-top:18px;font-style:normal;text-align:center;background:url(http://yiminghe.github.com/kissy-dpl/base/build/img//loading.gif) no-repeat;}
        .loading .check-box{
            position: absolute;
            width: 360px;
            left: 50%;
            top: 30%;
            background-color: #FFF;
            padding: 10px;
            margin-left: -180px;
        }
        .loading li {
            height: 24px;
            line-height: 24px;
        }
        .loading input {
            vertical-align: middle;
            margin-right: 10px;
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
            <h1><a href="http://wiki.ued.taobao.net/doku.php?id=user:zhangting:tools:ucool-pro:start" target="_blank">ucool config page</a></h1>
            <a class="version new" href="http://wiki.ued.taobao.net/doku.php?id=user:zhangting:tools:ucool-pro:history:start" target="_blank" title="what's new?">ucool-pro version：0.9.4 <i>?</i></a>
        </div>
    </div>
    <div id="content">
        <div class="box hidden">
            <div class="hd"><h3>MESSAGE</h3></div>
            <div class="bd">服务器将于2011-5-20日晚重启，届时将不可用，约为5分钟，同时升级版本为0.6，将直接采用ip标识用户，目录绑定将失效，请重新绑定，特此告知</div>
        </div>
        <div class="box">
            <div class="hd"><h3>INFO</h3></div>
            <div class="bd">
                <table>
                    <tr>
                        <th>用户 IP 标识：</th>
                        <td><%=request.getRemoteAddr()%>
                            （当前使用ip作为用户唯一标识，请注意ip变化）
                        </td>
                    </tr>
                    <%--<tr>--%>
                        <%--<th></th>--%>
                        <%--<td>--%>
                            <%--<input type="text">--%>
                            <%--<a class="icon-save" href="#" title="保存配置" id="saveConfig">保存配置</a>--%>
                            <%--<a class="icon-load" href="#" title="加载配置" id="loadConfig">加载配置</a>--%>
                            <%--<a class="icon-del" href="#" title="删除配置" id="delConfig">删除配置</a>--%>
                        <%--</td>--%>
                    <%--</tr>--%>
                    <tr>
                        <th>请选择一个绑定目录：</th>
                        <td>
                            <div id="dir">
                                <select name="root-bind" id="root-bind" autocomplete="off" <%if(personConfig.isEnableLocalMapping())out.print("disabled");%>>
                                    <%
                                        List<String> assetsSubDirs = fileEditor.getAssetsSubDirs();
                                        String curDirName = personConfig.getUserDO().getName();
                                        if (dirSyncTools.sync(configCenter.getWebRoot() + personConfig.getUcoolAssetsRoot(), personConfig)) {
                                            personConfig.getUserDO().setName("");
                                            curDirName = "";
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
                                                    out.print("<option selected value=\"" + assetsSubDir + "\">");
                                                } else {
                                                    out.print("<option value=\"" + assetsSubDir + "\">");
                                                }
                                                out.print(assetsSubDir);
                                                out.print("</option>");
                                            }
                                        }
                                    %>
                                </select>　/　
                                <select name="dir-bind" id="dir-bind" autocomplete="off" <%if(personConfig.isEnableLocalMapping())out.print("disabled");%>>
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
                                <span class="status load" id="message" style="display: none"></span>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th>映射前缀：<a style="line-height: normal" class="new" href="http://wiki.ued.taobao.net/doku.php?id=user:zhangting:tools:ucool-pro:mapping-prefix" target="_blank" title="什么是映射前缀?"><i>?</i></a></th>
                        <td>
                            <div>
                                <input type="text" id="bind-path"
                                       value=""
                                        <%if(!personConfig.isEnableLocalMapping())out.print("disabled");%>/>
                                <span class="status" id="bind-path-status"></span>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="box switch">
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
                        <th>手动combo：</th>
                        <td class="op"><a class="<%=configCenter.getStateStyle(personConfig.isEnableLocalCombo())%>" id="enableLocalCombo"></a></td>
                        <td class="note">根据根目录的配置文件combo.properties可以将一个文件以combo的形式拆分<a href="http://wiki.ued.taobao.net/doku.php?id=user:zhangting:tools:ucool-pro:ucool-local-combo">how to use?</a></td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <th>使用服务器 Assets 目录：</th>
                        <td class="op"><a class="<%=configCenter.getStateStyle(personConfig.isEnableAssets())%>" id="enableAssets"></a></td>
                        <td class="note">打开后启用服务器上的assets目录中的文件</td>
                    </tr>
                    <tr>
                        <th>本地目录映射：<sup class="lab">lab</sup></th>
                        <td class="op"><a class="<%=configCenter.getStateStyle(personConfig.isEnableLocalMapping())%>" id="enableLocalMapping"></a></td>
                        <td class="note">通过<a href="http://wiki.ued.taobao.net/doku.php?id=user:zhangting:tools:ucool-pro:hfs" target="_blank">本地工具</a>将请求代理到本机，代理整个目录，此开关和"服务器上的Assets目录"开关互斥</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div id="footer">
        <a href="http://wiki.ued.taobao.net/doku.php?id=user:zhangting:tools:ucool-pro:start" target="_blank"><span class="help">(?) help</span></a>
        <ul class="author">
            <li><a href="mailto:zhangting@taobao.com">开发：张挺(zhangting@taobao.com)</a></li>
            <li><a href="mailto:wuxuan@taobao.com">设计：悟玄(wuxuan@taobao.com)</a></li>
        </ul>
    </div>
</div>
<script type="text/javascript">
    KISSY.ready(function(S) {
        var Event = S.Event,
        DOM = S.DOM,
        JSON = S.JSON;
        S.app('UCOOL');
        S.namespace('Pz');

        UCOOL.Pz = function() {

            var bindPathRequest = false, mappingPopup, ipadStatus;

            // 映射路径的json
            var mappingJSON;
            try{mappingJSON = JSON.parse('<%=personConfig.getUserDO().getMappingPath()%>');}catch(e){mappingJSON = {mappings:[]}};

            var _change = function(pid, success, curState) {
                if (success === 'ok') {
                    _switchChange('#' + pid, curState);
                }
                if(pid === "enableAssets" && curState=="true") {
                    _switchChange('#enableLocalMapping', false);
                    DOM.get('#bind-path').disabled = true;
                    DOM.get('#root-bind').disabled =false;
                    DOM.get('#dir-bind').disabled = false;
                }
                if(pid === "enableLocalMapping") {
                    if (curState=="true") {
                        _switchChange('#enableAssets', false);
                    }
                    DOM.get('#bind-path').disabled = (curState=="true" ? "" : true);
                    DOM.get('#root-bind').disabled = (curState=="true" ? true : "");
                    DOM.get('#dir-bind').disabled = (curState=="true" ? true : "");
                }
            };

            var _doOnce = function(pid, success, time) {
                var el = S.get('#' + pid);
                var curParent = DOM.parent(el);
                DOM.next(curParent).innerHTML = '在' + time + (success === 'ok' ? '清理成功' : '清理失败');
            };

            var _switchChange = function(el, curState) {
                if(curState==="true") {
                    DOM.removeClass(el, 'switch-close');
                    DOM.addClass(el, 'switch-open');
                } else {
                    DOM.removeClass(el, 'switch-open');
                    DOM.addClass(el, 'switch-close');
                }
            };

            var _bindDir = function (pid, success, data) {
                DOM.hide('#message');
                if (success === 'ok') {
                    //switch config
                    if(data !== 'cancel') {
                        _updateConfig(data);
                    } else {
                        //取消要把子目录置位
                        
                    }
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
                }
            };

            var _updateConfig = function(config) {
                S.get('#assetsdebugswitch').className='';
                S.get('#bindPrepub').className='';
                S.get('#enableAssets').className='';
                S.get('#enableLocalCombo').className='';
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
                if(ConfigParser.isEnableLocalMapping(config)) {
                    DOM.addClass('#enableLocalMapping', 'switch-open');
                } else {
                    DOM.addClass('#enableLocalMapping', 'switch-close');
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
                },
                isEnableLocalMapping : function(config) {
                    return (config & 16) == 16;
                }
            };

            // 构造映射json并发送
            var _bindPath = function(fn) {
                var bindPathEl = S.get('#bind-path');
                bindPathRequest = false;
                DOM.removeClass('#bind-path-status', 'icon-success');
                DOM.addClass('#bind-path-status', 'load');
                DOM.show('#bind-path-status');
                S.jsonp('ppzbg.jsp?pid=bindPath&mappingPath=' + JSON.stringify(mappingJSON), function(data){
                    DOM.hide('#message');
                    if(data.success === "true") {
                        DOM.removeClass('#bind-path-status', 'load');
                        DOM.addClass('#bind-path-status', 'icon-success');
                        fn && fn.call();
                    }
                });
            };

            var _saveConfig = function(pid, success, data){
                if(success=='ok') {
                    ipadStatus.show();
                    S.later(function(){
                        ipadStatus.hide();
                    }, 1500);
                } else {
                
                }
            };

            var eventHandler = {
                assetsdebugswitch:"assetsdebugswitch",
                bindPrepub:"bindPrepub",
                enableAssets:"enableAssets",
                enableLocalCombo:"enableLocalCombo"
            };

            return {
                init:function() {
                    for (var eventEL in eventHandler) {
                        var eventTarget = eventHandler[eventEL];
                        Event.on('#'+eventEL, 'click', function(eventTarget) {
                            return function(e){
                                S.getScript("ppzbg.jsp?" + "pid=" + eventTarget + "&callback=UCOOL.Pz.change&t=" + new Date());
                            };
                        }(eventTarget));
                    }

                    Event.on('#root-bind', 'change', function(e) {
//                        DOM.hide('#J_BoxSwitch');
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
//                        DOM.hide('#J_BoxSwitch');
                        DOM.show('#message');
                        var selectRootEl = S.get('#root-bind');
                        var selectSubEl = S.get('#dir-bind');
                        S.getScript("ppzbg.jsp?" + "pid=bindDir&callback=UCOOL.Pz.bindDir&rootDir="+selectRootEl.options[selectRootEl.selectedIndex].value+"&subDir="+selectSubEl.options[selectSubEl.selectedIndex].value+"&t=" + new Date(), null);
                    });

                    Event.on('#enableLocalMapping', 'click', function(e) {
                        _bindPath(function(){
                            // 先去绑定一次映射路径
                            S.getScript("ppzbg.jsp?" + "pid=enableLocalMapping&callback=UCOOL.Pz.change&t=" + new Date());
                        });
                    });

                    // 填充映射
                    var paths = [];
                    for (var i = 0; i < mappingJSON.mappings.length; i++) {
                        if (mappingJSON.mappings[i].use) {
                            paths.push(mappingJSON.mappings[i].path);
                        }
                    }
                    DOM.get('#bind-path').value = paths.join(';');

                    KISSY.use('ua,overlay, template', function(S, UA, O, T) {

                        var ipadMsg = document.createElement("div");
                        ipadMsg.innerHTML = '<div class="msg-ipad"">保存成功</div>';
                        ipadStatus = new O.Popup({
                                    content: ipadMsg,
                                    width: 120,
                                    height: 120,
                                    elStyle:{
                                        position:UA.ie == 6 ? "absolute" : "fixed"
                                    },
                                    align: {
                                        points: ['cc', 'cc']
                                    },
                                    effect: {
                                        effect:"fade",
                                        duration:0.5
                                    }
                                });

                        var _inited = false;

                        // 总的内容模板
                        var mappingChecksTemplate = T('<h3 class="{{#if mappings.length==0}} hidden {{/if}}">已保存（上限5个）：</h3>'+
                                '<ul id="mapping-check" class="checks {{#if mappings.length==0}} hidden {{/if}}"></ul>'+
                                '<div style="margin-top:10px">添加一个映射前缀：<input type="text" style="width: 200px;" maxlength="100" placeholder="例如：/apps/buy 或者 /p/fp"/><a href="#" class="icon-add" id="mappingAdd" title="添加">添加</a>'+
                                '</div><div style="margin-top:10px"><a href="#" class="icon-ok" title="保存" id="addMappingOK" style="text-indent: 0;padding-left: 18px;width: auto">保存</a>' +
                                '<a href="#" class="icon-cancel" title="取消" id="addMappingCancel" style="text-indent: 0;padding-left: 18px;width: auto">取消</a></div>');

                        //各li模板
                        var mappingChecksLiTemplate = T('{{#each mappings}}<li>'+
                                '<a class="icon-del" href="#" title="删除">删除</a><input type="checkbox" value="{{_ks_value.path}}" {{#if _ks_value.use}}checked{{/if}}/><label for="">{{_ks_value.path}}</label></li>{{/each}}');
                        // 添加的li模板
                        var mappingChecks = T('<li><a class="icon-del" href="#" title="删除">删除</a><input type="checkbox" value="{{path}}" checked /><label for="">{{path}}</label></li>');

                        Event.on('#bind-path', 'focus', function(e) {
                            mappingPopup.show();
                            S.one('#mapping-check').html(mappingChecksLiTemplate.render(mappingJSON));
                            if(!_inited) {
                                _inited = true;
                                //bind pupup event
                                Event.on('#addMappingOK,#addMappingCancel', 'click', function(e){
                                    e.halt();
                                    if(e.target.id==='addMappingOK') {
                                        mappingJSON.mappings.length = 0;
                                        //save mapping
                                        S.query('#mapping-check input').each(function(el){
                                            mappingJSON.mappings.push({path:el.value,use:el.checked});
                                        });
                                    }
                                    mappingJSON.mappings.sort(function(a, b){
                                        if(a.path === b.path) {
                                            return 0;
                                        }
                                        var l = a.path.length > b.path.length ? b.path.length: a.path.length;
                                        var i = 0;
                                        while(i < l) {
                                            if(a.path.charCodeAt(i) > b.path.charCodeAt(i)) {
                                                return -1;
                                            } else if(a.path.charCodeAt(i) < b.path.charCodeAt(i)) {
                                                return 1;
                                            }
                                            i++;
                                        }
                                        if(i == a.path.length) {
                                            return 1;
                                        } else {
                                            return -1;
                                        }
                                    });
                                    mappingPopup.hide();
                                    var paths = [];
                                    for (var i = 0; i < mappingJSON.mappings.length; i++) {
                                        if(mappingJSON.mappings[i].use) {
                                            paths.push(mappingJSON.mappings[i].path);
                                        }
                                    }
                                    DOM.get('#bind-path').value = paths.join(';');
                                    _bindPath(undefined);
                                });

                                Event.on('#mappingAdd', 'click', function(e){
                                    e.halt();
                                    var path = S.trim(DOM.prev(e.target).value);
                                    if(!path) {
                                        alert('别试了，不会让空的内容过的。。');
                                        return;
                                    }
                                    if(mappingJSON.mappings.length >=5) {
                                        alert('超过限制了，删除几个不用的吧，或者悄悄跟张挺说，给你多几个。。');
                                        return;
                                    }
                                    var checkObject = {path:path, use:false};
                                    for (var i = 0; i < mappingJSON.mappings.length; i++) {
                                        var obj = mappingJSON.mappings[i];
                                        if(obj.path === checkObject.path) {
                                            alert('这个映射已经有了');
                                            return;
                                        }
                                    }

                                    var checkParent = DOM.parent(e.target, '.check-box');

                                    S.one('#mapping-check').append(mappingChecks.render(checkObject)).show();
                                    S.one('h3', checkParent).show();
                                    mappingJSON.mappings.push(checkObject);
                                    DOM.prev(e.target).value = '';
                                });

                                //delete
                                Event.on('#mapping-check', 'click', function(e){
                                    if(DOM.hasClass(e.target, 'icon-del')) {
                                        DOM.remove(e.target.parentNode);
                                    }
                                });
                            }
                            DOM.hide('#bind-path-status');
                        });

                        var mappingSelect = document.createElement("div");
                        mappingSelect.innerHTML = '<div class="loading" style="width:950px;height:650px;"> '
                                + '<div class="mask"></div><div class="check-box">'
                                + mappingChecksTemplate.render(mappingJSON)
                                + '</div></div>';
                        mappingPopup = new O.Popup({
                                    content: mappingSelect,
                                    width: 950,
                                    height: 650,
                                    elStyle:{
                                        position:UA.ie == 6 ? "absolute" : "fixed"
                                    },
                                    align: {
                                        points: ['cc', 'cc']
                                    },
                                    effect: {
                                        effect:"fade",
                                        duration:0.5
                                    }
                                });

                        Event.on("#saveConfig", "click", function(e) {
                            e.halt();
                            
//                            S.getScript("ppzbg.jsp?" + "pid=saveConfig&alias=xxxxx&callback=UCOOL.Pz.saveConfig&t=" + new Date());
                        });
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
                },
                saveConfig:function(pid, success, data){
                    _saveConfig(pid, success, data);
                }
            }
        }();

        UCOOL.Pz.init();
    });
</script>
</body>
</html>
