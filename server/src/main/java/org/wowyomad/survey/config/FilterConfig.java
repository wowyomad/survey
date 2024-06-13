package org.wowyomad.survey.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wowyomad.survey.filter.SessionFilter;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<SessionFilter> sessionFilter() {
        FilterRegistrationBean<SessionFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SessionFilter());
        registrationBean.addUrlPatterns("*.html");
        registrationBean.setName("SessionFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
