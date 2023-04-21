    public static String getSignature(File f) throws IOException {
        try {
            final AsciiEncoder coder = new AsciiEncoder();
            final MessageDigest msgDigest = MessageDigest.getInstance("MD5");
            final FileInputStream is = new FileInputStream(f);
            int read = -1;
            while ((read = is.read()) != -1) {
                msgDigest.update((byte) read);
            }
            is.close();
            final byte[] digest = msgDigest.digest();
            return coder.encode(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }
