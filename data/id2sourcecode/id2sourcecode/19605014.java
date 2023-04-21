    public static byte[] decode(String s) {
        byte[] bytes;
        try {
            bytes = s.getBytes(PREFERRED_ENCODING);
        } catch (java.io.UnsupportedEncodingException uee) {
            bytes = s.getBytes();
        }
        bytes = decode(bytes, 0, bytes.length);
        if (bytes.length >= 2) {
            int head = ((int) bytes[0] & 0xff) | ((bytes[1] << 8) & 0xff00);
            if (bytes != null && bytes.length >= 4 && java.util.zip.GZIPInputStream.GZIP_MAGIC == head) {
                java.io.ByteArrayInputStream bais = null;
                java.util.zip.GZIPInputStream gzis = null;
                java.io.ByteArrayOutputStream baos = null;
                byte[] buffer = new byte[2048];
                int length;
                try {
                    baos = new java.io.ByteArrayOutputStream();
                    bais = new java.io.ByteArrayInputStream(bytes);
                    gzis = new java.util.zip.GZIPInputStream(bais);
                    while ((length = gzis.read(buffer)) >= 0) baos.write(buffer, 0, length);
                    bytes = baos.toByteArray();
                } catch (java.io.IOException e) {
                } finally {
                    try {
                        baos.close();
                    } catch (Exception e) {
                    }
                    try {
                        gzis.close();
                    } catch (Exception e) {
                    }
                    try {
                        bais.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
        return bytes;
    }
