    public String MD5(String in) {
        byte[] defaultbytes = in.getBytes();
        StringBuffer hex = new StringBuffer();
        try {
            MessageDigest alg = MessageDigest.getInstance("MD5");
            alg.reset();
            alg.update(defaultbytes);
            byte[] md = alg.digest();
            for (int i = 0; i < md.length; i++) {
                String h = Integer.toHexString(0xFF & md[i]);
                if (h.length() == 1) hex.append("0");
                hex.append(h);
            }
        } catch (Exception e) {
        }
        return hex.toString();
    }
