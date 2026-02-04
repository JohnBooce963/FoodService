package com.goo.mai_roo.model.repository;

import com.goo.mai_roo.model.dao.FoodDAO;
import com.goo.mai_roo.model.entities.*;
import com.goo.mai_roo.view.rest.body.FoodListReq;
import com.goo.mai_roo.view.rest.body.FoodPriceListReq;
import com.goo.mai_roo.view.rest.body.FoodReq;
import com.goo.mai_roo.view.rest.body.FoodSizeReq;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FoodRepo implements FoodDAO {

    @Autowired
    private EntityManager em;

    public FoodRepo(EntityManager em){
        this.em = em;
    }

    @Transactional
    public void insert(Food food){
        em.persist(food);
    }

    @Transactional
    public void insertPrice(FoodPrice foodPrice){ em.persist(foodPrice); }

    @Transactional
    public void insertSize(FoodSize foodSize){ em.persist(foodSize); }

    @Transactional
    public void update(Food food){
        em.merge(food);
    }

    @Transactional
    public void updatePrice(FoodPrice foodPrice){ em.merge(foodPrice); }

    @Transactional
    public void updateSize(FoodSize foodSize){ em.merge(foodSize); }

    @Transactional
    public void delete(String id){
        Food foodTarget = findById(id);
        em.remove(foodTarget);
    }

    @Transactional
    public void deletePrice(Long id){
        FoodPrice foodPrice = findPriceById(id);
        em.remove(foodPrice);
    }

    @Transactional
    public void deleteSize(Long id){
        FoodSize foodSize = findSizeById(id);
        em.remove(foodSize);
    }

    public Food findById(String id){
        return em.find(Food.class , id);
    }
    public FoodPrice findPriceById(Long id){
        return em.find(FoodPrice.class, id);
    }
    public FoodSize findSizeById(Long id){
        return em.find(FoodSize.class, id);
    }

    public List<FoodList> getFoodListBySql() throws Exception{
        List<FoodList> resultList = new ArrayList<>();
        try{
            String sql = """
            SELECT a.food_id,
                   a.food_name,
                   a.food_ispopular,
                   a.food_img,
                   b.food_price_price,
                   b.food_price_size_id,
                   c.size_name
            FROM food a
            LEFT JOIN food_price b ON a.food_id = b.food_price_food_id
            LEFT JOIN food_size c ON b.food_price_size_id = c.id
        """;

            List<Object[]> rows = em.createNativeQuery(sql).getResultList();

            for (Object[] r : rows) {
                resultList.add(new FoodList(
                        (String) r[0],
                        (String) r[1],
                        (Character) r[2],
                        (String) r[3],
                        r[4] == null ? null : ((Number) r[4]).intValue(),
                        r[5] == null ? null : ((Number) r[5]).intValue(),
                        (String) r[6]
                ));
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return resultList;
    }

    public List<FoodPriceList> getFoodPriceListBySql() throws Exception{
        List<FoodPriceList> resultList = new ArrayList<>();
        try{
            StringBuilder sb = new StringBuilder();
            sb.append(" SELECT b.food_price_id, b.food_price_food_id, b.food_price_size_id, b.food_price_price ");
            sb.append(" a.food_name, c.size_name ");
            sb.append(" FROM food_price b");
            sb.append(" LEFT JOIN food a ON b.food_price_food_id = a.food_id ");
            sb.append(" LEFT JOIN food_size c ON b.food_price_size_id = c.id");

            List<Object[]> rows = em.createNativeQuery(sb.toString()).getResultList();

            for (Object[] r : rows) {
                resultList.add(new FoodPriceList(
                        (Long) r[0],
                        (String) r[1],
                        (Integer) r[2],
                        (Integer) r[3],
                        (String) r[4],
                        (String) r[5]
                ));
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return resultList;
    }

    public List<FoodSize> getFoodSizeListBySql() throws Exception{
        List<FoodSize> resultList = new ArrayList<>();
        try{
            StringBuilder sb = new StringBuilder();
            sb.append(" SELECT c.* FROM food_size c");
            Query query = em.createNativeQuery(sb.toString());
            resultList = query.getResultList();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return resultList;
    }

    public List<FoodList> findFoodListbySQL(FoodListReq req) throws Exception{
        List<FoodList> resultList = new ArrayList<>();
        try{
            StringBuilder sb = new StringBuilder();
            sb.append(" SELECT a.food_id, a.food_name, a.food_ispopular, a.food_img, ");
            sb.append(" b.food_price_price, b.food_price_size_id, ");
            sb.append(" c.size_name ");
            sb.append(" FROM food a ");
            sb.append(" LEFT JOIN food_price b ON a.food_id = b.food_price_food_id ");
            sb.append(" LEFT JOIN food_size c ON b.food_price_size_id = c.id ");
            sb.append(" WHERE 1=1 ");

            if(req.getFoodId() != null){
                sb.append(" AND a.food_id = '" + req.getFoodId() + "' ");
            }
            if(req.getFoodName() != null){
                sb.append(" AND a.food_name LIKE '%" + req.getFoodName() + "%' ");
            }
            if((req.getFoodMinPrice() != null) && (req.getFoodMaxPrice() != null)){
                sb.append(" AND b.food_price_price BETWEEN " + req.getFoodMinPrice() + " AND " + req.getFoodMaxPrice() + " " );
            }
            if(req.getFoodIsPopular() != null){
                sb.append(" AND a.isPopular = '" + req.getFoodIsPopular() + "' ");
            }
            if(req.getFoodSize() != null){
                sb.append(" AND b.food_price_size_id = '" + req.getFoodSize() + "' ");
            }

            Query query = em.createNativeQuery(sb.toString());
            List<Object[]> rows = query.getResultList();

            for (Object[] r : rows) {
                resultList.add(new FoodList(
                        (String) r[0],
                        (String) r[1],
                        (Character) r[2],
                        (String) r[3],
                        r[4] == null ? null : ((Number) r[4]).intValue(),
                        r[5] == null ? null : ((Number) r[5]).intValue(),
                        (String) r[6]
                ));
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return resultList;
    }

    public List<FoodPriceList> findFoodPriceListbySQL(FoodPriceListReq req) throws Exception{
        List<FoodPriceList> resultList = new ArrayList<>();
        try{
            StringBuilder sb = new StringBuilder();
            sb.append(" SELECT b.food_price_id, b.food_price_food_id, b.food_price_size_id, b.food_price_price ");
            sb.append(" a.food_name, c.size_name ");
            sb.append(" FROM food_price b");
            sb.append(" LEFT JOIN food a ON b.food_price_food_id = a.food_id ");
            sb.append(" LEFT JOIN food_size c ON b.food_price_size_id = c.id ");
            sb.append(" WHERE 1=1 ");
            if(req.getData().getId() != null){
                sb.append(" AND b.food_price_id = " + req.getData().getId() + " ");
            }
            if(req.getData().getFoodId() != null){
                sb.append(" AND b.food_price_food_id = '" + req.getData().getFoodId() + "' ");
            }
            if(req.getData().getSizeId() != null){
                sb.append(" AND b.food_price_size_id = " + req.getData().getSizeId() + " ");
            }
            if((req.getData().getFoodMinPrice() != null) && (req.getData().getFoodMaxPrice() != null)){
                sb.append(" AND b.food_price_price BETWEEN " + req.getData().getFoodMinPrice() + " AND " + req.getData().getFoodMaxPrice() + " " );
            }
            if(req.getData().getFoodName() != null){
                sb.append(" AND a.food_name = '" + req.getData().getFoodName() + "' ");
            }
            if(req.getData().getSizeName() != null){
                sb.append(" AND c.size_name = '" + req.getData().getSizeName() + "' ");
            }

            List<Object[]> rows = em.createNativeQuery(sb.toString()).getResultList();

            for (Object[] r : rows) {
                resultList.add(new FoodPriceList(
                        (Long) r[0],
                        (String) r[1],
                        (Integer) r[2],
                        (Integer) r[3],
                        (String) r[4],
                        (String) r[5]
                ));
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return resultList;
    }

    public FoodSize findFoodSizebySQL(FoodSizeReq req) throws Exception{
        FoodSize result = null;
        try{
            StringBuilder sb = new StringBuilder();
            sb.append(" SELECT c.* FROM food_size c ");
            sb.append(" WHERE 1=1 ");
            if(req.getData().getId() != null){
                sb.append(" AND c.id = " + req.getData().getId() + " ");
            }
            if(req.getData().getSizeName() != null){
                sb.append(" AND c.size_name = '" + req.getData().getSizeName() + "' ");
            }

            Query query = em.createNativeQuery(sb.toString());
            List<FoodSize> resultList = query.getResultList();

            if(!resultList.isEmpty()){
                result = resultList.get(0);
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return result;
    }

}
