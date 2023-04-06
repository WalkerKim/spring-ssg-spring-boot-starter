package kim.figure.spring.ssg.autoconfigure;

import kim.figure.springssg.HtmlGenerator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import java.util.Arrays;

/**
 * author         : walker
 * date           : 2023. 03. 17.
 * description    :
 */
@Configuration
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@EnableConfigurationProperties
@ConditionalOnMissingBean(type = "kim.figure.spring.ssg.autoconfigure.SsgAutoconfiguration")
public class SsgAutoconfiguration implements BeanFactoryPostProcessor {

    @Bean
    public HtmlGenerator htmlGenerator(final RequestMappingHandlerMapping requestMappingHandlerMapping, final @Qualifier("viewControllerHandlerMapping") HandlerMapping viewControllerHandlerMapping, final ResourceUrlProvider resourceUrlProvider, final ApplicationContext applicationContext, @Value("${dist.path:dist}") String distPath, @Value("${server.port:8080}") String port){
        HtmlGenerator htmlGenerator = new HtmlGenerator(resourceUrlProvider, distPath, Arrays.asList(requestMappingHandlerMapping, viewControllerHandlerMapping), port, applicationContext, true);
        return htmlGenerator;
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("htmlGenerator");
        beanDefinition.setDependsOn("staticPagePathFinder");
    }
}
