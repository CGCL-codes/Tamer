    public static byte[] readBytes(InputStream is) throws IOException {
        byte[] data = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        try {
            byte[] buffer = new byte[512];
            int read = -1;
            while ((read = bis.read(buffer, 0, buffer.length)) != -1) {
                bos.write(buffer, 0, read);
            }
            data = bos.toByteArray();
        } finally {
            if (is != null) {
                is.close();
            }
            if (bos != null) {
                bos.close();
            }
            if (bis != null) {
                bis.close();
            }
        }
        return data;
    }
