    static String getBase64Contents(InputStream in) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Base64OutputStream out = new Base64OutputStream(bytes);
        byte[] buf = new byte[1024];
        int read;
        while ((read = in.read(buf)) >= 0) out.write(buf, 0, read);
        out.close();
        return bytes.toString("UTF-8");
    }
