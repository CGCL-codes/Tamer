    private static byte[] getBytes(InputStream source) {
        byte[] buf = new byte[512];
        try {
            BufferedInputStream in = new BufferedInputStream(source);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int count;
            while ((count = in.read(buf)) > -1) out.write(buf, 0, count);
            in.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }
