    private String getMD5(File f) {
        MessageDigest digest;
        String output = "";
        InputStream is;
        try {
            is = new FileInputStream(f);
            try {
                digest = MessageDigest.getInstance("MD5");
                is = new FileInputStream(f);
                byte buffer[] = new byte[512 * 1024];
                int read = 0;
                while ((read = is.read(buffer)) > 0) {
                    digest.update(buffer, 0, read);
                }
                byte[] md5sum = digest.digest();
                BigInteger bigInt = new BigInteger(1, md5sum);
                output = bigInt.toString(16);
                is.close();
            } catch (IOException e) {
                getLogger().log(Level.INFO, "Unable to process file for MD5. " + f);
                throw new RuntimeException("Unable to process file for MD5", e);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    getLogger().log(Level.INFO, "Unable to close file at the end of MD5. " + f);
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e1) {
            getLogger().log(Level.INFO, "File not found for MD5. " + f);
            e1.printStackTrace();
        }
        return output;
    }
