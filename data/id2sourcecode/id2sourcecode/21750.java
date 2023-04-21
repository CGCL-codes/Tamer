    public static void main(String[] args) throws Exception {
        InputStream in = new BufferedInputStream(new FileInputStream(new File(args[0])));
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int b;
        while ((b = in.read()) != -1) {
            bout.write(b);
        }
        processRequest(bout.toByteArray());
    }
