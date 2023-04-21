    public static byte[] generateSHA1(final File selectedFile) {
        byte[] output = null;
        try {
            InputStream is = new FileInputStream(selectedFile);
            byte[] buffer = new byte[1024];
            MessageDigest md = MessageDigest.getInstance("SHA1");
            int numRead;
            do {
                numRead = is.read(buffer);
                if (numRead > 0) {
                    md.update(buffer, 0, numRead);
                }
            } while (numRead != -1);
            output = md.digest();
        } catch (NoSuchAlgorithmException e) {
            SignData.log.info("error finding algorithm", e);
        } catch (FileNotFoundException e) {
            SignData.log.info("error finding file", e);
        } catch (IOException e) {
            SignData.log.info("i/o error", e);
        }
        return output;
    }
