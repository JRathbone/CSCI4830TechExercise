import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SimpleFormSearch")
public class SimpleFormSearch extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public SimpleFormSearch() {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String keyword = request.getParameter("keyword");
      search(keyword, response);
   }
   
   void search(String keyword, HttpServletResponse response) throws IOException {
	      response.setContentType("text/html");
	      PrintWriter out = response.getWriter();
	      String title = "Justin's Sporting Goods Inventory";
	      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + //
	            "transitional//en\">\n"; //
	      out.println(docType + //
	            "<html>\n" + //
	            "<head><title>" + title + "</title></head>\n" + //
	            "<body bgcolor=\"#f5f5f5\">\n" + //
	            "<h1 align=\"center\">" + title + "</h1>\n");
	      out.println("<div style=\"\r\n"
	      		+ "    display: flex;\r\n"
	      		+ "    justify-content: center;\r\n"
	      		+ "    font-size: larger;\r\n"
	      		+ "\"><a href=/webproject/Homepage>Homepage</a> <br></div>");
	      

	      Connection connection = null;
	      PreparedStatement preparedStatement = null;
	      try {
	         DBConnection.getDBConnection(getServletContext());
	         connection = DBConnection.connection;
	         String selectSQL = "SELECT * FROM inventory WHERE name LIKE ?";
	         String searchTerm = "%" + keyword + "%";
	         preparedStatement = connection.prepareStatement(selectSQL);
	         preparedStatement.setString(1, searchTerm);
	         ResultSet rs = preparedStatement.executeQuery();
	         
	         out.println("<div style=\"\r\n"
	         		+ "    display: flex;\r\n"
	         		+ "    align-items: center;\r\n"
	         		+ "    justify-content: center;\r\n"
	         		+ "\"><table style=\"\r\n"
	         		+ "    text-align: center;\r\n"
	         		+ "\">\r\n"
	         		
	         		+ "  <tr>\r\n"
	         		+ "    <th>ID</th>\r\n"
	         		+ "    <th>NAME</th>\r\n"
	         		+ "    <th>QUANTITY</th>\r\n"
	         		+ "    <th>IMAGE</th>\r\n"
	         		+ "  </tr>");
	         
	         
	         while (rs.next()) {
	            int id = rs.getInt("id");
	            String name = rs.getString("name").trim();
	            String quantity = rs.getString("quantity").trim();
	            String url = rs.getString("imageURL").trim();
	            out.println("<tr>\r\n"
	            		+ "    <td>" + id + "</td>\r\n"
	            		+ "    <td>" + name + "</td>\r\n"
	            		+ "    <td>" + quantity + "</td>\r\n"
	            		+ "    <td>" + "<img src=" + url + " \" width=\"42\" height=\"42\"/>" + "</td>\r\n");
	            		
	        
	         }
	         out.println("</tr></table></div>");
	         out.println("</body></html>");
	         rs.close();
	         preparedStatement.close();
	         connection.close();
	      } catch (SQLException se) {
	         se.printStackTrace();
	      } catch (Exception e) {
	         e.printStackTrace();
	      } finally {
	         try {
	            if (preparedStatement != null)
	               preparedStatement.close();
	         } catch (SQLException se2) {
	         }
	         try {
	            if (connection != null)
	               connection.close();
	         } catch (SQLException se) {
	            se.printStackTrace();
	         }
	      }
	   }

   
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

}
