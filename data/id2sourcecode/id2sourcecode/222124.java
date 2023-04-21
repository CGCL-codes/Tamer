    protected void Inventory(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        try {
            Class.forName(drv);
            Connection con = DriverManager.getConnection(dbsource, "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from item");
            out.println("<html><head><title>JDBC Testing using SERVLET</title></head>" + "<body background bgcolor=gray>");
            while (rs.next()) {
                out.println("Item Code: " + rs.getString(1) + "<br/>");
                out.println("Item Name: " + rs.getString(2) + "<br/>");
                out.println("Item Detail #: " + rs.getString(3) + "<br/>");
                out.println("Category Code: " + rs.getString(4) + "<br/>");
                out.println("Quantity: " + rs.getString(5) + "<br/>");
                out.println("<br/>");
            }
            out.print("</body></html>");
        } catch (Exception e) {
            out.println("<html><body background color=red>" + e + "</body></html>");
        }
    }
