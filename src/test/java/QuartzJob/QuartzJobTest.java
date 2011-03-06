package QuartzJob;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.text.ParseException;
import java.util.Date;

/**
 * @author <a href="mailto:zhangting@taobao.com">уем╕</a>
 * @since 10-10-27 обнГ3:53
 */
public class QuartzJobTest {
    public static void main(String[] args) throws SchedulerException, ParseException {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        System.out.println(new Date());
        Scheduler scheduler = (Scheduler) context.getBean("startQuertz");
        CronTriggerBean trigger = (CronTriggerBean) scheduler.getTrigger("doTime", Scheduler.DEFAULT_GROUP);
        String dbCronExpression = "*/5 * * * * ?";

        trigger.setCronExpression(dbCronExpression);
        scheduler.rescheduleJob("doTime", Scheduler.DEFAULT_GROUP, trigger);

        scheduler.shutdown();
    }
}
