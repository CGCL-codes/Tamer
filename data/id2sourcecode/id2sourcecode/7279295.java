    public StringBuffer createUserKey(String username, String password) {
        StringBuffer sb = new StringBuffer();
        String rvalue = "no";
        String sql = "";
        String rusername = "";
        String ruserfullname = "";
        String rkey = "";
        String tkey = "";
        int userid = 0;
        Random rand = new Random();
        for (int i = 0; i < 5; i++) {
            int j = rand.nextInt();
            int n = 30;
            j = rand.nextInt(n + 1);
            tkey = tkey + j;
        }
        try {
            con = getConnection();
            if (con != null) {
                stmt = con.createStatement();
                Statement stmt1 = con.createStatement();
                sql = "SELECT userid, firstname, lastname FROM usdms_users WHERE email='" + username + "' AND password = '" + password + "'";
                rs = stmt.executeQuery(sql);
                String key1 = "";
                if (rs.next()) {
                    rusername = username;
                    ruserfullname = rs.getString("firstname") + " " + rs.getString("lastname");
                    userid = rs.getInt("userid");
                    sql = "SELECT userid FROM usdms_user_keys WHERE userid =" + userid;
                    rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        rkey = tkey;
                        sql = "UPDATE usdms_user_keys SET userkey='" + rkey + "' WHERE userid =" + userid;
                    } else {
                        rkey = tkey;
                        sql = "INSERT INTO usdms_user_keys VALUES (" + userid + ",'" + rkey + "')";
                    }
                    stmt1.executeUpdate(sql);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (rkey.length() > 0) rvalue = "yes";
        sb.append("<username>");
        sb.append(rusername);
        sb.append("</username>");
        sb.append("<key>");
        sb.append(rkey);
        sb.append("</key>");
        sb.append("<rvalue>");
        sb.append(rvalue);
        sb.append("</rvalue>");
        sb.append("<usernamefullname>");
        sb.append(ruserfullname);
        sb.append("</usernamefullname>");
        return sb;
    }
