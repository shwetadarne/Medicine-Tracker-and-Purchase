package com.cdac.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdac.dto.LoginData;
import com.cdac.dto.PharmasMedicine;
import com.cdac.dto.OrderData;
import com.cdac.entity.Customer;
import com.cdac.entity.Medicine;
import com.cdac.entity.Order;
import com.cdac.entity.Pharmacist;
import com.cdac.interfaces.CustomerServiceInterface;
import com.cdac.repository.CustomerRepository;
import com.cdac.repository.GenericRepository;

@Service
@Transactional
public class CustomersService implements CustomerServiceInterface{
	@Autowired
	GenericRepository repo;
	@Autowired
	CustomerRepository custRepo;
	@Autowired
	EmailSenderService emailService;
	
	public Customer registerCustomer(Customer cust) {
		return (Customer) repo.save(cust);
	}
	
	public Customer loginCustomer(LoginData loginData) {
		String email = loginData.getEmailId();
		String pass = loginData.getPassword();
		
		return custRepo.fetchCustomerByEmailAndPass(email,pass);
	}
	
	public List<Pharmacist> searchPharmacists(String medName){
		return custRepo.fetchAllPharmacistsByMedName(medName);
	}
	
	public Medicine viewMedicine(PharmasMedicine pharmasMed) {
		int pharmacistId = pharmasMed.getPharmacistId();
		String medName = pharmasMed.getMedName();
		
		return custRepo.fetchMedByPharmaIdAndMedName(pharmacistId,medName);
	}
	
	public Order orderMedicine(OrderData orderData) {
		Order order = new Order();
		order.setOrderDate(LocalDate.now());
		order.setOrderQuantity(orderData.getOrderQuantity());
		order.setOrderPrice(orderData.getMedicine().getMedCost().multiply(new BigDecimal(orderData.getOrderQuantity())));
		order.setMedicine(orderData.getMedicine());
		order.setCustomer(orderData.getCustomer());
	
		Order ordr = (Order) repo.save(order);

		orderData.getDelivery().setOrder(ordr);
	
		repo.save(orderData.getDelivery());
		
		String toEmail = ordr.getCustomer().getCustEmailId();
		String subject = "Your MedTracker Order Confirmation";
		String body = "Order Confirmation" + "\n" 
				+ "Order Id : " + ordr.getOrderId() + "\n\n" 
				+ "Hello " + ordr.getCustomer().getCustName() + ",\n" 
				+ "Thank you for your order. We will deliver your order shortly." + "\n\n" 
				+ "Order Id : " + ordr.getOrderId() + "\n" 
				+ "Order Item : " + ordr.getMedicine().getMedName() + "\n"
				+ "Order Quantity : " + ordr.getOrderQuantity() + "\n"
				+ "Order Total : Rs. " + ordr.getOrderPrice() + "\n" 
				+ "Payment Pending : Rs. " + ordr.getOrderPrice() + "\n\n"
				+ "Delivery Type : " + orderData.getDelivery().getDeliveryType() + "\n"
				+ "Delivery Address : " + orderData.getDelivery().getDeliveryAddress() + "\n\n"
				+ "MedTracker";
		
		sendOrderConfirmation(toEmail,subject,body);
		
		return ordr;
	}
	
	public void sendOrderConfirmation(String toEmail,String subject,String body) {
		emailService.sendEmail(toEmail, subject, body);
	}
}
