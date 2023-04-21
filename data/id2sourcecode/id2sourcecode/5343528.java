    public static String getHash(Object o) {
        try {
            MessageDigest mdAlgorithm = MessageDigest.getInstance("MD5");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            mdAlgorithm.update(baos.toByteArray());
            byte[] digest = mdAlgorithm.digest();
            StringBuffer hexString = new StringBuffer();
            for (byte aDigest : digest) {
                String x = Integer.toHexString(0xFF & aDigest);
                if (x.length() < 2) x = "0" + x;
                hexString.append(x);
            }
            return (hexString.toString());
        } catch (NoSuchAlgorithmException e) {
            return (null);
        } catch (IOException e) {
            return (null);
        }
    }
