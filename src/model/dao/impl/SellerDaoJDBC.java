package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

/**
 * This class is responsible to implement basic DB activities
 * @author Mariana
 *
 */

public class SellerDaoJDBC implements SellerDao{
	
	// Dependência do dao com a conexão do banco
	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {  // construtor para criar a dependência.
		this.conn = conn;
	}

	@Override
	public void insert(Seller seller) {
		
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)", 
					Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, seller.getName());
			ps.setString(2, seller.getEmail());
			ps.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
			ps.setDouble(4, seller.getBaseSalary());
			ps.setInt(5, seller.getDepartment().getId());
			
			int rowsAffected = ps.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					seller.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No row affected!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
		}
		
		
	}

	@Override
	public void update(Seller seller) {
		
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ? ");
			
			ps.setString(1, seller.getName());
			ps.setString(2, seller.getEmail());
			ps.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
			ps.setDouble(4, seller.getBaseSalary());
			ps.setInt(5, seller.getDepartment().getId());
			ps.setInt(6, seller.getId());
			
			ps.executeUpdate();
			
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement("DELETE FROM seller "
					+ "WHERE Id = ?");
			
			ps.setInt(1, id);
			
			int rowsAffected = ps.executeUpdate();
			
			if (rowsAffected == 0) {
				throw new DbException("This is an invalid id!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
		}
	}

	@Override
	public Seller findById(Integer id) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
			
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			// Criando os objetos da OO
			if (rs.next()) {
				Department dep = instantiateDepartment(rs);				
				Seller seller = instantiateSeller(rs, dep);
				
				return seller;
			}
			return null;  // se o rs não retornar nada, o método retornará nulo.
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
			
		}
		finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}		
	}
	
	/**
	 * Método auxiliar para instanciar os vendedores
	 * @param rs
	 * @param dep
	 * @return seller
	 * @throws SQLException
	 */
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller seller = new Seller();
		seller.setId(rs.getInt("Id"));
		seller.setName(rs.getString("Name"));
		seller.setEmail(rs.getString("Email"));
		seller.setBaseSalary(rs.getDouble("BaseSalary"));
		seller.setBirthDate(rs.getDate("BirthDate"));
		seller.setDepartment(dep);
		return seller;
	}

	/**
	 * Método auxiliar para instanciar os departamentos
	 * @param rs
	 * @return dep
	 * @throws SQLException
	 */
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name");
			
			rs = ps.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			// Criando os objetos da OO
			while (rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));  // busca no map se existe um objeto com aquele id, senão retorna nulo pro objeto dep.
				
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);  // se não existir, insere no map.
				}				 
							
				Seller seller = instantiateSeller(rs, dep);
				list.add(seller);
				
			}
			return list;  
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
			
		}
		finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}		
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name");
			
			ps.setInt(1, department.getId());
			rs = ps.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			// Criando os objetos da OO
			while (rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));  // busca no map se existe um objeto com aquele id, senão retorna nulo pro objeto dep.
				
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);  // se não existir, insere no map.
				}				 
							
				Seller seller = instantiateSeller(rs, dep);
				list.add(seller);
				
			}
			return list;  
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
			
		}
		finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}		
	}
}
