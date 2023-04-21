    protected void Inventory(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        String s = null;
        try {
            Class.forName(drv);
            Connection con = DriverManager.getConnection(dbsource, "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(defaultquery);
            out.println("<html><head><title>JDBC Testing using SERVLET</title></head>" + "<body background bgcolor=gray>");
            while (rs.next()) {
                s = rs.getString(1);
                out.println("Item Code: " + s + "<br/>");
                out.println("Item Name: " + rs.getString(2) + "<br/>");
                out.println("Item Detail #: " + rs.getString(3) + "<br/>");
                out.println("Category Code: " + rs.getString(4) + "<br/>");
                out.println("Quantity: " + rs.getString(5) + "<br/>");
                out.println("<a href=\"http://localhost:8080/finals/UpdateItem?itemcnt=" + s + "\">update</a>");
                out.println("<br/>");
            }
            out.print("<form method=POST><INPUT TYPE=SUBMIT NAME=\"delete\" VALUE=\"delete\"> </form>" + "</body></html>");
        } catch (Exception e) {
            out.println("<html><body background color=red>" + e + "</body></html>");
        }
    }
