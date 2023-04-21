    public static byte[] generate(String file, String algo) {
        FileInputStream fis = null;
        byte buffer[] = new byte[8192];
        int i = 0;
        if (!algo.equals(ALGO_SHA1) && !algo.equals(ALGO_MD5)) {
            throw new IllegalArgumentException("given algo: " + algo + ", use DigitalFingerPrint.ALGO_MD5 or DigitalFingerPrint.ALGO_SHA1");
        }
        try {
            md = MessageDigest.getInstance(algo);
            fis = new FileInputStream(file);
            while ((i = fis.read(buffer)) != -1) {
                md.update(buffer, 0, i);
            }
        } catch (NoSuchAlgorithmException ex) {
            log.error("Algorithm not found", ex);
            return null;
        } catch (FileNotFoundException ex) {
            log.error("File not found", ex);
            return null;
        } catch (IOException ex) {
            log.error("reading file failed", ex);
            return null;
        }
        return md.digest();
    }
