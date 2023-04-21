    public String calcHash(File file) {
        String hash = null;
        if (digest == null) {
            log.error("Hash cannot be calculated. The MessageDigest has not been initialised probably.");
            hash = "0";
        } else {
            try {
                byte[] buffer = new byte[HASHBUFFER_SIZE];
                int len;
                FileInputStream fileIn;
                fileIn = new FileInputStream(file);
                while ((len = fileIn.read(buffer)) >= 0) {
                    digest.update(buffer, 0, len);
                }
                fileIn.read(buffer);
                fileIn.close();
            } catch (FileNotFoundException e) {
                log.error(e.getMessage());
            } catch (IOException e) {
                log.error(e.getMessage());
            }
            hash = new BigInteger(digest.digest()).toString(16);
        }
        return hash;
    }
