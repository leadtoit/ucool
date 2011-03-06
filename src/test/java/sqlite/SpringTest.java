package sqlite;

import dao.entity.UserDO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-12-2 下午11:11
 */
public class SpringTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        JdbcTemplate jdbcTemplate = (JdbcTemplate) context.getBean("jdbcTemplate");
        System.out.println(jdbcTemplate == null);
//        String sql = "insert into user (host_name,dir, config) values (?,?,?)";
//        if (jdbcTemplate != null) {
//            jdbcTemplate.update(sql, new Object[]{"czy-notebook1", "这是中文", 5});
//        }

        String sql = "select * from user where host_name=?";
        String hostName = "czy-notebook1";
        final UserDO user = new UserDO();
        if (jdbcTemplate != null) {
            jdbcTemplate.queryForObject(sql, new Object[]{hostName}, new RowMapper() {
                @Override public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    user.setId(rs.getLong("id"));
                    user.setHostName(rs.getString("host_name"));
                    return null;
                }
            });
        }
        System.out.println(user.getHostName());
    }
}
