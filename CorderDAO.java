package com.javaservlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CorderDAO {
    private Connection connection;

    public CorderDAO() {

    }
    public static Connection openConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found.", e);
        }
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/ordermanagment", "postgres", "qualian");
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void insertOrderWithLines(Corder order, List<CorderLine> orderLines) throws SQLException {
        String insertOrderSQL = "INSERT INTO c_order (order_id, vendor_name, order_date, edd, shipping_address, is_sale, is_active, created_by, updated_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertOrderLineSQL = "INSERT INTO c_orderline (order_id, product_id, quantity, price, created_by, updated_by) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement orderStmt = connection.prepareStatement(insertOrderSQL);
             PreparedStatement orderLineStmt = connection.prepareStatement(insertOrderLineSQL)) {
            connection.setAutoCommit(false);

            orderStmt.setString(1, order.getOrderId());
            orderStmt.setString(2, order.getVendorName());
            orderStmt.setTimestamp(3, order.getOrderDate());
            orderStmt.setTimestamp(4, order.getEdd());
            orderStmt.setString(5, order.getShippingAddress());
            orderStmt.setBoolean(6, order.isSale());
            orderStmt.setBoolean(7, order.isActive());
            orderStmt.setString(8, order.getCreatedby());
            orderStmt.setString(9, order.getUpdatedby());
            orderStmt.executeUpdate();

            for (CorderLine line : orderLines) {
                orderLineStmt.setString(1, line.getOrderId());
                orderLineStmt.setString(2, line.getProductId());
                orderLineStmt.setInt(3, line.getQuantity());
                orderLineStmt.setDouble(4, line.getPrice());
                orderLineStmt.setString(5, line.getCreatedby());
                orderLineStmt.setString(6, line.getUpdatedby());
                orderLineStmt.addBatch();
            }
            orderLineStmt.executeBatch();

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
	public List<Corder> viewOrders(int offset, int recordsPerPage) {
		
		
		List<Corder> orderList = new ArrayList<>();
	    String query = "SELECT * FROM c_order LIMIT ? OFFSET ?";
	    try (Connection connection = openConnection();
	         PreparedStatement ps = connection.prepareStatement(query)) {
	        
	        ps.setInt(1, recordsPerPage);
	        ps.setInt(2, offset);
	        
	        ResultSet rs = ps.executeQuery();
	        
	        while (rs.next()) {
	            Corder order = new Corder();
	            order.setOrderId(rs.getString("c_order_Id"));
	            order.setOrderDate(rs.getTimestamp("order_date"));
	            order.setVendorName(rs.getString("Vendor"));
	            order.setEdd(rs.getTimestamp("edd"));
	            order.setShippingAddress(rs.getString("shipping_address"));
	            orderList.add(order);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return orderList;
	}
	public int getNoOfRecords() {
		String query = "SELECT COUNT(*) FROM c_order";
	    int noOfRecords = 0;
	    try (Connection conn = openConnection();
	         Statement st = conn.createStatement();
	         ResultSet rs = st.executeQuery(query)) {
	        
	        if (rs.next()) {
	            noOfRecords = rs.getInt(1);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return noOfRecords;
	}
	public void insertOrder(Corder order) throws SQLException {
		Connection conn = openConnection();
		PreparedStatement orderStmt= null;
		 try {
			 conn.setAutoCommit(false);
			 
			 orderStmt = conn.prepareStatement("INSERT INTO c_order (c_order_id, vendor, order_date, edd, shipping_address, issale, isactive, createdby, updatedby) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			 
            orderStmt.setString(1, order.getOrderId());
            orderStmt.setString(2, order.getVendorName());
            orderStmt.setTimestamp(3, order.getOrderDate());
            orderStmt.setTimestamp(4, order.getEdd());
            orderStmt.setString(5, order.getShippingAddress());
            orderStmt.setBoolean(6, true);
            orderStmt.setBoolean(7, true);
            orderStmt.setString(8, "100");
            orderStmt.setString(9, "100");
            orderStmt.executeUpdate();
            conn.commit();
			
		}catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
//	public boolean addOrder(Corder newOrder) {
//		 boolean result = false;
//		    String query = "INSERT INTO c_order ( c_order_id, vendor, order_date, edd, shipping_address) VALUES ( ?, ?, ?, ?, ?)";
//		    try (Connection connection = openConnection();
//		         PreparedStatement ps = connection.prepareStatement(query)) {
//		        
//		      
//		        ps.setString(1, newOrder.getOrderId());
//		        ps.setString(2, newOrder.getVendorName());
//		        ps.setTimestamp(3, newOrder.getOrderDate());
//		        ps.setTimestamp(4, newOrder.getEdd());
//		        ps.setString(5, newOrder.getShippingAddress());
//		        
//		        result = ps.executeUpdate() > 0;
//		    } catch (SQLException e) {
//		        e.printStackTrace();
//		    }
//		    return result;
//		}
	public void insertOrderLine(CorderLine orderLine) throws SQLException {
	    Connection conn = null;
	    PreparedStatement orderLineStmt = null;

	    try {
	        conn = openConnection(); // Ensure this returns a valid Connection
	        conn.setAutoCommit(false);

	        orderLineStmt = conn.prepareStatement("INSERT INTO c_orderline ( product, quantity, price, umo, createdby, updatedby, totalprice, c_order_id) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)");

	        // Set the parameters for the SQL query
	        
	        orderLineStmt.setString(1, orderLine.getProductId());
	        orderLineStmt.setInt(2, orderLine.getQuantity());
	        orderLineStmt.setDouble(3, orderLine.getPrice());
	        orderLineStmt.setString(4, orderLine.getUOM());
	        orderLineStmt.setString(5, orderLine.getCreatedby());
	        orderLineStmt.setString(6, orderLine.getUpdatedby());
	        orderLineStmt.setDouble(7, orderLine.getTotalPrice());
	        orderLineStmt.setString(8, orderLine.getOrderId());

	        // Execute the SQL query
	        orderLineStmt.executeUpdate();

	        // Commit the transaction
	        conn.commit();
	    } catch (SQLException e) {
	        if (conn != null) {
	            try {
	                conn.rollback(); // Rollback in case of an error
	            } catch (SQLException rollbackEx) {
	                rollbackEx.printStackTrace(); // Handle rollback exception
	            }
	        }
	        e.printStackTrace(); // Print the exception
	        throw e; // Rethrow exception to handle it at a higher level
	    } finally {
	        // Close resources in reverse order of their creation
	        if (orderLineStmt != null) {
	            try {
	                orderLineStmt.close();
	            } catch (SQLException closeEx) {
	                closeEx.printStackTrace();
	            }
	        }
	        if (conn != null) {
	            try {
	                conn.close();
	            } catch (SQLException closeEx) {
	                closeEx.printStackTrace();
	            }
	        }
	    }
	}


	}
	

