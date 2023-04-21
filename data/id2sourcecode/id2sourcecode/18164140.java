    public boolean compare(String password, String hash) {
        if (password == null || hash == null) return false;
        try {
            byte[] hashedPassword = md.digest(password.getBytes("ASCII"));
            byte[] hashedDBPassword = decode(hash);
            if (hashedPassword.length != hashedDBPassword.length) {
                return false;
            } else {
                for (int i = 0; i < hashedPassword.length; i++) {
                    if (hashedPassword[i] != hashedDBPassword[i]) {
                        return false;
                    }
                }
                System.out.println("MATCHED!");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
