    public String readString() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = -1; (i = read()) > 0; ) baos.write(i);
        return new String(baos.toByteArray(), "UTF-8");
    }
