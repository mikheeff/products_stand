package com.product_stand.test.api;

import javax.ejb.Remote;
import java.util.Map;

@Remote
public interface ExampleService {

    String greet(String name);

    Map<Object, Object> getSystemProperties();
}
