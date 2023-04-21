    public static void main(String[] args) throws Exception {
        byte[] c = new byte[1024];
        int i;
        Writer w = new CanonicalWriter(System.out);
        while ((i = System.in.read(c)) > 0) w.write(new String(c, 0, i));
    }
