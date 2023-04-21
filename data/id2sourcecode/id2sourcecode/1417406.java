    public String count(String pathToFile) {
        String output = "";
        try {
            File f = new File(pathToFile);
            if (f.exists()) {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                InputStream is = new FileInputStream(f);
                byte[] buffer = new byte[819200];
                int read = 0;
                while ((read = is.read(buffer)) > 0) {
                    digest.update(buffer, 0, read);
                }
                byte[] md5sum = digest.digest();
                BigInteger bigInt = new BigInteger(1, md5sum);
                output = bigInt.toString(16);
                is.close();
            } else {
                return "";
            }
        } catch (Exception e) {
            Ini.logger.fatal("Error: ", e);
        }
        return output;
    }
