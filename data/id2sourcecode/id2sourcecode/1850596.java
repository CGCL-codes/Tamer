    static String buildMD5(String text) {
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            {
                final byte[] bytes = text.getBytes("UTF-8");
                text = null;
                md.update(bytes, 0, bytes.length);
            }
            final byte[] md5hash = md.digest();
            return convertToHex(md5hash);
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 is missing", e);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is missing", e);
        }
    }
