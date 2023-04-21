    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement pst = null;
        String password = null;
        String firstName = null;
        String lastName = null;
        String school = null;
        int studentID;
        String sql = null;
        String redirectURL1 = null;
        HttpSession session = request.getSession(true);
        try {
            firstName = request.getParameter("firstName3");
            lastName = request.getParameter("lastName3");
            school = request.getParameter("school3");
            studentID = (Integer) session.getAttribute("userID");
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
                sql = "UPDATE students SET password=?, firstName=?, lastName=?, school=? WHERE studentID=?";
                pst = conn.prepareStatement(sql);
                pst.setString(1, password);
                pst.setString(2, firstName);
                pst.setString(3, lastName);
                pst.setString(4, school);
                pst.setInt(5, studentID);
            } else {
                sql = "UPDATE students SET firstName=?, lastName=?, school=? WHERE studentID=?";
                pst = conn.prepareStatement(sql);
                pst.setString(1, firstName);
                pst.setString(2, lastName);
                pst.setString(3, school);
                pst.setInt(4, studentID);
            }
            pst.executeUpdate();
            conn.close();
            redirectURL1 = "students.jsp?page=accountinfo&success=1";
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
