package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

/**
 * This interface is the DAO model to Seller class 
 * @author Mariana
 *
 */

public interface SellerDao {
	
	void insert(Seller seller);
	void update(Seller seller);
	void deleteById(Integer id);
	Seller findById(Integer id);
	List<Seller> findAll();
	List<Seller> findByDepartment(Department department);

}
