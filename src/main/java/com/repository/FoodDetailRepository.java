package com.repository;

import com.domain.FoodDetail;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class FoodDetailRepository {

    @PersistenceContext
    private EntityManager em;

    public FoodDetail findById(String id) {
        return em.find(FoodDetail.class, id);
    }

    public List<FoodDetail> findAll() {
        List<FoodDetail> resultList = em.createQuery("select food from FoodDetail food").getResultList();
        return resultList;
    }

}
