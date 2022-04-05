package com.cdac.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdac.entity.Medicine;
import com.cdac.entity.Order;
import com.cdac.entity.Pharmacist;
import com.cdac.interfaces.PharmacistServiceInterface;
import com.cdac.repository.GenericRepository;
import com.cdac.repository.PharmacistRepository;

@Service
@Transactional
public class PharmacistsService implements PharmacistServiceInterface{
	@Autowired
	GenericRepository repo;
	@Autowired
	PharmacistRepository pharmaRepo;
	
	public Pharmacist registerPharmacist(Pharmacist pharma) {
		return (Pharmacist) repo.save(pharma);
	}
	
	public Pharmacist loginPharmacist(String email,String pass) {
		return pharmaRepo.fetchPharmacistByEmailAndPass(email,pass);
	}
	
	public Medicine addMedicine(Medicine medicine) {
		return (Medicine) repo.save(medicine);
	}
	
	public Medicine updateMedicine(Medicine medicine) {
		//Get medId through session object and set in medicine.medId
		return (Medicine) repo.save(medicine);
	}
	
	public List<Medicine> viewMedicines(int pharmacistId){
		return pharmaRepo.fetchMedicinesByPharmaId(pharmacistId);
	}
	
	public List<Order> viewOrders(int pharmacistId){
		return pharmaRepo.fetchOrdersByPharmaId(pharmacistId);
	}
}
