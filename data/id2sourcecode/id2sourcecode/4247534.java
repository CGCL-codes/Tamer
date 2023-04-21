    public static String getMD5sum(File file) {
        String md5sum = null;
        try {
            InputStream inputStream = new FileInputStream(file);
            MessageDigest msgDigest = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int read = 0;
            try {
                while ((read = inputStream.read(buffer)) > 0) {
                    msgDigest.update(buffer, 0, read);
                }
                byte[] md5sumbytes = msgDigest.digest();
                char[] md5sumchars = Hex.encodeHex(md5sumbytes);
                md5sum = String.copyValueOf(md5sumchars);
                _log.debug("Processed MD5 sum [" + md5sum + "]");
            } catch (IOException e) {
                _log.error("Unable to process MD5 from file [" + file.getAbsolutePath() + "]", e);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    _log.error("Unable to close input stream!", e);
                }
            }
        } catch (NoSuchAlgorithmException nosae) {
            _log.error(nosae, nosae);
        } catch (FileNotFoundException fnfe) {
            _log.error(fnfe, fnfe);
        }
        return md5sum;
    }
