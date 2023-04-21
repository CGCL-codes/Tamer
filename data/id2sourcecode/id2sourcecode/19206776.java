    public static byte[] getMd5(File fileLook) throws IOException {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            DigestInputStream digestIn = new DigestInputStream(new BufferedInputStream(new FileInputStream(fileLook)), md5);
            digestIn.on(true);
            for (; ; ) {
                int iRead = digestIn.read();
                if (iRead < 0) {
                    break;
                }
            }
            digestIn.close();
            return md5.digest();
        } catch (IOException ex) {
            System.out.println(ex.toString());
            ex.printStackTrace();
        } catch (java.security.NoSuchAlgorithmException ex) {
            System.out.println(ex.toString());
            ex.printStackTrace();
        }
        return null;
    }
