    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement pst = null;
        String password = null;
        String firstName = null;
        String lastName = null;
        String school = null;
        String teacherID;
        String sql = null;
        String redirectURL1 = null;
        HttpSession session = request.getSession(true);
        try {
            firstName = request.getParameter("firstName3");
            lastName = request.getParameter("lastName3");
            school = request.getParameter("school3");
            teacherID = (String) session.getAttribute("userID");
            password = request.getParameter("password3");
            byte[] defaultBytes = password.getBytes();
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            messageDigest.toString();
            password = hexString + "";
            conn = DBConnection.GetConnection();
            if (!password.equals("d41d8cd98f0b24e980998ecf8427e")) {
                sql = "UPDATE teachers SET password=?, firstName=?, lastName=?, school=? WHERE teacherID=?";
                pst = conn.prepareStatement(sql);
                pst.setString(1, password);
                pst.setString(2, firstName);
                pst.setString(3, lastName);
                pst.setString(4, school);
                pst.setString(5, teacherID);
            } else {
                sql = "UPDATE teachers SET firstName=?, lastName=?, school=? WHERE teacherID=?";
                pst = conn.prepareStatement(sql);
                pst.setString(1, firstName);
                pst.setString(2, lastName);
                pst.setString(3, school);
                pst.setString(4, teacherID);
            }
            pst.executeUpdate();
            conn.close();
            redirectURL1 = "teachers.jsp?page=accountinfo&success=1";
            response.sendRedirect(redirectURL1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
