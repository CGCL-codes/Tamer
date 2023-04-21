    private boolean validateCurrentPass() throws NoSuchAlgorithmException, MyHibernateException {
        String currentPass = new String(passTF.getPassword());
        byte[] b = CriptUtil.digest(currentPass.getBytes(), CriptUtil.SHA);
        user = facade.loadUser(loginTF.getText(), CriptUtil.byteArrayToHexString(b));
        return (user != null) ? true : false;
    }
