    public static synchronized byte[] getBytesFromStream(InputStream in) {
        byte[] bytes = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) >= 0) out.write(buffer, 0, len);
            in.close();
            out.close();
            bytes = out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }
