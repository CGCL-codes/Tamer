    public String md5sum(String filename) throws Exception {
        String result = "ERROR";
        InputStream is = null;
        InputStream dis = null;
        InputStream bis = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            is = new FileInputStream(filename);
            dis = new DigestInputStream(is, md);
            bis = new BufferedInputStream(dis);
            byte[] buffer = new byte[8192];
            while (bis.read(buffer) > -1) ;
            byte[] md5sum = md.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            result = String.format("%032x", bigInt);
        } finally {
            if (bis != null) bis.close();
            if (dis != null) dis.close();
            if (is != null) is.close();
        }
        return result;
    }
