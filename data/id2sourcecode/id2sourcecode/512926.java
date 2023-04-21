    public int alterUser(int uid, String nuser, String passwd, long phone, String first, String last, String birth, String email, Statement state) {
        try {
            int one = checksUser(nuser, email, state);
            System.out.println("Password: " + passwd);
            System.out.println("one = " + one);
            log.appendLog("Code = " + one);
            userError(one);
            if (one == 1) {
                MessageDigest sha = MessageDigest.getInstance("sha1");
                byte[] bytepasswd = passwd.getBytes();
                sha.update(bytepasswd);
                byte[] encpass = sha.digest();
                String hash = Base64.encodeBytes(encpass);
                log.appendLog("INSERT INTO rides.authentication (username, password, phone, first, last, birth, email) VALUES ('" + nuser + "', '" + hash + "'," + phone + ", '" + first + "', '" + last + "', '" + birth + "', '" + email + "');");
                state.executeUpdate("INSERT INTO rides.authentication (username, password, phone, first, last, birth, email) VALUES ('" + nuser + "', '" + hash + "'," + phone + ", '" + first + "', '" + last + "', '" + birth + "', '" + email + "');");
                log.appendLog("SELECT uid FROM rides.authentication where username='" + nuser + "';");
                ResultSet userid = state.executeQuery("SELECT uid FROM rides.authentication where username='" + nuser + "';");
                userid.next();
                uid = userid.getInt("uid");
                log.appendLog("UID = " + uid);
                return uid;
            }
            return one;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            log.appendLog("Error -1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
