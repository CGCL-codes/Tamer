    private void savePayload(Hashtable params, BufferedInputStream is) throws java.io.IOException {
        int c;
        PushbackInputStream input = new PushbackInputStream(is, 128);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while ((c = read(input, boundary)) >= 0) out.write(c);
        int id = Integer.parseInt((String) params.get("ID"));
        saveBlob(id, (String) params.get("filename"), out.toByteArray());
        out.close();
    }
