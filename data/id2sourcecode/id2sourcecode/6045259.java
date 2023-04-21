    protected Recordset loginDB(Recordset inputParams) throws Throwable {
        String userid = inputParams.getString("userlogin");
        String password = inputParams.getString("passwd");
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        byte[] b = (userid + ":" + password).getBytes();
        byte[] hash = md.digest(b);
        String pwd = Base64.encodeToString(hash, true);
        inputParams.setValue("passwd", pwd);
        String sqlLogin = getSQL(getResource("login.sql"), inputParams);
        Recordset rs = getDb().get(sqlLogin);
        return rs;
    }
