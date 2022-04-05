package com.cdac.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.cdac.entity.Medicine;
import com.cdac.entity.Order;
import com.cdac.entity.Pharmacist;
import com.cdac.interfaces.PharmacistRepositoryInterface;

@Repository
public class PharmacistRepository implements PharmacistRepositoryInterface{
	@PersistenceContext
	EntityManager em;
	
	public Pharmacist fetchPharmacistByEmailAndPass(String email,String pass) {
		Pharmacist pharmacist = (Pharmacist) em.createQuery("select p from Pharmacist p where pharmacistEmailId=:emailId")
			.setParameter("emailId", email).getSingleResult();
		
		if(pharmacist.getPharmacistPassword().equals(pass))
			return pharmacist;
		
		return null;
	}
	
	public List<Medicine> fetchMedicinesByPharmaId(int pharmacistId){
		return em.createQuery("select med from Medicine med where pharmacist.pharmacistId = :pid")
				.setParameter("pid", pharmacistId).getResultList();
	}
	
	public List<Order> fetchOrdersByPharmaId(int pharmacistId){
		return em.createQuery("select o from Order o where medicine in(select med from Medicine med where pharmacist.pharmacistId = :pid)")
				.setParameter("pid", pharmacistId).getResultList();
	}
}
