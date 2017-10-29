package com.product_stand.test;

import com.product_stand.test.api.ExampleService;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.Random;

@SessionScoped
@ManagedBean
public class MainView implements Serializable {
    private String hello = "Hello";

    public void changeHello() {
        hello = hello + new Random().nextInt();
    }

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }
}
