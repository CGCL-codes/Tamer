    public static Connection establishConnection(String username, String password, String serverurl, String serverport, String servicename) throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Properties cp = new Properties();
        cp.put("user", "LASCS");
        cp.put("password", "LASCS");
        Connection c = DriverManager.getConnection("jdbc:oracle:thin:@" + serverurl + ":" + serverport + ":" + servicename, cp);
        String pwd = String.valueOf(password);
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(pwd.getBytes());
        pwd = Base64.encode(digest);
        String strPWD = dlookup(c, "LAUPWD", "LAUSER", "LAUSTAT='A' AND LAUID='" + username + "'");
        if (strPWD.replaceAll("\n", "").replaceAll(" ", "").equals(pwd.replaceAll("\n", "").replaceAll(" ", ""))) {
            ctrlMain.setRight(Integer.parseInt(dlookup(c, "LAURIGHT", "LAUSER", "LAUID='" + username + "'")));
            ctrlMain.setUser(username);
            return c;
        } else return null;
    }
