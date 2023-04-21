    public static String getMd5(final String text) {
        try {
            final int md5hashSize = 32;
            MessageDigest md;
            md = MessageDigest.getInstance("MD5");
            byte[] md5hash = new byte[md5hashSize];
            final byte[] utf8Bytes = text.getBytes("UTF-8");
            md.update(utf8Bytes, 0, utf8Bytes.length);
            md5hash = md.digest();
            return convertToHex(md5hash);
        } catch (final NoSuchAlgorithmException e) {
            Log.e(TAG, "MD5 error", e);
            return "";
        } catch (final UnsupportedEncodingException e) {
            Log.e(TAG, "MD5 error", e);
            return "";
        }
    }
