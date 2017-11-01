package com.Tsystems.product_stand.DAO.impl;

import com.Tsystems.product_stand.DAO.api.SmallGoodsDAO;
import com.Tsystems.product_stand.entities.SmallGoodsEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Stateless
public class SmallGoodsDAOImpl implements SmallGoodsDAO {

    @PersistenceContext(unitName = "product_stand")
    private EntityManager em;

    public List<SmallGoodsEntity> getAll() {
        return em.createQuery("select goods from SmallGoodsEntity goods", SmallGoodsEntity.class).getResultList();
    }
}
