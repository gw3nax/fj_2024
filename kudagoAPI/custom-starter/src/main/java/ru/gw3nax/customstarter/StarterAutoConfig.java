package ru.gw3nax.customstarter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.gw3nax.customstarter.PostProxy.PostProxyInvokerContextListener;
import ru.gw3nax.customstarter.aspect.ProfilingAnnotationAspect;

@Configuration(proxyBeanMethods = false)
@EnableAspectJAutoProxy
public class StarterAutoConfig {

    @Bean
    public ProfilingAnnotationAspect profilingAnnotationAspect() {
        return new ProfilingAnnotationAspect();
    }

    @Bean
    public PostProxyInvokerContextListener postProxyInvokerContextListener() {
        return new PostProxyInvokerContextListener();
    }
}
