package com.product_stand.test.impl;

import com.product_stand.test.api.ExampleService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.Map;

@Stateless
public class ExampleServiceImpl implements ExampleService{
    @Override
    public String greet(String name) {
        return "Hello " + name + "!";
    }

    @Override
    public Map<Object, Object> getSystemProperties() {
        return new HashMap<>(System.getProperties());
    }
}
