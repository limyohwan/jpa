package com.example;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.example.domain.Movie;
import com.example.domain.Order;
import com.example.domain.OrderItem;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx =em.getTransaction();

        tx.begin();

        try{
            Movie movie = new Movie();
            movie.setName("영화");
            movie.setPrice(100000);
            movie.setActor("tony start");
            movie.setDirector("tony antony");
            
            em.persist(movie);

            tx.commit();
        } catch(Exception e){
            tx.rollback();
        }finally{
            em.close();
        }
        
        emf.close();
    }
}
