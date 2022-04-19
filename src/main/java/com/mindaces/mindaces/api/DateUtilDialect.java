package com.mindaces.mindaces.api;

import java.util.Collections;

import java.util.Set;



import org.springframework.stereotype.Component;

import org.thymeleaf.context.IExpressionContext;

import org.thymeleaf.dialect.AbstractDialect;

import org.thymeleaf.dialect.IExpressionObjectDialect;

import org.thymeleaf.expression.IExpressionObjectFactory;



@Component

public class DateUtilDialect extends AbstractDialect implements IExpressionObjectDialect {

    protected DateUtilDialect()
    {
        super("dateUtil");

    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return new IExpressionObjectFactory()
        {

            @Override
            public Set<String> getAllExpressionObjectNames() {
                return Collections.singleton("dateUtil");
            }


            @Override
            public Object buildObject(IExpressionContext context, String expressionObjectName) {
                return new DateUtil();
            }

            @Override
            public boolean isCacheable(String expressionObjectName) {
                return true;
            }
        };
    }



}