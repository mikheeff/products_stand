package com.Tsystems.product_stand.controllers;

import com.Tsystems.product_stand.services.api.SmallGoodsService;
import com.tsystems.SmallGoods;
import org.primefaces.context.RequestContext;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.servlet.http.HttpSession;
import java.util.List;

import static org.primefaces.context.RequestContext.INSTANCE_KEY;
@Stateless
@ApplicationScoped
@ManagedBean
public class MainView {
    @EJB
    private SmallGoodsService smallGoodsService;
    private List<SmallGoods> bestSellersList;
    @PostConstruct
    public void init(){
        smallGoodsService.removeAll();
        smallGoodsService.loadAllGoodsToDB();
        try {
            smallGoodsService.receiveMessage();
        } catch (JMSException e) {
            e.printStackTrace();
        }

//         this.requestContext = RequestContext.getCurrentInstance();
//        requestContext.update("form:goodsGrid");
//            requestContext = RequestContext.getCurrentInstance();
        setBestSellersList();
    }



    public List<SmallGoods> getBestSellersList() {
        return smallGoodsService.getBestSellers();
    }
    public void setBestSellersList() {
        this.bestSellersList = smallGoodsService.getBestSellers();
    }


    public List<SmallGoods> getAllGoods() {
        return smallGoodsService.getAll();
    }

    public void refreshForm(){
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.update("form:goodsGrid");
//        RequestContext requestContext = MainView.requestContext;
//        RequestContext requestContext = (RequestContext) FacesContext.getCurrentInstance().getAttributes().get(INSTANCE_KEY);
    }


}
