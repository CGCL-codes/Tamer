    @SuppressWarnings("unchecked")
    public boolean isValid(HttpServletRequest req, Recordset inputParams, HashMap attribs) throws Throwable {
        if (inputParams.isNull("userlogin")) inputParams.setValue("userlogin", getSession().getAttribute("dinamica.userlogin"));
        if (inputParams.isNull("userlogin")) inputParams.setValue("userlogin", getUserName());
        String userid = inputParams.getString("userlogin");
        String password = inputParams.getString("passwd");
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        byte[] b = (userid + ":" + password).getBytes();
        byte[] hash = md.digest(b);
        String pwd = Base64.encodeToString(hash, true);
        inputParams.setValue("passwd", pwd);
        return true;
    }
