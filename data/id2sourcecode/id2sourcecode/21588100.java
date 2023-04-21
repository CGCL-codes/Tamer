    public byte[] getBytesFromURL(String address) {
        try {
            URL url = new URL(address);
            URLConnection conn = url.openConnection();
            InputStream fis = conn.getInputStream();
            ByteArrayOutputStream byteArrOut = new ByteArrayOutputStream();
            int ln;
            byte[] buf = new byte[1024 * 12];
            while ((ln = fis.read(buf)) != -1) {
                byteArrOut.write(buf, 0, ln);
            }
            return byteArrOut.toByteArray();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException("Error while getting content from Url: " + address, ex);
        }
    }
