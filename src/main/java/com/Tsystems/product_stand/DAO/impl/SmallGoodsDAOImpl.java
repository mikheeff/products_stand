package com.Tsystems.product_stand.DAO.impl;

import com.Tsystems.product_stand.DAO.api.SmallGoodsDAO;
import com.Tsystems.product_stand.entities.SmallGoodsEntity;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Stateless(name = "goodsDAO", mappedName = "beanName")
//@EJB(beanInterface = SmallGoodsDAO.class, name = "SmallGoodsDAO")
//@LocalBean
public class SmallGoodsDAOImpl implements SmallGoodsDAO {

    @PersistenceContext(unitName = "product_stand")
    private EntityManager em;

    @Override
    public List<SmallGoodsEntity> getAll() {
        return em.createQuery("select goods from SmallGoodsEntity goods", SmallGoodsEntity.class).getResultList();
    }
    @Override
    public void addSmallGoods(SmallGoodsEntity smallGoodsEntity) {
        em.persist(smallGoodsEntity);
    }

    @Override
    public void deleteSmallGoodsById(int id) {
        em.remove(em.find(SmallGoodsEntity.class, id));
    }

    @Override
    public SmallGoodsEntity getSmallGoodsById(int id) {
        return em.find(SmallGoodsEntity.class, id);
    }

    @Override
    public void updateSmallGoods(SmallGoodsEntity smallGoodsEntity) {
        em.merge(smallGoodsEntity);
    }

    @Override
    public void removeAll() {
        em.createQuery("delete from SmallGoodsEntity").executeUpdate();
    }
}
