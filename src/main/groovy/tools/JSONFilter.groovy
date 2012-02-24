package tools;
import net.sf.json.JSONObject
import net.sf.json.JSONArray
/**
 * Created by IntelliJ IDEA.
 * User: czy-thinkpad
 * Date: 11-12-5
 * Time: ����12:14
 * To change this template use File | Settings | File Templates.
 */
class JSONFilter {

    String getValidateMapping(String mappingPath) {
        if (mappingPath != null && !"".equals(mappingPath)) {
            //����json
            JSONObject jsonObject = JSONObject.fromObject(mappingPath);
            JSONArray jsonArray = jsonObject.getJSONArray("mappings");

            // ȡ�õ�ǰ��ӳ��·��
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

    String getUsedMappings(String mappingPath) {
        if (mappingPath != null && !"".equals(mappingPath)) {
            //����json
            JSONObject jsonObject = JSONObject.fromObject(mappingPath);
            JSONArray jsonArray = jsonObject.getJSONArray("mappings");
            StringBuilder sb = new StringBuilder();

            // ȡ�õ�ǰ��ӳ��·��
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
