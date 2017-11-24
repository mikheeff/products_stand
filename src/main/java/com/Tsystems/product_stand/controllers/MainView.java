package com.Tsystems.product_stand.controllers;

import com.Tsystems.product_stand.Configuration.ConfigurationClass;
import com.Tsystems.product_stand.PushEvent;
import com.Tsystems.product_stand.services.api.SmallGoodsService;
import com.tsystems.Event;
import com.tsystems.SmallGoods;
import org.atmosphere.util.StringEscapeUtils;
import org.omnifaces.cdi.Push;
import org.omnifaces.util.Ajax;
import org.primefaces.context.RequestContext;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
@ApplicationScoped
@ManagedBean
public class MainView {
    @EJB
    private SmallGoodsService smallGoodsService;
    @Inject @Push(channel = "channel")
    org.omnifaces.cdi.PushContext channel;


    public void redirectToProductDetails(int id){
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(ConfigurationClass.SERVER_URL+"/catalog/goods/"+id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<SmallGoods> getBestSellersList() {
        return smallGoodsService.getBestSellers();
    }


    public void refreshForm(@Observes PushEvent event){
        channel.send("event");
    }

}
