package com.Servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.javaservlet.Corder;
import com.javaservlet.CorderDAO;
import com.javaservlet.CorderLine;

@WebServlet("/OrderHandler")
public class OrderHandler extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CorderDAO orderDAO = new CorderDAO();

    public OrderHandler() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        // Handle preflight request
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }


//        List<CorderLine> orderLines = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Define the date format

        try (BufferedReader reader = request.getReader()) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            // Print JSON for debugging
            System.out.println(sb.toString());

            try {
            	
            	Map<String, String> formData = new HashMap<>();
                String[] pairs = sb.toString().split("&");
                for (String pair : pairs) {
                    String[] keyValue = pair.split("=");
                    if (keyValue.length == 2) {
                        formData.put(keyValue[0], keyValue[1]);
                    }
                }
                
                JSONObject jsonObject = new JSONObject(sb.toString());

                // Extract order details
                String orderId = jsonObject.getString("orderId");
                String vendorName = jsonObject.getString("vendorName");
                String orderDateStr = jsonObject.getString("orderDate");
                String eddStr = jsonObject.getString("expectedDeliveryDate");
                String shippingAddress = jsonObject.getString("shippingAddress");

                // Parse dates
                Timestamp orderTimestamp = new Timestamp(dateFormat.parse(orderDateStr).getTime());
                Timestamp eddTimestamp = new Timestamp(dateFormat.parse(eddStr).getTime());

                // Create and populate Corder object
                Corder order = new Corder();
                order.setOrderId(orderId);
                order.setVendorName(vendorName);
                order.setOrderDate(orderTimestamp);
                order.setEdd(eddTimestamp);
                order.setShippingAddress(shippingAddress);
                order.setSale(true); // Adjust as needed
                order.setActive(true); // Adjust as needed
                order.setCreatedby("100");
                order.setUpdatedby("100");

                // Insert order
                orderDAO.insertOrder(order);

                // Initialize orderLines list
                List<CorderLine> orderLines = new ArrayList<>();

                // Process order lines
                JSONArray lineItems = jsonObject.getJSONArray("productList");
                for (int i = 0; i < lineItems.length(); i++) {
                    JSONObject lineItem = lineItems.getJSONObject(i);
                    String productId = lineItem.getString("product");
                    int quantity = lineItem.getInt("quantity");
                    String uom = lineItem.getString("uom");
                    double price = lineItem.getDouble("unitprice");
                    double tprice=lineItem.getDouble("totalPrice");

                    CorderLine orderLine = new CorderLine();
                    orderLine.setProductId(productId);
                    orderLine.setQuantity(quantity);
                    orderLine.setUOM(uom);
                    orderLine.setPrice(price);
                    orderLine.setTotalPrice(tprice);
                    orderLine.setCreatedby("100");
                    orderLine.setUpdatedby("100");
                    orderLine.setOrderId(orderId);

                    orderLines.add(orderLine);
                }

                // Insert order lines
                for (CorderLine orderLine : orderLines) {
                    orderDAO.insertOrderLine(orderLine);
                }

                // Respond with success
                response.getWriter().write("{\"status\":\"success\"}");
                response.setStatus(HttpServletResponse.SC_OK);

            } catch (JSONException | ParseException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Invalid JSON format or date parsing error\"}");
                e.printStackTrace();
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Database error: " + e.getMessage() + "\"}");
                e.printStackTrace();
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Error reading request\"}");
            e.printStackTrace();
        }
    }
}
