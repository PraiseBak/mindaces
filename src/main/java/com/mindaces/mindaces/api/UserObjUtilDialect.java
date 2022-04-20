package com.mindaces.mindaces.api;

import com.mindaces.mindaces.service.ObjService;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Collections;
import java.util.Set;


@Component
public class UserObjUtilDialect extends AbstractDialect implements IExpressionObjectDialect {

    protected UserObjUtilDialect()
    {
        super("userObjUtil");

    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return new IExpressionObjectFactory()
        {

            @Override
            public Set<String> getAllExpressionObjectNames() {
                return Collections.singleton("userObjUtil");
            }


            @Override
            public Object buildObject(IExpressionContext context, String expressionObjectName) {
                return new UserObjUtil();
            }

            @Override
            public boolean isCacheable(String expressionObjectName) {
                return true;
            }
        };
    }



}