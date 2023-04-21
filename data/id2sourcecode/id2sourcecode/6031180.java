    public static void main(String argv[]) throws Exception {
        int b;
        LineCounter os = new LineCounter(System.out);
        while ((b = System.in.read()) >= 0) os.write(b);
        os.flush();
        System.out.println(os.getLineCount());
    }
