    public int checkPasswordExists(String username, String password, Statement state) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance("sha1");
        byte[] pass = password.getBytes();
        try {
            ResultSet passwd = state.executeQuery("SELECT password,uid FROM rides.authentication where username='" + username + "';");
            passwd.next();
            String encpasswd = passwd.getString("password");
            log.appendLog("Found password: " + encpasswd);
            sha.update(pass);
            byte[] encpass = sha.digest();
            String hash = Base64.encodeBytes(encpass);
            log.appendLog("Compares to '" + hash + "'?");
            System.out.println(" Compare two passwords: (" + hash + ") (" + encpasswd + ")");
            if (encpasswd.equals(hash)) {
                int uid = passwd.getInt("uid");
                log.appendLog("Password MATCH. return uid " + uid);
                return uid;
            }
            log.appendLog("Passwords did NOT match. Return -2");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -2;
    }
