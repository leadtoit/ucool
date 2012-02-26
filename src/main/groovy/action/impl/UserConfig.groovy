package action.impl

import action.BaseAction
import dao.ConfigDAO
import dao.entity.ConfigDO

/**
 * Created by IntelliJ IDEA.
 * User: czy-thinkpad
 * Date: 11-12-19
 * Time: ÉÏÎç12:09
 * To change this template use File | Settings | File Templates.
 */
class SaveConfig extends BaseAction{

    def ConfigDAO configDAO

    void setConfigDAO(ConfigDAO configDAO) {
        this.configDAO = configDAO
    }


    @Override
    def processing(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {
        def configParam = request.getParameter("config")

        ConfigDO configDO = new ConfigDO();

        this.configDAO.addConfig(configDO);
        return ['ok', '1']
    }
}

class GetConfig extends BaseAction{
    @Override
    def processing(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {
        return null  //To change body of implemented methods use File | Settings | File Templates.
    }
}

class DelConfig extends BaseAction {
    @Override
    def processing(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {
        return null  //To change body of implemented methods use File | Settings | File Templates.
    }

}
