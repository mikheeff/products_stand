package com.product_stand.test.api;

import javax.ejb.Remote;
import java.util.Map;

public interface ExampleService {

    String greet(String name);

    Map<Object, Object> getSystemProperties();
}
