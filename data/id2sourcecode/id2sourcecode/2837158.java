    static SecurityToken newUser(String givenUserName, String plaintextPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            SecurityToken st = new SecurityToken();
            st.userName = givenUserName;
            st.passwordDigest = md.digest(plaintextPassword.getBytes());
            st.securityPermissions = new Permissions();
            return st;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
