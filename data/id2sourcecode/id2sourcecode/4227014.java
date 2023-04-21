    public static String toString(InputStream in) throws IOException {
        if (in == null) return null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int i;
        while ((i = in.read()) > -1) out.write(i);
        return new String(out.toByteArray());
    }
