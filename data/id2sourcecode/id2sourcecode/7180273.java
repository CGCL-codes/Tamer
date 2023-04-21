    public SugarSoapInstance(String url, String user, String pass) {
        try {
            Sugarsoap service = new SugarsoapLocator();
            port = service.getsugarsoapPort(new java.net.URL(url));
            User_auth userAuth = new User_auth();
            userAuth.setUser_name(user);
            MessageDigest md = MessageDigest.getInstance("MD5");
            String password = getHexString(md.digest(pass.getBytes()));
            userAuth.setPassword(password);
            userAuth.setVersion("0.1");
            Set_entry_result loginRes = port.login(userAuth, "sugarsoap");
            sessionID = loginRes.getId();
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Error al conectarse al WebService Sugar: " + ex.toString());
            ex.printStackTrace();
        }
    }
