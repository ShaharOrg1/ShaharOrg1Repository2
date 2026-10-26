public class LoginValidator extends HttpServlet {


protected void processRequest(HttpServletRequest request, HttpServletResponse response)
throws ServletException, IOException {


String user=request.getParameter("username").trim();
String pass=request.getParameter("password").trim();
try
{
Connection con=new DBConnect().connect(getServletContext().getRealPath("/WEB-INF/config.properties"));
if(con!=null && !con.isClosed())
{
ResultSet rs=null;
PreparedStatement stmt = con.prepareStatement("select * from users where username=? and password=?");
stmt.setString(1, user);
stmt.setString(2, pass);
rs=stmt.executeQuery();
if(rs != null && rs.next()){
HttpSession session=request.getSession();
session.setAttribute("isLoggedIn", "1");
session.setAttribute("userid", rs.getString("id"));
session.setAttribute("user", rs.getString("username"));
session.setAttribute("avatar", rs.getString("avatar"));
Cookie privilege=new Cookie("privilege","user");
response.addCookie(privilege);
if(request.getParameter("RememberMe")!=null)
{
Cookie username=new Cookie("username",user);
Cookie password=new Cookie("password",pass);
response.addCookie(username);
response.addCookie(password);
}
response.sendRedirect(response.encodeURL("ForwardMe?location=/index.jsp"));
}
else
{
response.sendRedirect("ForwardMe?location=/login.jsp&err=Invalid Username or Password");
}

}
}
catch(Exception ex)
{
response.sendRedirect("login.jsp?err=something went wrong");
}

}

}