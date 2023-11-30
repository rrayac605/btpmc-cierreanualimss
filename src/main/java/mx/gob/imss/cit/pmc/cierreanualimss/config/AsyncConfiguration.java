package mx.gob.imss.cit.pmc.cierreanualimss.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import mx.gob.imss.cit.pmc.cierreanualimss.constants.CierreAnualImssConstants;

@Configuration
@EnableAsync
public class AsyncConfiguration {

    @Bean(name = "taskExecutorController")
    public Executor taskExecutorController() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(CierreAnualImssConstants.POOL_SIZE);
        taskExecutor.setMaxPoolSize(CierreAnualImssConstants.POOL_SIZE);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }

}
