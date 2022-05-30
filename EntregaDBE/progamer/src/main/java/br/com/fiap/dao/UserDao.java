package br.com.fiap.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import br.com.fiap.model.Setup;
import br.com.fiap.model.User;

public class UserDao {

	EntityManagerFactory factory = 
			Persistence.createEntityManagerFactory("progamer-persistence-unit");
	EntityManager manager = factory.createEntityManager();
	//private EntityManager manager;
	
	public void create(User user) {
		manager.getTransaction().begin();
		manager.persist(user);
		manager.getTransaction().commit();
		
		manager.clear();
		
	}
	
	public void remove(User user) {
		manager.getTransaction().begin();
		manager.remove(user);
		manager.getTransaction().commit();
	}

	public void update(User user) {
		manager.getTransaction().begin();
		manager.merge(user);
		manager.getTransaction().commit();
	}
	
	public List<User> listAll() {
		TypedQuery<User> query = 
				manager.createQuery("SELECT u FROM User u", User.class);
		return query.getResultList();
	}

	public boolean exist(User user) {
		String jpql = "SELECT u FROM User u WHERE cpf=:cpf AND rg=:rg";
		TypedQuery<User> query = manager.createQuery(jpql , User.class);
		query.setParameter("cpf", user.getCpf());
		query.setParameter("rg", user.getRG());
		
		try {
			query.getSingleResult();
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}

}
