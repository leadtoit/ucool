package common;

import web.handler.Handler;

import java.util.Map;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-9-23 13:36:54
 */
public class DispatchMapping {
    
    private Map<String, Handler> mappingTable = null;

    public void setMappingTable(Map<String, Handler> mappingTable) {
        this.mappingTable = mappingTable;
    }

    /**
     * Method getMapping ...
     *
     * @param key of type String
     * @return Object
     */
    public Handler getMapping(String key){
        return this.mappingTable.get(key);
    }
}
