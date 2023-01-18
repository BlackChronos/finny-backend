package com.github.sbooster.templates.backend.rsocket.handler;

import org.springframework.messaging.handler.MessagingAdviceBean;
import org.springframework.web.method.ControllerAdviceBean;

@SuppressWarnings("NullableProblems")
public record ExceptionMessageHandlerAdvice(ControllerAdviceBean adviceBean) implements MessagingAdviceBean {
    @Override
    public Class<?> getBeanType() {
        return this.adviceBean.getBeanType();
    }

    @Override
    public Object resolveBean() {
        return this.adviceBean.resolveBean();
    }

    @Override
    public boolean isApplicableToBeanType(final Class<?> beanType) {
        return this.adviceBean.isApplicableToBeanType(beanType);
    }

    @Override
    public int getOrder() {
        return this.adviceBean.getOrder();
    }
}
