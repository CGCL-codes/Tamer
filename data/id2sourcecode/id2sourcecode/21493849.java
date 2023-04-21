    private byte[] hashPassword(String password) {
        byte hashedPwd[] = null;
        try {
            hashedPwd = md.digest(password.getBytes("ASCII"));
        } catch (Exception e) {
            System.out.println("DbManager.hashPassword: " + e.getMessage());
        }
        return hashedPwd;
    }
