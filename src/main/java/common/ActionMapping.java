package common;

import action.BaseAction;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: czy-thinkpad
 * Date: 11-12-18
 * Time: обнГ11:45
 * To change this template use File | Settings | File Templates.
 */
public class ActionMapping {
    private Map<String, BaseAction> mappingTable = null;

    public void setMappingTable(Map<String, BaseAction> mappingTable) {
        this.mappingTable = mappingTable;
    }

    /**
     * Method getMapping ...
     *
     * @param key of type String
     * @return Object
     */
    public BaseAction getMapping(String key){
        return this.mappingTable.get(key);
    }
}
