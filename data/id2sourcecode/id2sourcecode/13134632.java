    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement ucpst = null;
        PreparedStatement ucpst2 = null;
        ResultSet rs = null;
        try {
            String userLevelChoice = request.getParameter("newuserlevel");
            if (userLevelChoice.equals("student")) {
                String username = request.getParameter("username2");
                String password = request.getParameter("password2");
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
                String usercheck = "SELECT studentID, username FROM students WHERE username = ? AND password = ?";
                ucpst = conn.prepareStatement(usercheck);
                ucpst.setString(1, username);
                ucpst.setString(2, password);
                rs = ucpst.executeQuery();
                HttpSession session = request.getSession(true);
                String tempuserid = (String) session.getAttribute("userName");
                if (rs.next()) {
                    session.setAttribute("userName", rs.getString("username"));
                    session.setAttribute("userID", rs.getInt("studentID"));
                    session.setAttribute("userLevel", "student");
                    String updatequery = "UPDATE TutorialInfo SET createdBy = ?, userLevel=\"student\" WHERE createdBy = ?";
                    ucpst2 = conn.prepareStatement(updatequery);
                    ucpst2.setString(1, (String) session.getAttribute("userName"));
                    ucpst2.setString(2, tempuserid);
                    ucpst2.executeUpdate();
                    String redirectURL1 = "students.jsp";
                    response.sendRedirect(redirectURL1);
                } else {
                    String redirectURL2 = "students.jsp?error=4";
                    response.sendRedirect(redirectURL2);
                }
            } else {
                String username = request.getParameter("username2");
                String password = request.getParameter("password2");
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
                String usercheck = "SELECT teacherID, username FROM teachers WHERE username = ? AND password = ?";
                ucpst = conn.prepareStatement(usercheck);
                ucpst.setString(1, username);
                ucpst.setString(2, password);
                rs = ucpst.executeQuery();
                HttpSession session = request.getSession(true);
                String tempuserid = (String) session.getAttribute("userName");
                if (rs.next()) {
                    session.setAttribute("userName", rs.getString("username"));
                    session.setAttribute("userID", rs.getString("teacherID"));
                    session.setAttribute("userLevel", "teacher");
                    String updatequery = "UPDATE TutorialInfo SET createdBy = ?, userLevel=\"teacher\" WHERE createdBy = ?";
                    ucpst2 = conn.prepareStatement(updatequery);
                    ucpst2.setString(1, (String) session.getAttribute("userName"));
                    ucpst2.setString(2, tempuserid);
                    ucpst2.executeUpdate();
                    String redirectURL1 = "teachers.jsp";
                    response.sendRedirect(redirectURL1);
                } else {
                    String redirectURL2 = "teachers.jsp?error=4";
                    response.sendRedirect(redirectURL2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ucpst.close();
                ucpst2.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
