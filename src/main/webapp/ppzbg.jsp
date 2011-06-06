<%@ page contentType="text/html;charset=gbk" language="java" %>
<%@ page import="biz.file.FileEditor" %>
<%@ page import="common.ConfigCenter" %>
<%@ page import="common.PersonConfig" %>
<%@ page import="dao.UserDAO" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="web.handler.impl.PersonConfigHandler" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="dao.ConfigDAO" %>
<%@ page import="dao.entity.ConfigDO" %>
<%@ page import="common.tools.JSONFilter" %>
<%
    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
    ConfigCenter configCenter = (ConfigCenter) wac.getBean("configCenter");
    FileEditor fileEditor = (FileEditor) wac.getBean("fileEditor");
    PersonConfigHandler personConfigHandler = (PersonConfigHandler) wac.getBean("personConfigHandler");
    UserDAO userDAO = (UserDAO) wac.getBean("userDAO");
    ConfigDAO configDAO = (ConfigDAO) wac.getBean("configDAO");
    String pid = request.getParameter("pid");
    String callback = request.getParameter("callback");

    PersonConfig personConfig = personConfigHandler.doHandler(request);

    if(personConfig.isNewUser()) {
        boolean op = userDAO.createNewUser(personConfig.getUserDO());
        if (!op) {
            out.print(callback + "(\'" + pid + "\',\'error\', \'create user error\');");
            return;
        } else {
            //重新取一次
            personConfig.setNewUser(false);
            personConfig = personConfigHandler.doHandler(request);
        }
    }


    String srcMappingPath = personConfig.getUserDO().getMappingPath();

    int srcConfig = personConfig.getUserDO().getConfig();

    if (pid != null) {
        String tState = null;
        if (pid.equalsIgnoreCase("assetsdebugswitch")) {
            personConfig.setUcoolAssetsDebug(!personConfig.isUcoolAssetsDebug());
            userDAO.updateConfig(personConfig.getUserDO().getId(), personConfig.getUserDO().getConfig(), srcConfig);
            tState = personConfig.isUcoolAssetsDebug() ? "true" : "false";
        } else if (pid.equalsIgnoreCase("cleanOnlineCache")) {
            fileEditor.removeDirectory(configCenter.getWebRoot() + personConfig.getUcoolCacheRoot());
            tState = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒").format(new Date());
        } else if (pid.equalsIgnoreCase("bindPrepub")) {
            personConfig.setPrepub(!personConfig.isPrepub());
            userDAO.updateConfig(personConfig.getUserDO().getId(), personConfig.getUserDO().getConfig(), srcConfig);
            tState = personConfig.isPrepub() ? "true" : "false";
        } else if (pid.equalsIgnoreCase("enableAssets")) {
            personConfig.setEnableAssets(!personConfig.isEnableAssets());
            //和本地映射互斥
            if(personConfig.isEnableAssets()) {
                personConfig.setEnableLocalMapping(false);
            }
            
            userDAO.updateConfig(personConfig.getUserDO().getId(), personConfig.getUserDO().getConfig(), srcConfig);
            tState = personConfig.isEnableAssets() ? "true" : "false";
        } else if(pid.equalsIgnoreCase("bindPath")) {
            String mappingPath = request.getParameter("mappingPath");
            if(personConfig.isNewUser()) {
                //create user
                personConfig.getUserDO().setName("");
                boolean op = userDAO.createNewUser(personConfig.getUserDO());
                if (!op) {
                    out.print(callback + "(" + "{\"pid\":\""+ pid + "\",\"success\":\"error\", \"message\":\"create user error\"}" + ")");
                    return;
                }
            }
            JSONFilter filter = (JSONFilter) wac.getBean("jsonFilter");
            personConfig.getUserDO().setMappingPath(filter.getValidateMapping(mappingPath));
            //update
            boolean op = userDAO.updateMappingPath(personConfig.getUserDO().getId(), personConfig.getUserDO().getMappingPath(), srcMappingPath);
            tState = op ? "true" : "false";
            out.print(callback + "(" + "{\"pid\":\""+ pid + "\",\"success\":\"" +tState+ "\"}" + ")");
            return;
        } else if (pid.equalsIgnoreCase("enableLocalMapping")) {
            personConfig.setEnableLocalMapping(!personConfig.isEnableLocalMapping());
            //启用assets和这个开关互斥
            if(personConfig.isEnableLocalMapping()) {
                personConfig.setEnableAssets(false);
            }

            userDAO.updateConfig(personConfig.getUserDO().getId(), personConfig.getUserDO().getConfig(), srcConfig);
            tState = personConfig.isEnableLocalMapping() ? "true" : "false";
        } else if(pid.equalsIgnoreCase("enableLocalCombo")) {
            personConfig.setEnableLocalCombo(!personConfig.isEnableLocalCombo());
            userDAO.updateConfig(personConfig.getUserDO().getId(), personConfig.getUserDO().getConfig(), srcConfig);
            tState = personConfig.isEnableLocalCombo() ? "true" : "false";
        } else if (pid.equalsIgnoreCase("bindDir")) {
            String rootDir = request.getParameter("rootDir");
            String subDir = request.getParameter("subDir");

            if (subDir == null || subDir.isEmpty()) {
                out.print(callback + "(\'" + pid + "\',\'error\', \'directory name is null\');");
                return;
            }
            String targetPath = rootDir + "/" + subDir;
            if("-1".equals(rootDir) || "-1".equals(subDir)) {
                targetPath = "";
            }
            //校验新目录的有效性
            if(!fileEditor.findFile(configCenter.getWebRoot() + configCenter.getUcoolAssetsRoot() + "/" + targetPath)) {
                out.print(callback + "(\'" + pid + "\',\'error\', \'target directory is not found，please refresh\');");
                return;
            }

            //secord create a new user and bind subDir
            boolean op;
            if (personConfig.isNewUser()) {
                personConfig.setNewUser(false);
                personConfig.getUserDO().setName(targetPath);
                if(userDAO.getPersonInfo(personConfig.getUserDO().getHostName()) != null) {
                    op = userDAO.updateDir(personConfig.getUserDO().getId(), targetPath, personConfig.getUserDO().getName());
                    personConfig.getUserDO().setName(targetPath);
                    if (!op) {
                        out.print(callback + "(\'" + pid + "\',\'error\', \'update config error\');");
                        return;
                    }
                } else {
                    op = userDAO.createNewUser(personConfig.getUserDO());
                    if (!op) {
                        out.print(callback + "(\'" + pid + "\',\'error\', \'create user error\');");
                        return;
                    }
                }
            } else {
                op = userDAO.updateDir(personConfig.getUserDO().getId(), targetPath, personConfig.getUserDO().getName());
                personConfig.getUserDO().setName(targetPath);
                if (!op) {
                    out.print(callback + "(\'" + pid + "\',\'error\', \'update config error\');");
                    return;
                }
            }
            if (personConfig.getUserDO().getName().equals("")) {
                //取消绑定的情况
                out.print(callback + "(\'" + pid + "\',\'ok\', \'cancel\');");
            } else {
                out.print(callback + "(\'" + pid + "\',\'ok\', \'" + personConfig.getUserDO().getConfig() + "\');");
            }
            return;
        } else if("loadSub".equals(pid)) {
            String rootDir = request.getParameter("rootDir");
            if(rootDir == null) {
                out.print(callback + "(\'" + pid + "\',\'error\', \'load sub directory error\');");
                return;
            }
            List<String> subDirs = fileEditor.getAssetsSubDirs(rootDir);
            StringBuilder sb = new StringBuilder();
            for (String subDir : subDirs) {
                sb.append(subDir);
                sb.append(";");
            }
            if(sb.length()>0) {
                sb.deleteCharAt(sb.length()-1);
            }
            tState = sb.toString();
        } else if("saveConfig".endsWith(pid)) {
            String alias = request.getParameter("alias");
            ConfigDO configDO = new ConfigDO();
            configDO.setAlias(alias);
            configDO.setConfig(personConfig.getUserDO().getConfig());
            configDO.setName(personConfig.getUserDO().getName());
            configDO.setMappingPath(personConfig.getUserDO().getMappingPath());
            configDO.setIp(personConfig.getUserDO().getHostName());

            ConfigDO tempConfig = configDAO.getConfigByName(configDO.getAlias());
            if(tempConfig == null) {
                configDAO.addConfig(configDO);
            } else {
                configDO.setId(tempConfig.getId());
                configDAO.updateConfig(configDO);
            }
        } else if("loadConfig".endsWith(pid)) {
            String alias = request.getParameter("alias");
            ConfigDO tempConfig = configDAO.getConfigByName(alias);
            out.print(callback + "(\'" + pid + "\',\'ok\', \'" + tempConfig + "\');");
        }

        if (callback != null) {
            out.print(callback + "(\'" + pid + "\',\'ok\', \'" + tState + "\');");
        }
    }
%>
