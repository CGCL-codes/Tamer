    public static String hashSHA1PublicKey(PublicKey pub) {
        String hashSHA1 = null;
        try {
            MessageDigest hash = MessageDigest.getInstance("SHA");
            hash.update(pub.getEncoded());
            ByteArrayInputStream bis = new ByteArrayInputStream(Base64.encode(hash.digest()));
            InputStreamReader irr = new InputStreamReader(bis);
            BufferedReader r = new BufferedReader(irr);
            StringBuffer buff = new StringBuffer();
            String line;
            while ((line = r.readLine()) != null) {
                buff.append(line + "\n");
            }
            hashSHA1 = buff.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashSHA1;
    }
