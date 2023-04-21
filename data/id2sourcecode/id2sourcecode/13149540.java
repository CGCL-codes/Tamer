    public static void main(String[] args) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StringWriter sw = new StringWriter();
        char[] b1 = new char[1024 * 64];
        byte[] b2 = new byte[1024 * 64];
        if (args[0].equals("e")) {
            Reader r = getEncodingReader(System.in);
            int data = -1;
            while ((data = r.read(b1)) != -1) sw.write(b1, 0, data);
            System.out.print(sw.toString());
        } else if (args[0].equals("d")) {
            InputStream r = getDecodingInputStream(new InputStreamReader(System.in));
            int d = -1;
            while ((d = r.read(b2)) != -1) baos.write(b2, 0, d);
            System.out.print(baos.toString());
        }
    }
