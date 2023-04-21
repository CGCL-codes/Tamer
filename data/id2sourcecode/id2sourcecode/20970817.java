    private byte[] getClassDigest(String name) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            Class<?> cl = Class.forName(name);
            ClassUtil.getClassDigest(cl, md5);
            return md5.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new UnexpectedException(e);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
