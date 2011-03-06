package QuartzJob;

import java.util.Date;

/**
 * @author <a href="mailto:zhangting@taobao.com">张挺</a>
 * @since 10-10-27 下午3:41
 */
public class QuartzJob {
    public void work() {
        System.out.println("Quartz的任务调度！！！" + new Date());
    }
}
