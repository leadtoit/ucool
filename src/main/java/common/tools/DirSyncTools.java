package common.tools;

import biz.file.FileEditor;
import common.PersonConfig;
import dao.UserDAO;

/**
 * Created by IntelliJ IDEA.
 * User: zhangting
 * Date: 11-1-20
 * Time: 上午11:14
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public class DirSyncTools {

    private FileEditor fileEditor;

    private UserDAO userDAO;

    public void setFileEditor(FileEditor fileEditor) {
        this.fileEditor = fileEditor;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Method sync ...
     *
     * @param dirPath of type String 需要同步的目录路径
     * @param personConfig of type PersonConfig
     * @return boolean
     */
    public boolean sync(String dirPath, PersonConfig personConfig) {
        /**
         * 页面没刷新，目录被删除，同步有2种可能
         * 1、当前目录存在，目标目录不存在
         * 2、当前目录不存在，切换配置
         */

        if (!fileEditor.findFile(dirPath)) {
            //删除目录
            userDAO.updateDir(personConfig.getUserDO().getId(), "", personConfig.getUserDO().getName());
            return true;
        }
        return false;
    }
}
