    public String getUserName(String name, String role) {
        String fin_name = null;
        if (role != null) name = name + role;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] msg = name.getBytes();
            md.update(msg);
            byte[] aMessageDigest = md.digest();
            base32 b32 = new base32();
            fin_name = b32.encode(aMessageDigest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fin_name;
    }
