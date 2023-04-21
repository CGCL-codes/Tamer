    public static String getMD5(String filename) {
        {
            InputStream is = null;
            try {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                File f = new File(filename);
                is = new FileInputStream(f);
                byte[] buffer = new byte[8192];
                int read = 0;
                try {
                    while ((read = is.read(buffer)) > 0) {
                        digest.update(buffer, 0, read);
                    }
                    byte[] md5sum = digest.digest();
                    BigInteger bigInt = new BigInteger(1, md5sum);
                    String ret = bigInt.toString(16);
                    if (ret.length() == 31) ret = "0" + ret;
                    if (ret.length() == 30) ret = "00" + ret;
                    if (ret.length() == 29) ret = "000" + ret;
                    return ret;
                } catch (IOException e) {
                    throw new RuntimeException("Unable to process file for MD5", e);
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        throw new RuntimeException("Unable to close input stream for MD5 calculation", e);
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(TrubenResource.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(TrubenResource.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    is.close();
                } catch (IOException ex) {
                    Logger.getLogger(TrubenResource.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return "";
        }
    }
