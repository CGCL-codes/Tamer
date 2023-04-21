    private String generateFilename(byte[] data) {
        String encoded = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(data);
            String digestSTR = new BASE64Encoder().encode(digest);
            encoded = URLEncoder.encode(digestSTR, "UTF-8");
        } catch (NoSuchAlgorithmException noAlg) {
            noAlg.printStackTrace();
        } catch (UnsupportedEncodingException badEnc) {
            badEnc.printStackTrace();
        }
        return encoded;
    }
