package common.tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: czy-thinkpad
 * Date: 11-6-4
 * Time: 下午4:14
 * To change this template use File | Settings | File Templates.
 */
public class JSONFilter {
    
    public String getValidateMapping(String mappingPath) {
        if (mappingPath != null && !"".equals(mappingPath)) {
            //解析json
            JSONObject jsonObject = JSONObject.fromObject(mappingPath);
            JSONArray jsonArray = jsonObject.getJSONArray("mappings");

            // 取得当前的映射路径
            for (Object mp : jsonArray) {
                JSONObject temp = (JSONObject) mp;
                String path = temp.getString("path");
                if (path.equals("/")) {
                    continue;
                }
                if (!path.startsWith("/")) {
                    temp.element("path", "/" + path);
                }
                if (path.endsWith("/")) {
                    temp.element("path", path.substring(0, path.length() - 1));
                }
            }
            return jsonObject.toString();
        }
        return "";
    }

    public String getUsedMappings(String mappingPath) {
        if (mappingPath != null && !"".equals(mappingPath)) {
            //解析json
            JSONObject jsonObject = JSONObject.fromObject(mappingPath);
            JSONArray jsonArray = jsonObject.getJSONArray("mappings");
            StringBuilder sb = new StringBuilder();

            // 取得当前的映射路径
            for (Object mp : jsonArray) {
                JSONObject temp = (JSONObject) mp;
                if(temp.getBoolean("use")) {
                    sb.append(temp.getString("path")).append(";");
                }
            }
            return sb.toString();
        }
        return "";
    }
}
