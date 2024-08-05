package com.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaservlet.Corder;
import com.javaservlet.CorderDAO;

@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;    
    private CorderDAO corderDAO = new CorderDAO();
	private String orderDateStr;

    public OrderServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = 1;
        int recordsPerPage = 10;

        if (request.getParameter("page") != null) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        List<Corder> list = corderDAO.viewOrders((page - 1) * recordsPerPage, recordsPerPage);
        int noOfRecords = corderDAO.getNoOfRecords();
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        pw.print("<html>"
            + "<head>"
            + "<title>Order Form</title>"
            + "<link rel=\"stylesheet\" href=\"order1.css\">"
            + "</head>"
            + "<body>"
            + "<div id=\"title\">"
            + "<h1 id=\"topheaderindex\"> Purchase Orders</h1>"
            + "</div>"
            + "<button class=\"add-vendor\" alt=\"add-vendor\">"
            + "<img src=\"add.png\" onclick=\"pop()\" alt=\"Add\">"
            + "</button>"
            + "<div id=\"maintable\">"
            + "<table id=\"masterTable\">"
            + "<thead>"
            + "<tr>"
            + "<th style=\"width: 100px;\">S.No</th>"
            + "<th style=\"width: 150px;\">Order Id</th>"
            + "<th style=\"width: 150px;\">Vendor</th>"
            + "<th style=\"width: 150px;\">Order Date</th>"
            + "<th style=\"width: 150px;\">EDD</th>"
            + "<th style=\"width: 150px;\">Shipping Address</th>"
            + "<th style=\"width: 120px;\">Total Amount</th>"
            + "<th style=\"width: 175px;\">Actions</th>"
            + "</tr>"
            + "</thead>");
       

        pw.print("<tbody id='masterTableBody'>");
        int serialNo = (page - 1) * recordsPerPage + 1;
        for (Corder order : list) {
            pw.print("<tr>"
                + "<td>" + serialNo++ + "</td>"
                + "<td>" + order.getOrderId() + "</td>"
                + "<td>" + order.getVendorName() + "</td>"
                + "<td>" + order.getOrderDate() + "</td>"
                + "<td>" + order.getEdd() + "</td>"
                + "<td>" + order.getShippingAddress() + "</td>"
                + "<td>" + order.getTotalAmount() + "</td>"
                + "<td>"
                +"<img src='edit.png' onclick='editMasterTableRow(this)' class='editbutton'>"
                +"<img src='delete.png' onclick='deleteMasterTableRow(this)' class='editbutton'>"
                +"<img src='view.png' onclick='viewMasterTableRow(this)' class='editbutton'>"
                +"</td>"
                + "</tr>");
        }

        pw.print("</tbody>"
            + "</table>");
        

        
        pw.print("<div id=\"pop\">\n"
        		+ "        <div class=\"container\">\n"
        		+ "            <h1 id=\"headerindex\">PURCHASE ORDER</h1>\n"
        		+ "            <div id=\"upper\">\n"
        		+ "                <form id=\"orderForm\" action=\"OrderHandler\" method=\"post\">\n"
        		+ "                    <table class=\"orderTable\">\n"
        		+ "                        <tr>\n"
        		+ "                            <td><label for=\"orderId\">Order Id</label></td>\n"
        		+ "                            <td><input type=\"text\" id=\"orderId\" name=\"orderId\" class=\"inputField\" required></td>\n"
        		+ "                            <td><label for=\"orderDate\">Order Date</label></td>\n"
        		+ "                            <td><input type=\"date\" id=\"orderDate\" name=\"orderDate\" class=\"inputField\" required></td>\n"
        		+ "                        </tr>\n"
        		+ "                        <tr>\n"
        		+ "                            <td><label for=\"vendorName\">Vendor</label></td>\n"
        		+ "                            <td>\n"
        		+ "                                <select id=\"vendorName\" name=\"vendorName\" class=\"inputField\" required>\n"
        		+ "                                    <option value=\"\" disabled selected>Select Vendor</option>\n"
        		+ "                                    <option value=\"vendor1\">Vendor 1</option>\n"
        		+ "                                    <option value=\"vendor2\">Vendor 2</option>\n"
        		+ "                                </select>\n"
        		+ "                            </td>\n"
        		+ "                            <td><label for=\"expectedDeliveryDate\">EDD</label></td>\n"
        		+ "                            <td><input type=\"date\" id=\"expectedDeliveryDate\" name=\"expectedDeliveryDate\" class=\"inputField\" required></td>\n"
        		+ "                        </tr>\n"
        		+ "                        <tr>\n"
        		+ "                            <td><label for=\"shippingAddress\">Shipping Address</label></td>\n"
        		+ "                            <td colspan=\"3\"><textarea id=\"shippingAddress\" rows=\"2\" name=\"shippingAddress\" class=\"inputField\"></textarea></td>\n"
        		+ "                        </tr>\n"
        		+ "                    </table>\n"
        		+ "                </form>\n"
        		+ "            </div>\n"
        		+ "            <fieldset class=\"field\">\n"
        		+ "                <legend><h3 id=\"line-item-title\">Add Line Items</h3></legend>\n"
        		+ "                <div class=\"orderline-section\">\n"
        		+ "                    <form id=\"orderlineForm\">\n"
        		+ "                        <table class=\"orderlineTable\">\n"
        		+ "                            <tr>\n"
        		+ "                                <td><label for=\"productName\">Product</label></td>\n"
        		+ "                                <td>\n"
        		+ "                                    <select id=\"productName\" name=\"productName\" onchange=\"changes()\" required>\n"
        		+ "                                        <option value=\"\" disabled selected>Select Product</option>\n"
        		+ "                                        <option value=\"Egg\">Egg</option>\n"
        		+ "                                        <option value=\"Milk\">Milk</option>\n"
        		+ "                                        <option value=\"biscuit\">Biscuit</option>\n"
        		+ "                                        <option value=\"water\">Water</option>\n"
        		+ "                                        <option value=\"oil\">Oil</option>\n"
        		+ "                                        <option value=\"rice\">Rice</option>\n"
        		+ "                                    </select>\n"
        		+ "                                </td>\n"
        		+ "                                <td><label for=\"uom\">UOM</label></td>\n"
        		+ "                                <td><input type=\"text\" id=\"uom\" readonly></td>\n"
        		+ "                            </tr>\n"
        		+ "                            <tr>\n"
        		+ "                                <td><label for=\"quantity\">Quantity</label></td>\n"
        		+ "                                <td><input type=\"number\" id=\"quantity\" name=\"quantity\" class=\"inputField\" required></td>\n"
        		+ "                                <td><label for=\"unitprice\">Price</label></td>\n"
        		+ "                                <td><input type=\"number\" id=\"unitprice\" name=\"unitprice\" class=\"inputField\" required></td>\n"
        		+ "                            </tr>\n"
        		+ "                            <tr>\n"
        		+ "                                <td colspan=\"5\" class=\"buttons\">\n"
        		+ "                                    <button id=\"addButton\" type=\"button\" onclick=\"addItem()\">Add</button>\n"
        		+ "                                    <button id=\"cancelButton\" type=\"button\" onclick=\"closePop()\">Cancel</button>\n"
        		+ "                                </td>\n"
        		+ "                            </tr>\n"
        		+ "                        </table>\n"
        		+ "                    </form>\n"
        		+"						<table class=\"lineTable\">\n"
        		+ "                        <thead>\n"
        		+ "                            <tr>\n"
        		+ "                                <th>S.No</th>\n"
        		+ "                                <th>Product</th>\n"
        		+ "                                <th>Quantity</th>\n"
        		+ "                                <th>UOM</th>\n"
        		+ "                                <th>Price</th>\n"
        		+ "                                <th>Total Price</th>\n"
        		+ "                                <th>Action</th>\n"
        		+ "                            </tr>\n"
        		+ "                        </thead>\n"
        		+ "                        <tbody id=\"orderLineTable\">\n"
        		+ "                        </tbody>\n"
        		+ "                    </table>   \n"
        		+ "                </div>\n"
        		+ "            </fieldset>"
               
        		+ "            <div id=\"but\" class=\"buttons\">\n"
        		+ "            <button id=\"submitButton\" type=\"submit\" onclick=\"handleSubmit()\">Submit</button>\n"
        		+"				<button type=\"button\" onclick=\"off()\">Cancel</button>"
        		+ "        </div>\n"
        		+ "    </div>"
        		+ "    </div>");

        pw.print( "<script src='order1.js'></script>\n"
        		+ "<script src=\"https://code.jquery.com/jquery-3.6.0.min.js\"></script>");
        		
        		pw.print("<div class='pagination'>");
        		 if (page > 1) {
        	            pw.print("<a href=\"OrderServlet?page=" + (page - 1) + "\">Previous</a> ");
        	        }

        	        for (int i = 1; i <= noOfPages; i++) {
        	            if (i == page) {
        	                pw.print("<span>" + i + "</span> ");
        	            } else {
        	                pw.print("<a href=\"OrderServlet?page=" + i + "\">" + i + "</a> ");
        	            }
        	        }

        	        if (page < noOfPages) {
        	            pw.print("<a href=\"OrderServlet?page=" + (page + 1) + "\">Next</a>");
        	        }
        	        

        	        pw.print("</div>");
        		pw.print("</body></html>");
        		 pw.print("<div class=\"fullview\" id=\"fullview\">\n"
        		 		+ "    <div class=\"fullviewpage\">\n"
        		 		+ "        <div>\n"
        		 		+ "        <span class=\"closebtn\" onclick=\"closeFullView()\">&times;</span>\n"
        		 		+ "        <table id=\"information\">\n"
        		 		+ "            <tr>\n"
        		 		+ "                <th colspan=\"4\" class=\"heading\">Purchase Order</th>\n"
        		 		+ "            </tr>\n"
        		 		+ "            <tr>\n"
        		 		+ "                <th colspan=\"2\" class=\"vendor\">Vendor Info</th>\n"
        		 		+ "                <th colspan=\"2\" class=\"order\">Order Info</th>\n"
        		 		+ "            </tr>\n"
        		 		+ "            <tr>\n"
        		 		+ "                <td class=\"vendordata\" id=\"viname\">Vendor Name : </td>\n"
        		 		+ "				   <td id=\"vendorName\"></td>"
        		 		+ "                <td class=\"orderdata\">Order Id : </td>\n"
        		 		+ "                <td class=\"orderdata2\" id=\"orderId\"></td>\n"
        		 		+ "            </tr>\n"
        		 		+ "            <tr>\n"
        		 		+ "				   <td colspan=\"2\">"
        		 		+ "                <td class=\"orderdata\">Order Date : </td>\n"
        		 		+ "                <td class=\"orderdata2\" id=\"orderDate\"></td>\n"
        		 		+ "            </tr>\n"
        		 		+ "            <tr>\n"
        		 		+ "				   <td colspan=\"2\">"
        		 		+ "                <td class=\"orderdata\">EDD : </td>\n"
        		 		+ "                <td class=\"orderdata2\" id=\"expectedDeliveryDate\"></td>\n"
        		 		+ "            </tr>\n"
        		 		+ "        </table>\n"
        		 		+ "        \n"
        		 		+ "        <table id=\"fullviewTable\">\n"
        		 		+ "            <thead>\n"
        		 		+ "                <caption>Line Items</caption>\n"
        		 		+ "                <tr>\n"
        		 		+ "                    <th style=\"width: 80px;\">S.no</th>\n"
        		 		+ "                    <th style=\"width: 400px;\">Product</th>\n"
        		 		+ "                    <th>Price</th>\n"
        		 		+ "                    <th>Quantity</th>\n"
        		 		+ "                    <th>Amount</th>\n"
        		 		+ "                </tr>\n"
        		 		+ "            </thead>\n"
        		 		+ "            <tbody id=\"orderLineTable\">\n"
        		 		+ "\n"
        		 		+ "            </tbody>\n"
        		 		+ "        </table>\n"
        		 		+ "    </div>\n"
        		 		+ "</div>");
    }

//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String orderId = request.getParameter("orderId");
//        String orderDateStr = request.getParameter("orderDate"); // Renamed for clarity
//        String vendorName = request.getParameter("vendorName");
//        String expectedDeliveryDateStr = request.getParameter("expectedDeliveryDate"); // Renamed for clarity
//        String shippingAddress = request.getParameter("shippingAddress");
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Timestamp orderTimestamp = null;
//        Timestamp eddTimestamp = null;
//
//        try {
//            // Parse the order date string into a Timestamp
//            java.util.Date orderDate = dateFormat.parse(orderDateStr);
//            orderTimestamp = new Timestamp(orderDate.getTime());
//
//            // Parse the expected delivery date string into a Timestamp
//            java.util.Date expectedDeliveryDate = dateFormat.parse(expectedDeliveryDateStr);
//            eddTimestamp = new Timestamp(expectedDeliveryDate.getTime());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date format");
//            return;
//        }
//
//        // Create a new order object and set its properties
//        Corder newOrder = new Corder();
//        newOrder.setOrderId(orderId);
//        newOrder.setOrderDate(orderTimestamp);
//        newOrder.setVendorName(vendorName);
//        newOrder.setEdd(eddTimestamp);
//        newOrder.setShippingAddress(shippingAddress);
//
//        // Insert the order into the database
//        CorderDAO corderDAO = new CorderDAO(); // Make sure to initialize or obtain your DAO properly
//        boolean isInserted = corderDAO.addOrder(newOrder);
//
//        // Respond based on the result of the insertion
//        if (isInserted) {
//            response.sendRedirect("OrderServlet"); // Redirect to a confirmation page or another servlet
//        } else {
//            response.getWriter().print("Error inserting order.");
//        }
//    }

}
