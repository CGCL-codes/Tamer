    private static byte[] readPostData(String input) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = null;
        if (input == null) {
            System.out.println("Reading post-data from input stream, hit EOF when done");
            in = System.in;
        } else if ("-".equals(input)) {
            in = System.in;
        } else {
            in = new FileInputStream(input);
        }
        byte[] buf = new byte[1024];
        int read = 0;
        while ((read = in.read(buf)) > 0) {
            out.write(buf, 0, read);
        }
        in.close();
        return out.toByteArray();
    }
