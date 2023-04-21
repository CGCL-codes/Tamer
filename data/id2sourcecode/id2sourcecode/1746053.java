    private byte[] loadBytes(String resPath) {
        try {
            InputStream is = Support_Resources.getResourceStream(resPath);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int readlen;
            while ((readlen = is.read(buff)) > 0) {
                out.write(buff, 0, readlen);
            }
            is.close();
            return out.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }
