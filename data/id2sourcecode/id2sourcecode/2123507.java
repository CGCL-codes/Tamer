    private boolean validPassword(String password) {
        byte[] expectedPass = Base64.decode(_pass);
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA");
            byte[] raw = password.getBytes("UTF-8");
            byte[] hash = md.digest(raw);
            for (int i = 0; i < expectedPass.length; i++) if (hash[i] != expectedPass[i]) return false;
            return true;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException uee) {
        }
        return false;
    }
