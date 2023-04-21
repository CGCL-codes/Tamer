    public static String generate_MD5(File f) {
        InputStream is = null;
        String output;
        byte[] buffer = new byte[8192];
        int read = 0;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            is = new FileInputStream(f);
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            output = bigInt.toString(16);
        } catch (IOException e) {
            throw new RuntimeException("FileTools: Impossibile processare il file per il calcolo dell'md5 ", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("FileTools: Algoritmo di hashing non trovato ", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException("FileTools: Impossibile chiudere il file per calcolo md5 ", e);
            }
        }
        return output;
    }
